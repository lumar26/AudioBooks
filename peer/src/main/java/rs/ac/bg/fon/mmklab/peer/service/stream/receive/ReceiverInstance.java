package rs.ac.bg.fon.mmklab.peer.service.stream.receive;

import rs.ac.bg.fon.mmklab.book.AudioBook;
import rs.ac.bg.fon.mmklab.book.CustomAudioFormat;
import rs.ac.bg.fon.mmklab.peer.domain.Configuration;

import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.Socket;

public class ReceiverInstance {
    /*deklaracija soketa i tokova koji sluze za uspostavljanje veze sa posiljaocem*/
    private final Socket socket;
    private final PrintStream toSender;
    private final BufferedReader fromSender;
    private final AudioBook audioBook;
    private final Configuration configuration;

    /*deklaracija soketa i tokova koji sluze za prijem strimovanog audio sadrzaja*/
//
    private final DatagramSocket datagramSocket;
    private final AudioFormat audioFormat;
    private final SourceDataLine sourceLine;

    //        kako bismo znali kad da stanemo sa primanjem paketa moramo da vodimo evidenciju o tome kolko smo frjmova procitali
    private long framesRead = 0;


    /*pocetak getter metoda*/

    public Socket getSocket() {
        return socket;
    }

    public PrintStream getToSender() {
        return toSender;
    }

    public BufferedReader getFromSender() {
        return fromSender;
    }

    public AudioBook getAudioBook() {
        return audioBook;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public SourceDataLine getSourceLine() {
        return sourceLine;
    }

    public long getFramesRead() {
        return framesRead;
    }

    /*kraj getter metoda*/

//  jedino cemo frejmove da menjamo tokom prijema pa to ne bi smelo da bude final
    public void setFramesRead(long framesRead) {
        this.framesRead = framesRead;
    }


    //    konstruktor
    private ReceiverInstance(AudioBook book, Configuration config) throws IOException, LineUnavailableException {
        this.audioBook = book;
        this.configuration = config;

        this.socket = new Socket(this.audioBook.getBookOwner().getIpAddress(), this.audioBook.getBookOwner().getPort());
        this.toSender = new PrintStream(this.socket.getOutputStream());
        this.fromSender = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

//        podesavanje audio formata
        this.audioFormat = CustomAudioFormat.toStandard(this.audioBook.getAudioDescription().getAudioFormat());
        this.sourceLine = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, this.audioFormat));


//        otvaranje datagram soketa za slanje paketa
        this.datagramSocket = new DatagramSocket(this.configuration.getLocalPortUDP());
    }

    public static ReceiverInstance createReceiverInstance(AudioBook book, Configuration configuration) throws IOException, LineUnavailableException {
        return new ReceiverInstance(book, configuration);
    }
}
