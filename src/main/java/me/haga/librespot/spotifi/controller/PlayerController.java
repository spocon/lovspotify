package me.haga.librespot.spotifi.controller;

import com.google.gson.JsonObject;
import com.spotify.metadata.Metadata;
import me.haga.librespot.spotifi.model.CurrentSong;
import me.haga.librespot.spotifi.util.SessionWrapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.gianlu.librespot.common.ProtobufToJson;
import xyz.gianlu.librespot.mercury.model.EpisodeId;
import xyz.gianlu.librespot.mercury.model.PlayableId;
import xyz.gianlu.librespot.mercury.model.TrackId;
import xyz.gianlu.librespot.player.Player;

import java.util.Optional;

@RestController
@RequestMapping("player")
public class PlayerController {

    private final SessionWrapper sessionWrapper;
    private Player player;

    public PlayerController(SessionWrapper sessionWrapper) {
        this.sessionWrapper = sessionWrapper;
    }

    @GetMapping(value = "current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrentPlayList() {

        return getPlayer().map(t -> {
            PlayableId id = t.currentPlayableId();
            CurrentSong currentSong = new CurrentSong();
            currentSong.setTrackTime(t.time());
            if (id instanceof EpisodeId)
                currentSong.setEpisode(t.currentEpisode());
            if (id instanceof TrackId)
                currentSong.setTrack(t.currentTrack());
            currentSong.setCurrent(id.toSpotifyUri());
            return ResponseEntity.ok().body(currentSong);
        }).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("volumeup")
    public ResponseEntity<?> volumeUp() {
        return getPlayer().map(t -> {
            t.volumeUp();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("volumedown")
    public ResponseEntity<?> volumeDown() {
        return getPlayer().map(t -> {
            t.volumeDown();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.noContent().build());
    }

    private Optional<Player> getPlayer() {
        if (this.sessionWrapper.get() != null) {
            this.player = sessionWrapper.get().player();
        }
        return Optional.ofNullable(this.player);
    }
}
