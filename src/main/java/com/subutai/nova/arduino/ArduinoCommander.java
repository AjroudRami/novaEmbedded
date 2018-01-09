package com.subutai.nova.arduino;

import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.subutai.nova.arduino.command.ArduinoCallbackCommand;
import com.subutai.nova.arduino.command.ArduinoCommand;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
@DependsOn("ArduinoBoard")
public class ArduinoCommander implements SerialDataEventListener {

    private static Logger LOGGER = Logger.getLogger(ArduinoBoard.class.getName());
    private static CallbackRegistry registry;
    @EJB
    ArduinoBoard board;

    @PostConstruct
    private void init(){
        registry = new CallbackRegistry();
    }

    public boolean sendCommand(ArduinoCommand command){
        LOGGER.log(Level.INFO, "Sending command " + command.getId());
        try {
            board.write(command.parseCommand());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "IOException raised while sending command: " + command.getId());
            return false;
        } catch (CommandSizeExceedException c) {
            LOGGER.log(Level.WARNING, "Command size exceed the maximum size, not sent. id = " + command.getId());
        }
        return true;
    }

    /**
     * send a Command to the ArduinoBoard
     * if an error is thrown while sending, the onFailure method of the CommandCallback will be triggered with
     * an IOEXception FailureResponse
     * if no response is received from the arduino within the time window,
     * a timeout FailureResponse will be sent to the onFailure method;
     * @param command
     * @return
     */
    public boolean sendCommand(ArduinoCallbackCommand command){
        LOGGER.log(Level.INFO, "Sending command " + command.getId());
        try {
            registry.registerCallbackCommand(command);
            board.write(command.getDescription());
        } catch (IOException e) {
            command.onFailure(FailureResponse.IOException);
            registry.removeCallback(command);
            return false;
        } catch (CallbackRegistryException e) {
            command.onFailure(FailureResponse.RegistryError);
            return false;
        }
        return true;
    }

    @Override
    public void dataReceived(SerialDataEvent serialDataEvent) {
        try {
            byte[] rawResponse = serialDataEvent.getBytes();
            CommandResponse response = CommandResponse.fromBytes(rawResponse);
            registry.notifyResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
