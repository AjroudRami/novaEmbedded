package com.subutai.nova.arduino;

public class BinaryConverter {

    public static byte[] toBinary(short number) {
        byte[] result = new byte[2];
        result[0] = (byte)(number & 0xff);
        result[1] = (byte)((number >> 8) & 0xff);
        return result;
    }

    public static short getShort(byte[] array){
        short result = (short) ((array[0] & 0xff) | ((array[1] & 0xff) << 8));
        return result;
    }
}
