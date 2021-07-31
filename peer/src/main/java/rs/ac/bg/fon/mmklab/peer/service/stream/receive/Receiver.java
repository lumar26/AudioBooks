package rs.ac.bg.fon.mmklab.peer.service.stream.receive;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import rs.ac.bg.fon.mmklab.book.AudioBook;
import rs.ac.bg.fon.mmklab.book.CustomAudioFormat;
import rs.ac.bg.fon.mmklab.peer.domain.Configuration;
import rs.ac.bg.fon.mmklab.util.JsonConverter;

import javax.sound.sampled.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public class Receiver extends Service<AudioBook> {
//    nit slusaoca krece sa radom onog trenutka kad korisnik odabere knjigu koju hoce da slusa

    //    instance je objekat koji sadrzi reference svih tokova, soketa, audio fajla, adrese primaoca, udp port primaoca i evideciju o broju procitanih frejmova
    private final ReceiverInstance instance;

    private Receiver(ReceiverInstance receiverInstance) {
        this.instance = receiverInstance;
    }

    public static Receiver createInstance(AudioBook book, Configuration configuration) throws IOException, LineUnavailableException {
        return new Receiver(ReceiverInstance.createReceiverInstance(book, configuration));
    }


    public ReceiverInstance getInstance() {
        return instance;
    }

    @Override
    protected Task<AudioBook> createTask() {
        return new Task<>() {
            @Override
            protected AudioBook call() throws Exception {
                establishConnection();
                receive();
                return null;
            }
        };
    }

    private void establishConnection() {
        instance.getToSender().println("Available for streaming?");
        try {
            String res = instance.getFromSender().readLine();
            if (res.equals("Yes, which book?")) {
                System.out.println("Receiver: Sender confirmed");
                instance.getToSender().println(JsonConverter.toJSON(instance.getAudioBook()));
                instance.getToSender().println(instance.getConfiguration().getLocalPortUDP());
                System.out.println("Poslata knjiga posiljaocu: " + JsonConverter.toJSON(instance.getAudioBook()));
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Greska (Receiver -> establishConnection): sender nije poslao poruku na nas odgovor");

        }
    }

    private void receive() throws IOException, LineUnavailableException{

//        datagram soket, sourceLine i audio format su postavljeni pri kreiranju instance Receiver-a, na osnovu parametara audioBook i configuration koji se prosledjuju prilikom kreiranja listener-a za dugme knjige

        instance.getSourceLine().open(instance.getAudioFormat()); // ne znam dal bi ovde trebalo odmah da se otvara ovo
        instance.getSourceLine().start();
////        -----------------------------------------------------------------------------------------------------------------------------------------------

        int framesize = instance.getAudioBook().getAudioDescription().getFrameSizeInBytes();
//        System.out.println("Veličina jednog okvira u bajtovima -----> " + framesize);
        byte[] receiveBuffer = new byte[1024 * framesize]; //i ovde bi trebalo da nam se posalje koja je velicina
        byte[] confirmationBuffer = "OK".getBytes();
        System.out.println("Krece prijem datagram paketa sa mreze");


        /* definisanje paketa koje saljemo i primamo*/
        DatagramPacket receivePacket;
        DatagramPacket signalPacket;


        /* ova petlja vrti dok god ne dodjemo do kraja knjige, a koliko je duga knjiga znamo na osnovu podataka koje smo pokupili od servera metodom getAvailableBooks*/
        while (instance.getFramesRead() < instance.getAudioBook().getAudioDescription().getLengthInFrames()) {
            receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            instance.setFramesRead(instance.getFramesRead() + receiveBuffer.length / framesize);

            try {
                instance.getDatagramSocket().receive(receivePacket);
            } catch (IOException e) {
                System.err.println("Problem na mreži, paket nije moguće primiti");
                e.printStackTrace();
            }

            try {
                instance.getSourceLine().write(receiveBuffer, 0, receiveBuffer.length);
            } catch (Exception e) {
//                e.printStackTrace();
                System.err.println("Nije moguce upisati nista na liniju");
            }
            signalPacket = new DatagramPacket(
                    confirmationBuffer, confirmationBuffer.length, receivePacket.getAddress(), receivePacket.getPort());
            instance.getDatagramSocket().send(signalPacket); //paket potvrde omogućava da pošiljalac ne šalje pakete odmah, već da sačeka da se ceo bafer isprazni i ode ka mikseru
        }
        closeUDPConnection(instance.getDatagramSocket());
        try {
            closeTCPConnection(instance.getSocket(), instance.getToSender(), instance.getFromSender());
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("(Receiver -> receive): greska pri zatvaranju tcp soketa i tokova kao posiljaocu");
        }
        instance.getSourceLine().close();
        System.out.println("Kraj prenosa, soketi i tokovi zatvoreni na strani klijenta");
    }

    private void closeUDPConnection(DatagramSocket ds){
        ds.close();
    }

    private void closeTCPConnection(Socket s, PrintStream out, BufferedReader in) throws IOException {
        s.close();
        in.close();
        out.close();
    }


}
