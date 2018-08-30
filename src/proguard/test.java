package proguard;

public class test {
    public static void main(String[] args) {
        byte[] buf = new byte[1000];
        initBuf(buf);
        int offset = 10;
        int count = buf.length - 10;
        String descr = "abc";
        if (buf == null || offset < 0 || count <= 0 || (offset + count) > buf.length) {
            System.out.println("HexDump: Bad parameters: offset" + offset +" coount: " + count);
            return;
        }
        int cColumn = 32;
        descr = descr + " <- HexDump: offset = " + offset +" coount = " + count;
        StringBuffer sb = new StringBuffer(descr.length() + (count * 3) + (count / cColumn)*2 + 32);
        sb.append(descr);
        int end = offset + count;
        for (int i = offset; i < end;) {
            sb.append("\n\t");
            for ( int j = 0; j < cColumn && i < end; j++, i++) {
                int b = buf[i] & 0xFF;
                if (b < 16) {
                    sb.append('0');
                    sb.append(intToHex(buf[i]));
                } else {
                    sb.append(intToHex((b / 16)));
                    sb.append(intToHex((b % 16) ));
                }
                sb.append(' ');
            }
        }
        System.out.print(sb.toString());
    }

    static void initBuf(byte[] buf) {
        byte b = 0;
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte)(i & 0xFF);
        }
    }

    static char intToHex(int b) {
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
