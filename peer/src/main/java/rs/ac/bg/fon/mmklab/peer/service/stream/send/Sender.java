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
                    receiveSocket = new ServerSocket(configuration.getLocalPort());
                    System.out.println("Sender: STARTED");

                    while (true) {
                        System.out.println("Sender: awaiting connection");

                        communicationSocket = receiveSocket.accept();
                        System.out.println("Desilo se prihvatanje veze i otvaranje soketa");
                        PeerHandler handler = new PeerHandler(communicationSocket);  /// treba ovde try/catch
//                        handlers.add(handler);
                        handler.start();
                        System.out.println("Sender: connection activated, number of connections: " + handlers.size());

                    }
                } catch (IOException e) {
                    System.err.println("ERROR: Could not open receive socket on given port");
                }
                return null;
            }
        };
    }
}
/*try {
            receiveSocket = new ServerSocket(configuration.getLocalPort());
            System.out.println("Sender: STARTED");

            while (true) {
                System.out.println("Sender: awaiting connection");

                communicationSocket = receiveSocket.accept();
                PeerHandler handler = new PeerHandler(communicationSocket);  /// treba ovde try/catch
                handlers.add(handler);
                handler.run();
                System.out.println("Sender: connection activated, number of connections: " + handlers.size());

            }
        } catch (IOException e) {
            System.err.println("ERROR: Could not open receive socket on given port");
        }*/