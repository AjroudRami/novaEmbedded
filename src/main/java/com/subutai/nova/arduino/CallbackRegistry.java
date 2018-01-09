package com.subutai.nova.arduino;

import com.subutai.nova.arduino.command.ArduinoCallbackCommand;

import java.util.concurrent.*;

public class CallbackRegistry {

    private final int COMMAND_STORAGE_SIZE = 1024;
    private final long SLOT_TIMEOUT = 100;
    private ArduinoCallbackCommand[] commands;

    public CallbackRegistry(){
        this.commands = new ArduinoCallbackCommand[COMMAND_STORAGE_SIZE];
    }

    public synchronized void registerCallbackCommand(ArduinoCallbackCommand command) throws CallbackRegistryException {
        int slotID = getFreeSlot();
        if (slotID < 0) {
            throw new CallbackRegistryException();
        }
        command.setCallbackId((short) slotID);
        this.commands[slotID] = command;
        makeTimeout(slotID);
    }

    public synchronized void notifyResponse(CommandResponse response) {
        int id = response.getId();
        ArduinoCallbackCommand command = this.commands[id];
        command.onSuccess(response);
        this.commands[id] = null;
    }

    public synchronized void removeCallback(ArduinoCallbackCommand command) {
        this.commands[command.getCallbackId()] = null;
    }

    public synchronized int getFreeSlot(){
        for(int i = 0; i < commands.length; i++) {
            if(commands[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private synchronized void makeTimeout(int slotId){
        //TODO Executor service should be shutdown if the command fails to be sent.
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> {
            slotTimeout(slotId);
            scheduledExecutorService.shutdownNow();
        }, SLOT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private synchronized void slotTimeout(int slotId){
        if (this.commands[slotId] != null) {
            ArduinoCallbackCommand cmd = commands[slotId];
            cmd.onFailure(FailureResponse.Timeout);
            this.commands[slotId] = null;
        }
    }
}
