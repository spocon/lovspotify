package me.haga.librespot.spotifi.model.config;

import java.io.Serializable;

public class MainConfig implements Serializable {

    private ServerPort server;
    private LogLevelConfig logging;

    public MainConfig() {

    }

    public LogLevelConfig getLogging() {
        return logging;
    }

    public void setLogging(LogLevelConfig logging) {
        this.logging = logging;
    }

    public ServerPort getServer() {
        return server;
    }

    public void setServer(ServerPort server) {
        this.server = server;
    }
}
