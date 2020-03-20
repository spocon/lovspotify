package me.haga.librespot.spotifi.controller;

import me.haga.librespot.spotifi.model.CurrentSong;
import me.haga.librespot.spotifi.model.PlayerLoadSong;
import me.haga.librespot.spotifi.util.SessionWrapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("load")
    public ResponseEntity<?> loadSong(@RequestBody PlayerLoadSong loadSong) {
        return getPlayer().map(t -> {
            t.load(loadSong.getUri(),loadSong.getPlay());
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("pause")
    public ResponseEntity<?> pause() {
        return getPlayer().map(t -> {
            t.pause();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("resume")
    public ResponseEntity<?> resume() {
        return getPlayer().map(t -> {
            t.play();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("previous")
    public ResponseEntity<?> previous() {
        return getPlayer().map(t -> {
            t.previous();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("next")
    public ResponseEntity<?> next() {
        return getPlayer().map(t -> {
            t.next();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("volume/up")
    public ResponseEntity<?> volumeUp() {
        return getPlayer().map(t -> {
            t.volumeUp();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("volume/set/{volnr}")
    public ResponseEntity<?> volumeSet(@PathVariable("volnr") Integer volnr) {
        return getPlayer().map(t -> {
            t.setVolume(volnr);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("volume/down")
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
