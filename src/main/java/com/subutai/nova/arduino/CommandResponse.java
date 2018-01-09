package com.subutai.nova.arduino;

public class CommandResponse {

    private byte id;
    private short callbackId;
    private byte[] bytes;

    public static CommandResponse fromBytes(byte[] bytes) {
        CommandResponse response = new CommandResponse();
        response.id = bytes[0];
        response.callbackId = BinaryConverter.getShort(new byte[]{bytes[1], bytes[2]});
        byte[] payload = new byte[bytes.length - 3];
        response.bytes = payload;
        ByteArrayUtil.copy(bytes, response.bytes, 3, 0, response.bytes.length);
        return response;
    }

    public byte getId() {
        return id;
    }

    public short getCallbackId(){
        return this.callbackId;
    }

    public byte[] getBytes(){
        return this.bytes;
    }

}
