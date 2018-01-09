package com.subutai.nova.arduino;

import com.pi4j.io.serial.*;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton(name="ArduinoBoard")
@Startup
public class ArduinoBoard {
    private final Serial serial = SerialFactory.createInstance();
    private boolean online;
    private Logger LOGGER = Logger.getLogger(this.getClass().getSimpleName());
    private static final int initTryMax = 3;
    private static int initTry = 0;

    @PostConstruct
    public void init(){
        LOGGER.log(Level.INFO, "init Arduino Board");
        try {
            SerialConfig config = new SerialConfig();
            // set default serial settings (device, baud rate, flow control, etc)
            //
            // by default, use the DEFAULT com port on the Raspberry Pi (exposed on GPIO header)
            // NOTE: this utility method will determine the default serial port for the
            //       detected platform and board/model.  For all Raspberry Pi models
            //       except the 3B, it will return "/dev/ttyAMA0".  For Raspberry Pi
            //       model 3B may return "/dev/ttyS0" or "/dev/ttyAMA0" depending on
            //       environment configuration.
            config.device("/dev/ttyACM0")
                    .baud(Baud._38400)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.HARDWARE);
            serial.open(config);
            LOGGER.log(Level.INFO, "init Arduino Board success");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to init arduino board");
            if (initTry < initTryMax) {
                LOGGER.log(Level.INFO, "Trying to init again");
                initTry++;
                init();
            } else {
                LOGGER.log(Level.SEVERE, "Failed to init arduino board," +
                        " reached maximum attempt (" + initTryMax + ") without success");
            }
        }
    }

    public void write(byte[] bytes) throws IOException {
        serial.write(bytes);
    }

    public byte[] read(int i) throws IOException {
        return serial.read(i);
    }

    public InputStream getInputStream() {
        return serial.getInputStream();
    }

    public OutputStream getOutputStream() {
        return serial.getOutputStream();
    }
}