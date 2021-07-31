package rs.ac.bg.fon.mmklab.peer.service.stream.send;

import rs.ac.bg.fon.mmklab.peer.domain.Configuration;

import javax.sound.sampled.AudioInputStream;
import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class PeerHandlerInstance {
    private final Socket socket;
    private final PrintStream toReceiver;
    private final BufferedReader fromReceiver;
    private final Configuration configuration;

    private int remotePortUDP;

    /*soketi, tokovi i paketi za slanje audio zapisa*/
    private final DatagramSocket datagramSocket;
    private final InetAddress receiverAddress;
    private File audioFile;
    private AudioInputStream audioInputStream;
    private long framesSent;

    //    getter metode
    public Socket getSocket() {
        return socket;
    }

    public PrintStream getToReceiver() {
        return toReceiver;
    }

    public BufferedReader getFromReceiver() {
        return fromReceiver;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public int getRemotePortUDP() {
        return remotePortUDP;
    }

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public InetAddress getReceiverAddress() {
        return receiverAddress;
    }

    public File getAudioFile() {
        return audioFile;
    }

    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }

    public long getFramesSent() {
        return framesSent;
    }

//    kraj getter metoda


//    za promenljive koje moramo da postavljamo u runtime
    public void setRemotePortUDP(int remotePortUDP) {
        this.remotePortUDP = remotePortUDP;
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }

    public void setAudioInputStream(AudioInputStream audioInputStream) {
        this.audioInputStream = audioInputStream;
    }

    public void setFramesSent(long framesSent) {
        this.framesSent = framesSent;
    }



//    konstruktor
    private PeerHandlerInstance(Socket socket, Configuration configuration) throws IOException {
        this.socket = socket;
        this.configuration = configuration;


        this.toReceiver = new PrintStream(socket.getOutputStream());
        this.fromReceiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        /* za novu napravljenu instancu postavljamo adresu primaoca na osnovu soketa prosledjenog iz Sender niti
        *  postavljamo novi obican datagram soket preko koga cemo da saljemo pakete audio snimka */
        this.receiverAddress = this.socket.getInetAddress();
        this.datagramSocket =  new DatagramSocket();

        this.framesSent = 0;
    }

    public static PeerHandlerInstance createHandlerInstance(Socket socket, Configuration configuration) throws IOException {
        return new PeerHandlerInstance(socket, configuration);
    }
}
