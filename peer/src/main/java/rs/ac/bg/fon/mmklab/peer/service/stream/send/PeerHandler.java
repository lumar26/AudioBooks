package rs.ac.bg.fon.mmklab.peer.service.stream.send;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import rs.ac.bg.fon.mmklab.book.AudioBook;
import rs.ac.bg.fon.mmklab.peer.domain.Configuration;
import rs.ac.bg.fon.mmklab.peer.service.util.BooksFinder;
import rs.ac.bg.fon.mmklab.util.JsonConverter;

import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.net.*;

public class PeerHandler extends Service {

    //    instance je objekat koji sadrzi reference svih tokova, soketa, audio fajla, adrese primaoca, udp port primaoca i evideciju o broju procitanih frejmova
    private final PeerHandlerInstance instance;

    private PeerHandler(PeerHandlerInstance instance) {
        this.instance = instance;
    }

    public static PeerHandler createInstance(Socket socket, Configuration configuration) throws IOException {
        return new PeerHandler(PeerHandlerInstance.createHandlerInstance(socket, configuration));
    }


    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
//                prilikom uspostavljanja konekcije nam primalac salje koju tacno knjigu hoce da slusa
                AudioBook bookForStreaming = establishConnection();
//                System.out.println("Uspesan prijem informacije o knjizi koju slusalac zeli: " + bookForStreaming.toString());

                // pokretanje nove niti koja osluskuje signale od primaoca, koristi iste sokete kao i metoda establish connection
                new SignalHandler(instance).start();
                send(bookForStreaming);
                System.out.println("Zavrseno slanje audio zapisa");
                return null;
            }
        };
    }

    public AudioBook establishConnection() {

        System.out.println("establishConnection, nit: " + Thread.currentThread());


        try {
            String req = instance.getFromReceiver().readLine();
            if (req.equals("Available for streaming?")) {
                instance.getToReceiver().println("Yes, which book?");
                String jsonBookInfo = instance.getFromReceiver().readLine();
                instance.setRemotePortUDP(Integer.parseInt(instance.getFromReceiver().readLine().trim()));
                AudioBook bookForStreaming = JsonConverter.toOriginal(jsonBookInfo, AudioBook.class);

                return bookForStreaming;
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Greska (PeerHandler -> establishConnection): problemi sa ulaznim tokom, nisu primljene ocekivane poruke");
        }
        return null;
    }

    private void send(AudioBook book) throws IOException {

        /*  postavljamo audio fajl iz koga cemo da ucitavamo deo po deo i da saljemo na mrezu */
        instance.setAudioFile(BooksFinder.getBook(book, instance.getConfiguration())); // ovde bi mogla da se odradi provera dal imamo tu knjigu kod nas

        //preko klase AudioSystem se pristupa svim resursima u sistemu, tipa fajlovi, mikrofon itd... takodje mozemo da vidimo koji je format audia
        try {
            instance.setAudioInputStream(AudioSystem.getAudioInputStream(instance.getAudioFile()));// UnsupportedFileException  // IOException
        } catch (Exception e) {
            e.printStackTrace();
        }
        // veličina frejma
        int frameSize = instance.getAudioInputStream().getFormat().getFrameSize();

        //definisanje bafera koje saljemo na mrezu
        byte[] sendBuffer = new byte[1024 * frameSize]; // ovde da se proveri da li je okej da bude 1024 bajta
        byte[] confirmationMessage = new byte[10];
//        paket koji dobijamo od primaoca da bismo znali kad je završio sa obrađivanjem poslednjeg paketa koji smo mu poslali

        /* paketi koje saljemo na mrezu*/
        DatagramPacket dataPackage;
        DatagramPacket confirmationPacket = new DatagramPacket(confirmationMessage, confirmationMessage.length);

        System.out.println("Krecemo sa slanjem");
        System.out.println("Adresa primaoca: " + instance.getReceiverAddress().toString() + "; port primaoca: " + instance.getRemotePortUDP());

        while ((instance.getAudioInputStream().read(sendBuffer)) != -1) { // kada se dodje do kraja toka vraca -1  // IOException
//            kreiranje paketa koji saljemo na mrezu
            dataPackage = new DatagramPacket(sendBuffer, sendBuffer.length, instance.getReceiverAddress(), instance.getRemotePortUDP());

            /* trenutak slanja paketa */
            instance.getDatagramSocket().send(dataPackage);  // IOException
            instance.setFramesSent(sendBuffer.length / frameSize); // azuriramo broj poslatih frejmova

//            ovaj potvrdni paket nam omogućava da blokiramo slanje sve dok primalac ne primi paket, i dok ga ne prosledi na mikser
            instance.getDatagramSocket().receive(confirmationPacket); // IOException
        }

        // zatvaranje svih soketa i tokova
        closeUDPConnection(instance.getDatagramSocket());
        try {
            closeTCPConnection(instance.getSocket(), instance.getToReceiver(), instance.getFromReceiver());
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("(PeerHandler ---> startSending):  greska pri zatvaranju soketa nakon slanja knjige");
        }

        System.out.println("Ceo fajl je procitan, i soket zatvoren");
    }

    private void closeUDPConnection(DatagramSocket ds){
        ds.close();
    }

    private void closeTCPConnection(Socket s, PrintStream ps, BufferedReader br) throws IOException {
        s.close();
        ps.close();
        br.close();
    }



}
