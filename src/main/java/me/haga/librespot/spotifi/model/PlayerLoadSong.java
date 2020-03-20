package me.haga.librespot.spotifi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerLoadSong {

    @JsonProperty(required = true)
    private String uri;
    @JsonProperty(required = true)
    private Boolean play;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Boolean getPlay() {
        return play;
    }

    public void setPlay(Boolean play) {
        this.play = play;
    }
}
