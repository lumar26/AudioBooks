package rs.ac.bg.fon.mmklab.peer.domain;

public class Configuration {
    private final String serverName;
    private final int serverPort;
    private final int localPortTCP;
    private final int localPortUDP;
    private final String audioExtension;
    private final String pathToBookFolder;

    public Configuration(String serverName, int serverPort, int localPortTCP, int localPortUDP, String audioExtension, String pathToBookFolder) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.localPortTCP = localPortTCP;
        this.localPortUDP = localPortUDP;
        this.audioExtension = audioExtension;
        this.pathToBookFolder = pathToBookFolder;
    }

    public String getServerName() {
        return serverName;
    }

    public int getLocalPortTCP() {
        return localPortTCP;
    }

    public int getLocalPortUDP() { return localPortUDP; }

    public int getServerPort() {
        return serverPort;
    }

    public String getAudioExtension() {
        return audioExtension;
    }

    public String getPathToBookFolder() {
        return pathToBookFolder;
    }

}
