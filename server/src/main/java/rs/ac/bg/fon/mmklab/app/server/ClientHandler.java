package rs.ac.bg.fon.mmklab.app.server;

import util.AudioBook;

import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    Socket communicationSocket;
    private InetAddress clientAddress;
    private List<AudioBook> booksAvailable;

    public ClientHandler(Socket communicationSocket) {
        this.communicationSocket = communicationSocket;
        clientAddress = communicationSocket.getInetAddress();
    }



    @Override
    public void run() {

        /*ovde treba da ocekujemo od klijenta da nam posalje listu knjiga koje on moze da ponudi, to bismo mogli iz nekog JSON-a da izvucemo te podatke*/
        /*nakon toga ide azuriranje liste u klasi rs.ac.bg.fon.mmklab.app.server*/
        /*slanje korisniku liste dostupnih knjiga, isto neki json format |||||  samo na osnovu zahteva!!!*/
        /*za slucaj kad korisnik javi da je zauzet da se sklone iz liste dostupnih knjiga sve one koje taj korisnik nudi*/
        super.run();
    }
}
