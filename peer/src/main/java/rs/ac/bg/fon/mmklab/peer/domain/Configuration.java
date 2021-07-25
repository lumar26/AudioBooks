package rs.ac.bg.fon.mmklab.peer.domain;

public class Configuration {
    private String serverName;
    private int serverPort;
    private int localPort;
    private String audioExtension;
    private String pathToBookFolder;

    public Configuration(String serverName, int serverPort, int localPort, String audioExtension, String pathToBookFolder) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.localPort = localPort;
        this.audioExtension = audioExtension;
        this.pathToBookFolder = pathToBookFolder;
    }

    public String getServerName() {
        return serverName;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getAudioExtension() {
        return audioExtension;
    }

    public void setAudioExtension(String audioExtension) {
        this.audioExtension = audioExtension;
    }

    public String getPathToBookFolder() {
        return pathToBookFolder;
    }

    public void setPathToBookFolder(String pathToBookFolder) {
        this.pathToBookFolder = pathToBookFolder;
    }

    public boolean isComplete(){
        return this.serverName != null && this.audioExtension.startsWith(".") && this.serverPort != 0 && this.pathToBookFolder != null
                && !this.serverName.equals("") && !this.pathToBookFolder.equals("") && !this.audioExtension.equals("");
    }
}
