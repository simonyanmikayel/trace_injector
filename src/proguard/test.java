package proguard;

public class test {
    public static void main(String[] args) {
        byte[] buf = new byte[1000];
        initBuf(buf);
        int offset = 10;
        int coount = buf.length - 10;
        String descr = "abc";
        if (buf == null || offset < 0 || coount <= 0 || (offset + coount) > buf.length) {
            System.out.println("HexDump: Bad parameters: offset" + offset +" coount: " + coount);
            return;
        }
        int n = buf.length - offset;
        int cColumn = 32;
        StringBuffer sb = new StringBuffer(descr.length() + (n * 3) + (n / cColumn)*2 + 32);
        sb.append(descr + ":\n");
        for (int i = offset; i < n; i++) {

            if ((i % cColumn) == 0)
                sb.append("\n\t");

            int b = buf[i] & 0xFF;
            sb.append("("+b+")");
            if (b < 16) {
                sb.append('0');
                sb.append(halfBytToHex(buf[i]));
            } else {
                sb.append(halfBytToHex((b / 16)));
                sb.append(halfBytToHex((b % 16) ));
            }
            sb.append(' ');
        }
        System.out.print(sb.toString());
    }

    static void initBuf(byte[] buf) {
        byte b = 0;
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte)(i & 0xFF);
        }
    }

    static char halfBytToHex(int b) {
        if (b < 0)
            return '?';
        else if (b < 10)
            return (char)((byte)'0' + b);
        else if (b < 16)
            return (char)((byte)'A' + b - 10);
        else
            return '?';

    }
}
