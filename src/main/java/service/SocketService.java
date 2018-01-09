package service;

import socket.SocketHandler;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/socket")
@Produces(MediaType.APPLICATION_JSON)
public class SocketService {

    @EJB
    SocketHandler handler;

    @GET
    public int getSocket() {
        int port = handler.startSocket();
        return port;
    }

    @DELETE
    public String closeSocket(@QueryParam("address") String socketAddress,
                              @QueryParam("port") int port) {
        handler.closeSocket();
        return "OK";
    }
}
