package proguard;


import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
public class Logger {
    public static boolean IsInitialized() {
        return Initialized;
    }
    enum TRACE_TYPE {LOG_INFO_ENTER, LOG_INFO_EXIT, LOG_INFO_TRACE};
    public static synchronized void write(boolean enter) {
        writeTrace(enter ? TRACE_TYPE.LOG_INFO_ENTER : TRACE_TYPE.LOG_INFO_EXIT, null, 3);
    }
    static void printStack(StackTraceElement[] stackTraceElements, String descr) {
        Logger.out_println(descr + " began stack >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        for (int i = 0; i < stackTraceElements.length; i++)
        {
            Logger.out_println( i + ". " + stackElemToString(stackTraceElements, i));
        }
        Logger.out_println(descr + "end stack <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }
    static String stackElemToString(StackTraceElement[] stackTraceElements, int i) {
        return getMethodNmae(stackTraceElements, i)+":"+getLineNumber(stackTraceElements, i);
    }
    static int getUnicId(StackTraceElement[] stackTraceElements, int i)
    {
        if (i >= stackTraceElements.length)
            return 0;
        return getMethodNmae(stackTraceElements, i).hashCode() + stackTraceElements.length;
    }
    static int getLineNumber(StackTraceElement[] stackTraceElements, int i)
    {
        if (i >= stackTraceElements.length)
            return -1;
        return stackTraceElements[i].getLineNumber();
    }
    static String getMethodNmae(StackTraceElement[] stackTraceElements, int i)
    {
        if (i >= stackTraceElements.length)
            return "";
        StackTraceElement caller = stackTraceElements[i];
        String classname = caller.getClassName();
        String methodName = caller.getMethodName();
        return classname+"."+methodName;
    }

    static private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    static private DatagramSocket socket;
    static private InetAddress address;
    static private byte[] buf = new byte[1412];
    static ByteBuffer byteBuffer = ByteBuffer.wrap(buf);
    static private DatagramPacket packet;
    static int packetNN;
    static private boolean Initialized = Initialize();
    //static long pid = ProcessHandle.current().pid();
    static boolean Initialize() {
        String envVarTrace = System.getenv("FLOW_TRACE");
        if ( envVarTrace == null || envVarTrace.length() == 0)
            return false;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        packet = new DatagramPacket(buf, buf.length, address, 8889);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return true;
    }

    static void writeTrace(TRACE_TYPE type, String trace, int deep) {
        if (!Initialized)
            return;
        try {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            int this_fn = getUnicId(stackTraceElements, deep);
            int call_site = getUnicId(stackTraceElements, deep + 1);
            int fn_line = getLineNumber(stackTraceElements, deep);
            int call_line = getLineNumber(stackTraceElements, type == TRACE_TYPE.LOG_INFO_TRACE ?  deep : deep + 1);
            String methodName = getMethodNmae(stackTraceElements, deep);

            int cb_app_name = 1;
            byte[] methodNameBuf = encodeUTF8(methodName);
            int cb_fn_name = methodNameBuf.length;
            byte[] traceBuf = null;
            if (trace != null)
                traceBuf = encodeUTF8(trace);
            int cb_trace = (traceBuf == null) ? 0 : traceBuf.length;

            // UDP_PACK_INFO
            byteBuffer.position(0);
            byteBuffer.putInt(0);//int data_len;
            byteBuffer.putInt(0);//unsigned int term_sec;
            byteBuffer.putInt(0);//unsigned int term_msec;
            int sizeof_UDP_PACK_INFO = byteBuffer.position();
            // LOG_REC
            byteBuffer.putInt(0);//int len;
            byteBuffer.putShort((short)(type.ordinal()));//int log_type; JAVA_LOG_ENTER, JAVA_LOG_EXIT
            byteBuffer.putShort((short)2);//int log_flags
            byteBuffer.putInt(++packetNN);//int nn;
            byteBuffer.putShort((short)cb_app_name);//cb_app_name;
            byteBuffer.putShort((short)0);//cb_module_name;
            byteBuffer.putShort((short)cb_fn_name);//int cb_fn_name;
            byteBuffer.putShort((short)cb_trace);//int cb_trace;
            byteBuffer.putInt((int)Thread.currentThread().getId());//int tid;
            byteBuffer.putInt(0);//int pid;
            byteBuffer.putInt(0);//unsigned int sec;
            byteBuffer.putInt(0);//unsigned int msec;
            byteBuffer.putInt(this_fn);//int this_fn;
            byteBuffer.putInt(call_site);//int call_site;
            byteBuffer.putInt(fn_line);//int call_line;
            byteBuffer.putInt(call_line);//int call_line;
            int sizeof_LOG_REC = byteBuffer.position() - sizeof_UDP_PACK_INFO + 1;
            if (0 != (sizeof_LOG_REC & 0x3))
                sizeof_LOG_REC = ((sizeof_LOG_REC / 4) * 4) + 4; //len = ((len >> 2) << 2) + 4;

            byteBuffer.put((byte)'J');//char data[1];
            //TODO check buffer overflow
            byteBuffer.put(methodNameBuf);
            if(traceBuf != null)
                byteBuffer.put(traceBuf);
            byteBuffer.put((byte)0); //null terminator

            int len = sizeof_LOG_REC + cb_app_name + cb_fn_name + cb_trace;
            // make sure that length is 4-byte aligned
            if (0 != (len & 0x3))
                len = ((len / 4) * 4) + 4; //len = ((len >> 2) << 2) + 4;
            byteBuffer.putInt(0, len); //one data
            byteBuffer.putInt(sizeof_UDP_PACK_INFO, len);

            int package_len = len + sizeof_UDP_PACK_INFO; //just to check arithmetic
            packet.setLength(package_len);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            Initialized = false;
        }
    }

    static String decodeUTF8(byte[] bytes) {
        return new String(bytes, UTF8_CHARSET);
    }

    static byte[] encodeUTF8(String string) {
        return string.getBytes(UTF8_CHARSET);
    }

    public static void err_println() {
        trace("\n", true);
    }

    public static void out_println() {
        trace("\n", true);
    }

    public static void err_println(Object o) {
        trace(o.toString(), true);
    }

    public static void out_println(Object o) {
        trace(o.toString(), true);
    }

    public static void err_print(Object o) {
        trace(o.toString(), false);
    }

    public static void out_print(Object o) {
        trace(o.toString(), false);
    }

    private static void trace(String s, boolean newLine) {
        if (newLine)
            System.out.println(s);
        else
            System.out.print(s);
        if (newLine && (s.length() == 0 || s.charAt(s.length() - 1) != '\n'))
            s = s + "\n";
        writeTrace(TRACE_TYPE.LOG_INFO_TRACE, s, 4);
    }
}
