package com.github.exper0.efilecopier.ftp;

/**
 * Created by Acer-V3 on 17.12.2016.
 */
public abstract class ReportSettings {
//    public enum Protocol {
//        FTP,
//        SFTP
//    }

    private String reportName;
//    private Protocol protocol;
    private String user;
    private String password;
    private String host;
    private int port;
    private String remoteDir;
    private String localDir;

//    public Protocol getProtocol() {
//        return protocol;
//    }
//
//    public void setProtocol(Protocol protocol) {
//        this.protocol = protocol;
//    }

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

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getRemoteDir() {
        return remoteDir;
    }

    public void setRemoteDir(String remoteDir) {
        this.remoteDir = remoteDir;
    }

    public String getLocalDir() {
        return localDir;
    }

    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }
}
