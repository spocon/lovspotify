package me.haga.librespot.spotifi.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spotify.metadata.Metadata;
import me.haga.librespot.spotifi.util.ProtobufSerializer;

import java.util.List;

public class CurrentSong {


    private String current;
    private Long trackTime;
    @JsonSerialize(using = ProtobufSerializer.class)
    private Metadata.Episode episode;
    @JsonSerialize(using = ProtobufSerializer.class)
    private Metadata.Track track;
    private List<NextTrack> nextTracks;
    private Image image;


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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<NextTrack> getNextTracks() {
        return nextTracks;
    }

    public void setNextTracks(List<NextTrack> nextTracks) {
        this.nextTracks = nextTracks;
    }
}
