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
                switch (signal) {
                    case TERMINATE: {
                        System.out.println(">>   TERMINATE");

                        terminate();
                    }
                    break;
                    case PAUSE: {
                        System.out.println(">>  PAUSE ");

                        pause();
                    }
                    break;
                    case RESUME: {
                        System.out.println(">>   RESUME");

                        resume();
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
            receiver.getToSender().println(receiver.getFramesRead());
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Posiljalac nije prihvatio signal za pauzu");
        }
//        receiver.getSourceLine().close();
    }

    private void resume(){
        receiver.getToSender().println(Signal.RESUME);
        System.out.println("POslat signal posiljaocu da nastavi slanje");
    }
}
