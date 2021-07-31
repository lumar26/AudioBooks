package rs.ac.bg.fon.mmklab.peer.service.stream.receive;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import rs.ac.bg.fon.mmklab.peer.service.stream.Signal;

import java.io.IOException;

public class Signaler extends Service<Signal> {

    private final Signal signal;
    private final ReceiverInstance receiver;

    public Signaler(Signal signal, Receiver receiver) {
        this.signal = signal;
        this.receiver = receiver.getInstance();
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                System.out.println(">>   Pokrenuta je Signaler nit");
                switch (signal) {
                    case TERMINATE: {
                        terminate();
                    }
                    break;
                    case PAUSE: {

                    }
                    break;
                    case RESUME: {

                    }
                    break;
                    default:
                        break;
                }
                return null;
            }
        };
    }

    private void terminate(){
        receiver.getToSender().println(Signal.TERMINATE);
        try {
            if (receiver.getFromSender().readLine().equals("Signal accepted")) {
                System.out.println("Posiljalac prihvatio signal za prekid");
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Posiljalac nije prihvatio signal za prekid");
        }
        receiver.getSourceLine().stop();

    }

    private void pause() {
        receiver.getToSender().println(Signal.PAUSE);
        try {
            if (receiver.getFromSender().readLine().equals("Signal accepted")) {
                System.out.println("Posiljalac prihvatio signal za pauzu");
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Posiljalac nije prihvatio signal za pauzu");
        }
        receiver.getSourceLine().close();
    }
}
