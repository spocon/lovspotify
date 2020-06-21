package me.haga.librespot.spotifi.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spotify.metadata.Metadata;
import me.haga.librespot.spotifi.util.ProtobufSerializer;

public class NextTrack {

    @JsonSerialize(using = ProtobufSerializer.class)
    private Metadata.Track track;
    private Image image;

    public NextTrack(Metadata.Track track, Image imageNextSong) {
        this.track = track;
        this.image = imageNextSong;
    }

    public void setTrack(Metadata.Track track) {
        this.track = track;
    }

    public Metadata.Track getTrack() {
        return track;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
