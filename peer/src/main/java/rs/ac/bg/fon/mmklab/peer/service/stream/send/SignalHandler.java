package rs.ac.bg.fon.mmklab.peer.service.stream.send;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import rs.ac.bg.fon.mmklab.peer.service.stream.Signal;

import java.io.IOException;

public class SignalHandler extends Service {


    private long framesRead;

    private final PeerHandlerInstance peerHandlerInstance;

    public SignalHandler(PeerHandlerInstance handlerInstance) {
        this.peerHandlerInstance = handlerInstance;
    }


    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                while (!peerHandlerInstance.getSocket().isClosed()) {
                    System.out.println(">>    Pokrenut je Signal handler; ocekujemo zahtev; nit: " + Thread.currentThread());
                    Signal signal = Signal.valueOf(peerHandlerInstance.getFromReceiver().readLine());
                    switch (signal) {
                        case TERMINATE:
                            terminate();
                            break;
                        case PAUSE: {
                            pause();
                        }
                        break;
                        default:
                            break;
                    }
                }
                return null;
            }
        };
    }

    private void terminate() {
        peerHandlerInstance.getToReceiver().println("Signal accepted");
//        ne moramo da prekidamo ovde nikakvo slanje jer ce svakako peer da zatvori sourceDataLine i onda stajemo sa slanjem
    }

    private void pause(){
        System.out.println("Pauziranje prenosa u toku");
        peerHandlerInstance.getToReceiver().println("Signal accepted");
        try {
            framesRead = Integer.parseInt(peerHandlerInstance.getFromReceiver().readLine());
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("(SignalHandler --> pause): neuspesan prijem broja procitanih frejmova od strane primaoca");
        }
        System.out.println("(Signal handler): korisnik prekinuo video, do sada je porcitano frejmova: " + framesRead);
    }
}
