package me.haga.librespot.spotifi.util;

import xyz.gianlu.librespot.core.Session;
import xyz.gianlu.librespot.player.Player;

public class PlayerWrapper {

    private Player player;
    private Session session;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
