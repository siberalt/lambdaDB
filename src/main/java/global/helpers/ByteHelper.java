package global.helpers;

public class ByteHelper {

    public static byte[] intToBytes(int value) {
        return new byte[]{
            (byte) (value >>> 24),
            (byte) (value >>> 16),
            (byte) (value >>> 8),
            (byte) value
        };
    }

    public static int BytesToInt(byte[] bytes) {
        int value = 0;

        for (int i = 4; i >= 0; i--) {
            value = (value | bytes[i]) << 8 * i;
        }

        return value;
    }

    public static long BytesToLong(byte[] bytes) {
        long value = 0;

        for (int i = 8; i >= 0; i--) {
            value = (value | bytes[i]) << 8 * i;
        }

        return value;
    }

    public static byte[] LongToBytes(long value) {
        return new byte[]{
            (byte) (value >>> 56),
            (byte) (value >>> 48),
            (byte) (value >>> 40),
            (byte) (value >>> 32),
            (byte) (value >>> 24),
            (byte) (value >>> 16),
            (byte) (value >>> 8),
            (byte) value
        };
    }


}
