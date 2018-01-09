package com.subutai.nova.arduino.command;

import com.subutai.nova.arduino.ByteArrayUtil;
import com.subutai.nova.arduino.CommandSizeExceedException;

public abstract class ArduinoCommand {

    protected byte id;
    public static final byte MAXIMUM_COMMAND_SIZE = 64;

    public byte[] parseCommand() throws CommandSizeExceedException {
        byte[] description = getDescription();
        int commandLength = description.length + 2;
        if (description.length > MAXIMUM_COMMAND_SIZE) {
            throw new CommandSizeExceedException();
        }
        byte[] parsedCommand = new byte[commandLength];
        parsedCommand[0] = id;
        parsedCommand[1] = (byte) commandLength;
        ByteArrayUtil.copy(description, parsedCommand, 0, 2, description.length);
        return parsedCommand;
    }

    /**
     * This method should return the command description in a byte array, Its length should not exceed 63 for an ArduinoCommand
     * or 62 for an ArduinoCallbackCommand. Due to hardware restrictions, the arduino serial byte buffer size is 64. T
     * rying to send a command that exceed this length will results in loss of data.
     * @return The command description in a byte array
     */
    public abstract byte[] getDescription();


    public byte getId(){
        return this.id;
    }
}
