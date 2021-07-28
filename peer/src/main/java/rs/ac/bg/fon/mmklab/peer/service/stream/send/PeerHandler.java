package rs.ac.bg.fon.mmklab.peer.service.stream.send;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import rs.ac.bg.fon.mmklab.book.AudioBook;
import rs.ac.bg.fon.mmklab.peer.domain.Configuration;
import rs.ac.bg.fon.mmklab.peer.service.util.BooksFinder;
import rs.ac.bg.fon.mmklab.util.JsonConverter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.*;

public class PeerHandler extends Service {

    private final Socket socket;
    private final PrintStream toReceiver;
    private final BufferedReader fromReceiver;
    private final Configuration configuration;

    private int remotePortUDP;

    private PeerHandler(Socket socket, PrintStream toReceiver, BufferedReader fromReceiver, Configuration configuration) {
        this.socket = socket;
        this.toReceiver = toReceiver;
        this.fromReceiver = fromReceiver;
        this.configuration = configuration;
    }

    public static PeerHandler peerHandlerFactory(Socket socket, Configuration configuration) throws IOException {
        return new PeerHandler(socket, new PrintStream(socket.getOutputStream()), new BufferedReader(new InputStreamReader(socket.getInputStream())), configuration);
    }
    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                AudioBook bookForStreaming = establishConnection();
                System.out.println("Uspesan prijem informacije o knjizi koju slusalac zeli: " + bookForStreaming.toString());

                startSending(bookForStreaming, configuration);
                return null;
            }
        };
    }

    private void startSending(AudioBook book, Configuration configuration) throws IOException,  UnsupportedAudioFileException {
//        InetAddress recieverAddress = InetAddress.getByName("localhost"); // IOException
        InetAddress recieverAddress = socket.getInetAddress(); // adresa peer-a koju iz
//        System.out.println("Adresa primaoca ---->" + receiverAddress.toString());
        DatagramSocket datagramSocket = new DatagramSocket(); // IOException
        File audioFile = BooksFinder.getBook(book, configuration); // ovde bi mogla da se odradi provera dal imamo tu knjigu kod nas
        System.out.println("(PeerHandler ----> startSending): audio fajl koji hoce peer: " + audioFile.toString());
        //preko klase AudioSystem se pristupa svim resursima u sistemu, tipa fajlovi, mikrofon itd... takodje mozemo da vidimo koji je format audia
//        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(audioFile); //trebala bi da se uradi provera da li je tip fajla podrzan


        AudioInputStream audioInputStream = null;
        try {
             audioInputStream = AudioSystem.getAudioInputStream(audioFile); // UnsupportedFileException  // IOException
        } catch (Exception e){
            e.printStackTrace();
        }
        // veličina frejma
        int frameSize = audioInputStream.getFormat().getFrameSize();
        System.out.println("Veličina jednog okvira u bajtovima -----> " + frameSize);
        //bafer u koji ucitavamo frejm po frejm
        byte[] sendBuffer = new byte[1024 * frameSize]; // ovde da se proveri da li je okej da bude 1024 bajta
        byte[] confirmationMessage = new byte[10];
        int bytesRead = 0, framesRead = 0;
        DatagramPacket sendPackage;
//        paket koji dobijamo od primaoca da bismo znali kad je završio sa obrađivanjem poslednjeg paketa koji smo mu poslali
        DatagramPacket confirmationPacket = new DatagramPacket(confirmationMessage, confirmationMessage.length);



        System.out.println("Krecemo sa slanjem");
        System.out.println("Adresa primaoca: " + recieverAddress.toString() + "; port primaoca: " + remotePortUDP);

        while((bytesRead = audioInputStream.read(sendBuffer)) != -1){ // kada se dodje do kraja toka vraca -1  // IOException
//            framesRead += bytesRead / frameSize;
            sendPackage = new DatagramPacket(sendBuffer, sendBuffer.length, recieverAddress, remotePortUDP);
            datagramSocket.send(sendPackage);  // IOException

//            ovaj potvrdni paket nam omogućava da blokiramo slanje sve dok primalac ne primi paket, i dok ga ne prosledi na mikser
            datagramSocket.receive(confirmationPacket); // IOException
        }
        // obavestenje da je dosao knjizi kraj
        datagramSocket.close();
        System.out.println("Ceo fajl je procitan, i soket zatvoren");
    }

    public AudioBook establishConnection() {
        try {
            String req = fromReceiver.readLine();
            if (req.equals("Available for streaming?")) {
                toReceiver.println("Yes, which book?");
                String jsonBookInfo = fromReceiver.readLine();
                remotePortUDP = Integer.parseInt(fromReceiver.readLine().trim());
                System.out.println("--------- Pre konvertovanja u AudioBook: " + jsonBookInfo);
                AudioBook bookForStreaming = JsonConverter.toOriginal(jsonBookInfo, AudioBook.class);
//                AudioBook bookForStreaming = JsonConverter.jsonToAudioBook(jsonBookInfo);
                System.out.println("--------- Nakon konvertovanja u AudioBook: " + bookForStreaming.toString());

                return bookForStreaming;
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Greska (PeerHandler -> establishConnection): problemi sa ulaznim tokom, nisu primljene ocekivane poruke");
        }
        return null;
    }

}
