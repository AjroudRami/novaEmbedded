package com.subutai.nova.arduino.command;

import com.subutai.nova.arduino.*;

public abstract class ArduinoCallbackCommand extends ArduinoCommand{

    private short callbackId;
    private CommandCallback callback;

    @Override
    public byte[] parseCommand() throws CommandSizeExceedException {
        byte[] description = getDescription();
        int headerSize = 4;
        int commandLength = description.length + headerSize;
        if (commandLength > MAXIMUM_COMMAND_SIZE) {
            throw new CommandSizeExceedException();
        }
        byte[] parsedCommand = new byte[commandLength];
        parsedCommand[0] = id;
        parsedCommand[1] = (byte) commandLength;
        parsedCommand[2] = BinaryConverter.toBinary(callbackId)[0];
        parsedCommand[3] = BinaryConverter.toBinary(callbackId)[1];
        ByteArrayUtil.copy(description, parsedCommand, 0, headerSize, description.length);
        return parsedCommand;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ArduinoCallbackCommand) {
            ArduinoCallbackCommand cmd = (ArduinoCallbackCommand) object;
            return callbackId == cmd.callbackId && id == cmd.id;
        }
        return false;
    }

    public void setCommandCallback(CommandCallback callback) {
        this.callback = callback;
    }

    public void onSuccess(CommandResponse response){
        this.callback.onSuccess(response);
    }

    public void onFailure(FailureResponse response) {
        this.callback.onFailure(response);
    }

    public void setCallbackId(short id){
        this.callbackId = id;
    }

    public short getCallbackId(){
        return this.callbackId;
    }
}
