package socket;

import com.subutai.nova.arduino.ArduinoCommander;
import com.subutai.nova.arduino.command.Accelerate;
import com.subutai.nova.arduino.command.Brake;
import com.subutai.socketrpc.PrioritizedCommunicator;
import com.subutai.socketrpc.core.InstructionSocket;
import javax.ejb.EJB;

public class OperationCommunicator extends PrioritizedCommunicator {

    @EJB
    ArduinoCommander commander;

    public OperationCommunicator(InstructionSocket socket) {
        super(socket);
        bindOperations();
    }

    private void bindOperations() {
        bindAccelerate();
        bindBrake();
    }

    private void bindAccelerate(){
        super.registerOperationListener(Operations.ACCELERATE,
                e -> commander.sendCommand(
                        new Accelerate(e.getFloat(0), e.getByte(1))));
    }

    private void bindBrake(){
        super.registerOperationListener(Operations.BRAKE,
                e -> commander.sendCommand(new Brake()));
    }
}
