package me.haga.librespot.spotifi.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spotify.metadata.Metadata;
import me.haga.librespot.spotifi.util.ProtobufSerializer;

public class CurrentSong {


    private String current;
    private Long trackTime;
    @JsonSerialize(using = ProtobufSerializer.class)
    private Metadata.Episode episode;
    @JsonSerialize(using = ProtobufSerializer.class)
    private Metadata.Track track;




    public CurrentSong(Metadata.Track track) {
        this.track = track;
    }

    public CurrentSong() {

    }

    public Metadata.Track getTrack() {
        return track;
    }

    public void setTrack(Metadata.Track track) {
        this.track = track;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public Long getTrackTime() {
        return trackTime;
    }

    public void setTrackTime(Long trackTime) {
        this.trackTime = trackTime;
    }

    public void setEpisode(Metadata.Episode episode) {
        this.episode = episode;
    }

    public Metadata.Episode getEpisode() {
        return episode;
    }
}
