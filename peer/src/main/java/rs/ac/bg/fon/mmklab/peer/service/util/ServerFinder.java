package rs.ac.bg.fon.mmklab.peer.service.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerFinder {

    public static InetAddress getServerAddress() throws UnknownHostException {
        return InetAddress.getByName("localhost");
    }

    public static int getServerPort(){
        return 8000;
    }

}
