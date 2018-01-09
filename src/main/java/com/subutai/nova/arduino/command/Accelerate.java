package com.subutai.nova.arduino.command;

import java.nio.ByteBuffer;

public class Accelerate extends ArduinoCommand {

    private short acceleration;
    private byte direction;

    public Accelerate(float value, byte direction){
        super.id = Commands.ACCELERATE;
        setPercentage(value);
        this.direction = direction;
    }

    private void setPercentage(float value) {
        short res;
        if (value < 0) {
            res = 0;
        } else if (value > 1) {
            res = 1024;
        } else {
            res = (short) (1024 * value);
        }
        this.acceleration = res;
    }

    @Override
    public byte[] getDescription() {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.putShort(acceleration);
        buffer.put(direction);
        return buffer.array();
    }
}
