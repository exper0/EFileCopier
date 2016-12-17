package org.springframework.integration.samples.ftp;

/**
 * Created by Acer-V3 on 17.12.2016.
 */
public class RemoteServerSettings {
    public enum Protocol {
        FTP,
        SFTP
    }
    private Protocol protocol;
    private String user;
    private String password;
    private String host;
    private int port;

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
