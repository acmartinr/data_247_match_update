package models;

import com.typesafe.config.Config;

public class EmailConfig {

    private String hostname;
    private int port;
    private String username;
    private String password;
    private String from;
    private String fromName;
    private String reply;
    private String contentType = "text/html";
    private boolean sslOnConnect;
    private String API_KEY;

    public EmailConfig() {}

    public EmailConfig(Config config) {
        setFrom(config.getString("mail.smtp.from"));
        setAPI_KEY(config.getString("mail.api.key"));
    }

    public EmailConfig(Config config, String from, String fromName, String reply) {
        if (from != null && fromName != null) {
            setFrom(from);
            setFromName(fromName);

            if (reply != null) {
                setReply(reply);
            }
        } else {
            setFrom(config.getString("mail.smtp.from"));
        }

        setAPI_KEY(config.getString("mail.api.key"));
    }

    public static EmailConfig forMakeDataList(Config config) {
        EmailConfig result = new EmailConfig(config);
        //result.setUsername("postmaster@makedatalist.com");
        //result.setPassword("73b4bd9b0c4fe2cd4c706026105613a3-a3d67641-e6c2ab18");
        //result.setFrom("admin@makedatalist.com");

        return result;
    }

    public static EmailConfig forMakeTheData(Config config) {
        EmailConfig result = new EmailConfig(config);
        //result.setUsername("postmaster@makethedata.com");
        //result.setPassword("");
        //result.setFrom("admin@makethedata.com");

        return result;
    }

    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public boolean isSslOnConnect() { return sslOnConnect; }
    public void setSslOnConnect(boolean sslOnConnect) { this.sslOnConnect = sslOnConnect; }

    public String getAPI_KEY() { return API_KEY; }
    public void setAPI_KEY(String API_KEY) { this.API_KEY = API_KEY; }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
