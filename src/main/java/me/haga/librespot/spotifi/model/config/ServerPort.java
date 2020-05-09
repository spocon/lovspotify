package me.haga.librespot.spotifi.model.config;

import java.io.Serializable;

public class ServerPort implements Serializable {

    private Integer port;

    public ServerPort() {

    }

    public ServerPort(Integer port) {
        this.port = port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getPort() {
        return port;
    }
}
