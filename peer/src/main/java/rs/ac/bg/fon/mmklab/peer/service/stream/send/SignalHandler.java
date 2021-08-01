package rs.ac.bg.fon.mmklab.peer.service.stream.send;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import rs.ac.bg.fon.mmklab.peer.service.stream.Signal;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.DatagramPacket;

public class SignalHandler extends Service {


    private final PeerHandler handler;

    public SignalHandler(PeerHandler handler) {
        this.handler = handler;
    }


    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                while (!handler.getInstance().getSocket().isClosed()) {
                    System.out.println(">>    Pokrenut je Signal handler; ocekujemo zahtev; nit: " + Thread.currentThread());
                    Signal signal = Signal.valueOf(handler.getInstance().getFromReceiver().readLine());
                    System.out.println("Primili smo signal : " + signal);
                    switch (signal) {
                        case TERMINATE:
                            terminate();
                            break;
                        case PAUSE: {
                            pause();
                        }
                        break;
                        case RESUME: {
                            resume();
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
        handler.getInstance().getToReceiver().println("Signal accepted");
//        ne moramo da prekidamo ovde nikakvo slanje jer ce svakako peer da zatvori sourceDataLine i onda stajemo sa slanjem
    }

    private void pause(){
        System.out.println("Pauziranje prenosa u toku");
        handler.getInstance().getToReceiver().println("Signal accepted");
        try {
            handler.getInstance().setFramesSent(Integer.parseInt(handler.getInstance().getFromReceiver().readLine()));
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("(SignalHandler --> pause): neuspesan prijem broja procitanih frejmova od strane primaoca");
        }
        System.out.println("(Signal handler): korisnik prekinuo video, do sada je porcitano frejmova: " + handler.getInstance().getFramesSent());
        try {
            handler.getInstance().getAudioInputStream().close();
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("(SignalHandler -> pause); nije moguce zatvoriti audioInputStream na strani posiljaoca");
        }
    }

    private void resume(){
        try {
            int frameSize = handler.getInstance().getAudioInputStream().getFormat().getFrameSize();
            byte[] sendBuffer = new byte[1024 * frameSize]; // ovde da se proveri da li je okej da bude 1024 bajta
            byte[] confirmationMessage = new byte[10];
            DatagramPacket confirmationPacket = new DatagramPacket(confirmationMessage, confirmationMessage.length);


            handler.getInstance().setAudioInputStream(AudioSystem.getAudioInputStream(handler.getInstance().getAudioFile()));
            handler.getInstance().getAudioInputStream().skip(handler.getInstance().getFramesSent() * frameSize);

            handler.send(handler.getInstance(), sendBuffer, frameSize, confirmationPacket);
            handler.closeTCPConnection(handler.getInstance().getSocket(), handler.getInstance().getToReceiver(), handler.getInstance().getFromReceiver());
            handler.closeUDPConnection(handler.getInstance().getDatagramSocket());


        } catch (UnsupportedAudioFileException e) {
//            e.printStackTrace();
            System.err.println("(SignalHandler -> resume): nismo mogli da ponovo napravimo tok od audio fajla, jer fajl nije podrzan");
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("(SignalHandler -> resume): nismo mogli da ponovo napravimo tok od audio fajla");
        }
    }

}
