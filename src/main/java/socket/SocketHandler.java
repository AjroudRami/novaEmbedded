package socket;

import com.subutai.socketrpc.PrioritizedCommunicator;
import com.subutai.socketrpc.core.InstructionSocketServer;
import javax.ejb.Singleton;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class SocketHandler {

    private static Logger LOGGER = Logger.getLogger(SocketHandler.class.getName());
    public final static int DEFAULT_SOCKET_PORT = 12345;

    private InstructionSocketServer server;
    private PrioritizedCommunicator serverCommunicator;

    public int startSocket() {
        LOGGER.log(Level.INFO, "request received");
        server = new InstructionSocketServer(DEFAULT_SOCKET_PORT);
        Thread serverThread = new Thread(server);
        serverCommunicator = new PrioritizedCommunicator(server);
        Thread serverCommunicatorThread = new Thread(serverCommunicator);
        serverThread.start();
        serverCommunicatorThread.start();
        LOGGER.log(Level.INFO, "sending response: " + DEFAULT_SOCKET_PORT);
        return DEFAULT_SOCKET_PORT;
    }

    public void closeSocket() {
        serverCommunicator.stop();
        server.stop();
    }

}
