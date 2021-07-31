package rs.ac.bg.fon.mmklab.peer.service.stream.send;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import rs.ac.bg.fon.mmklab.peer.domain.Configuration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Sender extends Service<Configuration> {
    private ServerSocket receiveSocket;
    private Socket communicationSocket;

    private Configuration configuration;

    private List<PeerHandler> handlers;

    public Sender(Configuration configuration) {
        this.configuration = configuration;
    }



    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    receiveSocket = new ServerSocket(configuration.getLocalPortTCP());
                    System.out.println("Sender: STARTED");

                    while (true) {
                        System.out.println("Sender: awaiting connection");

                        communicationSocket = receiveSocket.accept();
                        PeerHandler handler = PeerHandler.createInstance(communicationSocket, configuration);  /// treba ovde try/catch
                        System.out.println("Desilo se prihvatanje veze i otvaranje soketa");
                        handler.start();
                    }
                } catch (IOException e) {
                    System.err.println("ERROR: Could not open receive socket on given port");
                }
                return null;
            }
        };
    }
}