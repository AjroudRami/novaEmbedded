package com.subutai.nova.arduino.command;

import java.nio.ByteBuffer;

public class Turn extends ArduinoCommand {

    private short angle;

    public Turn(float angle) {
        setAngle(angle);
    }

    private void setAngle(float angle) {
        angle = angle % 360;
        if (angle < 0) {
            angle = -angle;
        }
        angle = angle * 100;
        this.angle = (short) angle;
    }

    @Override
    public byte[] getDescription() {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(angle);
        return buffer.array();
    }
}
