package com.subutai.nova.arduino.command;

public class Brake extends ArduinoCommand {

    public Brake() {
        super.id = Commands.BRAKE;
    }

    @Override
    public byte[] getDescription() {
        return new byte[0];
    }
}
