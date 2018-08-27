package proguard;


import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
public class FlowTraceWriter {
    public static boolean IsInitialized() {
        return Initialized;
    }
    public static synchronized void write(boolean b) {
//        try {
//            Thread.sleep(0, 1);
//        }
//        catch (Exception e) {
//        }
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        boolean enter = b;
        int this_fn = getUnicId(stackTraceElements, 2);
        int call_site = getUnicId(stackTraceElements, 3);
        int fn_line = getLineNumber(stackTraceElements, 2);
        int call_line = getLineNumber(stackTraceElements, 3);
        String methodName = getMethodNmae(stackTraceElements, 2);
        writeTrace(enter, this_fn, call_site, fn_line, call_line, methodName);

//        String descr = (b ? "Before: " : "After: ");
//        printStack(stackTraceElements, descr);
//        System.out.println(descr + "AAA " + packetNN + " " + methodName + " " + fn_line + " " + call_line);
    }
    static void printStack(StackTraceElement[] stackTraceElements, String descr) {
        System.out.println(descr + " began stack >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        for (int i = 0; i < stackTraceElements.length; i++)
        {
            System.out.println( i + ". " + stackElemToString(stackTraceElements, i));
        }
        System.out.println(descr + "end stack <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
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

    static void writeTrace(boolean enter, int this_fn, int call_site, int fn_line, int call_line, String methodName) {
        if (!Initialized)
            return;
        try {
            int cb_app_name = 1;
            int cb_trace = 0;
            byte[] methodNameBuf = encodeUTF8(methodName);
            int cb_fn_name = methodNameBuf.length;
            // UDP_PACK_INFO
            byteBuffer.position(0);
            byteBuffer.putInt(0);//int data_len;
            byteBuffer.putInt(0);//unsigned int term_sec;
            byteBuffer.putInt(0);//unsigned int term_msec;
            int sizeof_UDP_PACK_INFO = byteBuffer.position();
            // LOG_REC
            byteBuffer.putInt(0);//int len;
            byteBuffer.putShort((short)(enter ? 0 : 1));//int log_type; LOG_INFO_ENTER, LOG_INFO_EXIT
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
}
