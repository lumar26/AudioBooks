package rs.ac.bg.fon.mmklab.peer.service.server_communication;

import rs.ac.bg.fon.mmklab.book.AudioBook;
import rs.ac.bg.fon.mmklab.peer.service.service_utils.JsonConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class ServerCommunicator {
    /*Ova klasa treba da sadrzi:

    * 3. metoda koja datu listu salje serveru na osnovu prosledjene adrese servera, u JSON formatu
    */

//  prvo cemo da odradimo metodu koja pokrece komunikaciju, otvaranje konekcije, itd...

    public static void initialConnectToServer(InetAddress serverAddress, int serverPort, List<AudioBook> listOfBooks){
        Socket communicationSocket;
        BufferedReader streamFromServer;
        PrintStream streamToServer;

//        proba mala da vidimo sta se salje
        System.out.println("(initialConnectToServer): Lista knjiga koja se salje serveru ima sledeci oblik: " + JsonConverter.bookListToJSON(listOfBooks));

        try {
//            inicijalizacija potrebnih tokova, preko tih tokova saljemo stringove
            communicationSocket = new Socket(serverAddress, serverPort); // u ovom trenutku je veza  sa serverom uspostavljena
            streamFromServer = new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));
            streamToServer = new PrintStream(communicationSocket.getOutputStream());

//          onda prvo sto bismo trebali da uradimo jeste da uspostavimo nekakvu vezu sa serverom, da se odradi neka
//          saljemo listu knjiga
            streamToServer.print(listOfBooks); // u ovom trenutku kad server procita iz toka


        } catch (IOException e) {
            System.err.println("Greska(initialConnectToServer): Nije moguce otvoriti socket ka serveru, proveri server ili adresu i port koji su prosledjeni");
//            e.printStackTrace();
        }

    }

}
