package rs.ac.bg.fon.mmklab.util;

import java.net.InetAddress;

public class BookOwner {
    /*Par nekih opstih informacija mozemo da ubacimo, cisto radi prikazivanja*/
    private String name;
    /*Vlasnik knjige od informacija obavezno treba da ima IP adresu*/
    private InetAddress ipAddress;
    /*mozda nam bude zatrebao status da li je seeder online ili ne*/
    private boolean isOnline;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "BookOwner: {" +
                "name='" + name + '\'' +
                ", ipAddress=" + ipAddress +
                ", isOnline=" + isOnline +
                '}';
    }
}
