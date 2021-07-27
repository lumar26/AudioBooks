package rs.ac.bg.fon.mmklab.peer.service.config_service;

import rs.ac.bg.fon.mmklab.peer.domain.Configuration;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConfigurationService {
    public static Configuration getConfiguration(String localPortTCP, String localPortUDP, String pathToFolder) {
        List<String> predefinedConfiguration = getPredefinedConfiguration(new File("lib/src/main/resources/config/confiruration.txt"));
        String serverName = null;
        String serverPortTxt = null;
        String audioExtension = null;
        for (int i = 0; i < predefinedConfiguration.size(); i++) {
            String[] temp = predefinedConfiguration.get(i).split(":");
            switch (temp[0]) {
                case "server_address":
                    serverName = temp[1];
                    break;
                case "server_port":
                    serverPortTxt = temp[1];
                    break;
                case "audio_extension":
                    audioExtension = temp[1];
                    break;
                default:
                    break;
            }
        }

        // kod portova ako se desi greska vraca -1
        int serverPort = getValidPort(serverPortTxt); // port na kome osluskuje server
        int localPortTcp = getValidPort(localPortTCP);// port na kome osluskuje nasa sender nit
        int localPortUdp = getValidPort(localPortUDP); // port na kome primamo audio tok

        // kod adrese ako se desi greska vraca null
        InetAddress serverAddress = getValidInetAddress(serverName);
        String extension = getValidAudioExtension(audioExtension);
        String path = pathToFolder.trim(); // nikakva validacija za sad
        return new Configuration(serverAddress.getCanonicalHostName(), serverPort, localPortTcp, localPortUdp, extension, path);


    }

    private static String getValidAudioExtension(String audioExtension) {
        audioExtension = audioExtension.trim().toLowerCase(Locale.ROOT);
        if (audioExtension == null){
            System.err.println("Greska (ConfigurationService -> getValidAudioExtension): audio ekstenzija nije uspesno procitana iz konfiguracionog fajla");
            return null;
        }
        if(audioExtension.length() != 4 || !audioExtension.startsWith(".")) {
            System.err.println("Greska (ConfigurationService -> getValidAudioExtension): audio ekstenzija nije u odgovarajucem formatu");
            return null;
        }

        return audioExtension;
    }

    private static InetAddress getValidInetAddress(String serverName) {
        InetAddress res = null;
        try {
            res = InetAddress.getByName(serverName);
        } catch (UnknownHostException e) {
//            e.printStackTrace();
            System.err.println("Greska (ConfigurationService -> getValidInetAddress): adresa servera navedena u konfiduracionom fajlu nije validna");
        }
        return res;
    }

    public static int getValidPort(String portTxt) {
        int result = -1;
        if (portTxt == null) {
            System.err.println("Greska (ConfigurationService -> getServerPort): port na kom osluskuje server nije dobro ucitan iz fajla, ili udp port nije dobro ucitan iz gui-ja");
            return result;
        }
        try {
            result = Integer.parseInt(portTxt.trim());
        } catch (NumberFormatException e) {
//            e.printStackTrace();
            System.err.println("Greska (ConfigurationService -> getServerPort): u fajlu nije unet broj tipa int, ili korisnik nije uneo udp port tipa int");
            return result;
        }
        if (result > 1023)
            return result;
        System.err.println("Greska (ConfigurationService -> getServerPort): Broj porta u fajlu, ili onog koji je korisnik uneo nije veci od 1024 pa postoji opasnost da bude zauzet");
        return -1;

    }

    private static List<String> getPredefinedConfiguration(File configFile) {
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            System.err.println("Greska (COnfigurationService -> getPredefinedConfiguration): problem sa citanjem iz konfiguracionog fajla;\n " + e.getMessage());
        }
        return result;
    }
}
