package com.subutai.nova.arduino;

public class ByteArrayUtil {

    public static void copy(byte[] in, byte[] out, int inMinOffset, int outMinOffset, int length) {
        for (int i = inMinOffset,j = outMinOffset; i < length && j < length; i++, j++) {
            out[j] = in[i];
        }
    }
}
