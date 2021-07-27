package rs.ac.bg.fon.mmklab.peer.service.stream.receive;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import rs.ac.bg.fon.mmklab.book.AudioBook;
import rs.ac.bg.fon.mmklab.book.BookInfo;
import rs.ac.bg.fon.mmklab.book.BookOwner;
import rs.ac.bg.fon.mmklab.book.CustomAudioFormat;
import rs.ac.bg.fon.mmklab.peer.domain.Configuration;
import rs.ac.bg.fon.mmklab.util.JsonConverter;

import javax.sound.sampled.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class Receiver extends Service<AudioBook> {
//    nit slusaoca krece sa radom onog trenutka kad korisnik odabere knjigu koju hoce da slusa

    /*deklaracija soketa i tokova koji sluze za uspostavljanje veze sa posiljaocem*/
    private Socket socket;
    private PrintStream toSender;
    private BufferedReader fromSender;
    private AudioBook audioBook;
    private Configuration configuration;

    /*deklaracija soketa i tokova koji sluze za prijem strimovanog audio sadrzaja*/
// ...

    public Receiver(AudioBook audioBook, Configuration configuration) throws IOException {
       this.audioBook = audioBook;
       this.configuration = configuration;
    }

    @Override
    protected Task<AudioBook> createTask() {
        return new Task<>() {
            @Override
            protected AudioBook call() throws Exception {
                establishConnection();
                startReceiving();
                return null;
            }
        };
    }

    private void startReceiving() throws IOException, LineUnavailableException, UnsupportedAudioFileException {

        DatagramSocket datagramSocket = new DatagramSocket(configuration.getLocalPortUDP());// i ovo mora da se menja // socket exception

//        -----------------------------------------------------------------------------------------------------------------------------------------------
        AudioFormat audioFormat = CustomAudioFormat.toStandard(audioBook.getAudioDescription().getAudioFormat());
        SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, audioFormat)); // LineUnavailableException
        sourceLine.open(audioFormat); // ne znam dal bi ovde trebalo odmah da se otvara ovo
        sourceLine.start();
//        -----------------------------------------------------------------------------------------------------------------------------------------------

        int framesize = audioBook.getAudioDescription().getFrameSizeInBytes();
        System.out.println("Veličina jednog okvira u bajtovima -----> " + framesize);
        byte[] recieveBuffer = new byte[1024 * framesize]; //i ovde bi trebalo da nam se posalje koja je velicina
        byte[] confirmationBuffer = "OK".getBytes();
        System.out.println("Krecemo da primamo pakete sa mreze, nasa adresa: " + datagramSocket.getLocalAddress().toString());

        while (true) {//ovde bi bilo dobro da posiljalac posalje prvo kolko je veliki fajl, da bismo znali dokle vrtimo petlju, ili tako nesto
            DatagramPacket receivePacket = new DatagramPacket(recieveBuffer, recieveBuffer.length);

            try {
                datagramSocket.receive(receivePacket);
//                System.out.print(".");
            } catch (IOException e) {
                System.err.println("Problem na mreži, paket nije moguće primiti");
                e.printStackTrace();
            }

            sourceLine.write(recieveBuffer, 0, recieveBuffer.length);
            DatagramPacket confirmationPacket = new DatagramPacket(
                    confirmationBuffer, confirmationBuffer.length, receivePacket.getAddress(), receivePacket.getPort());
            datagramSocket.send(confirmationPacket); //paket potvrde omogućava da pošiljalac ne šalje pakete odmah, već da sačeka da se ceo bafer isprazni i ode ka mikseru
        }
//        System.out.println("-------------------------------Prekid rada-------------------------------");
    }

    private void establishConnection() {
        try {
            this.socket = new Socket(audioBook.getBookOwner().getIpAddress(), audioBook.getBookOwner().getPort()); // u ovom trenutku je uspostavljena veza
            toSender = new PrintStream(socket.getOutputStream());
            fromSender = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("(establisConnection): Odradjena inicijalizacija tokova");
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Greska (Receiver -> establishConnection): nije doslo do otvaranja soketa ka posiljaocu");
        }

        toSender.println("Available for streaming?");
        System.out.println("1.---------- Poslali smo poruku posiljaocu");
        try {
            String res = fromSender.readLine();
            if (res.equals("Yes, which book?")){
                System.out.println("Receiver: Sender confirmed");
                toSender.println(JsonConverter.toJSON(audioBook));
                toSender.println(configuration.getLocalPortUDP());
                System.out.println("Poslata knjiga posiljaocu: " + JsonConverter.toJSON(audioBook));
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Greska (Receiver -> establishConnection): sender nije poslao poruku na nas odgovor");

        }
    }



}
