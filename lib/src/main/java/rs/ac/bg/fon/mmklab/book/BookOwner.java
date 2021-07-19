package rs.ac.bg.fon.mmklab.book;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

@JsonSerialize
public class BookOwner implements Serializable {
    /*Par nekih opstih informacija mozemo da ubacimo, cisto radi prikazivanja*/
//    private String name;
    /*Vlasnik knjige od informacija obavezno treba da ima IP adresu i port na kome osluskuje zahteve*/
    private InetAddress ipAddress;
    private int port;
    /*mozda nam bude zatrebao status da li je seeder online ili ne*/
    private boolean isOnline;

    public BookOwner() {
    }

    public BookOwner(InetAddress ipAddress, int port,  boolean isOnline) {
        this.ipAddress = ipAddress;
        this.isOnline = isOnline;
        this.port = port;
    }


    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    @Override
    public String toString() {
        return "\nBookOwner: {" +
                ", ipAddress=" + ipAddress +
                ", isOnline=" + isOnline +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookOwner)) return false;
        BookOwner bookOwner = (BookOwner) o;
        return getPort() == bookOwner.getPort() && isOnline() == bookOwner.isOnline() && getIpAddress().equals(bookOwner.getIpAddress());
    }
}
