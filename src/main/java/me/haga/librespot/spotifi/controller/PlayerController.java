package me.haga.librespot.spotifi.controller;

import com.spotify.metadata.Metadata;
import me.haga.librespot.spotifi.model.CurrentSong;
import me.haga.librespot.spotifi.model.Image;
import me.haga.librespot.spotifi.model.PlayerLoadSong;
import me.haga.librespot.spotifi.util.SessionWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.gianlu.librespot.common.Utils;
import xyz.gianlu.librespot.mercury.model.EpisodeId;
import xyz.gianlu.librespot.mercury.model.ImageId;
import xyz.gianlu.librespot.mercury.model.PlayableId;
import xyz.gianlu.librespot.mercury.model.TrackId;
import xyz.gianlu.librespot.player.Player;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("player")
public class PlayerController {

    private static final Logger log = LogManager.getLogger(PlayerController.class);

    private final SessionWrapper sessionWrapper;
    private Player player;

    public PlayerController(SessionWrapper sessionWrapper) {
        this.sessionWrapper = sessionWrapper;
    }

    @GetMapping(value = "current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrentPlayList(@RequestParam(value = "imgsize", defaultValue = "0", required = false) Integer imgsize) {

        return getPlayer().map(song -> {
            PlayableId id = song.currentPlayableId();
            CurrentSong currentSong = new CurrentSong();
            currentSong.setImage(getImageFromCurrentSong(song, imgsize));
            currentSong.setTrackTime(song.time());
            if (id instanceof EpisodeId) currentSong.setEpisode(song.currentEpisode());
            if (id instanceof TrackId) currentSong.setTrack(song.currentTrack());
            return ResponseEntity.ok().body(currentSong);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "load", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loadSong(@RequestBody PlayerLoadSong loadSong) {
        return getPlayer().map(t -> {
            t.load(loadSong.getUri(), loadSong.getPlay());
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("pause")
    public ResponseEntity<?> pause() {
        return getPlayer().map(t -> {
            t.pause();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("resume")
    public ResponseEntity<?> resume() {
        return getPlayer().map(t -> {
            t.play();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("previous")
    public ResponseEntity<?> previous() {
        return getPlayer().map(t -> {
            t.previous();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("next")
    public ResponseEntity<?> next() {
        return getPlayer().map(t -> {
            t.next();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("volume/up")
    public ResponseEntity<?> volumeUp() {
        return getPlayer().map(t -> {
            t.volumeUp();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("volume/set/{volnr}")
    public ResponseEntity<?> volumeSet(@PathVariable("volnr") Integer volnr) {
        return getPlayer().map(t -> {
            t.setVolume(volnr);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("volume/down")
    public ResponseEntity<?> volumeDown() {
        return getPlayer().map(t -> {
            t.volumeDown();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    private Optional<Player> getPlayer() {
        if (this.sessionWrapper.get() != null) {
            this.player = sessionWrapper.get().player();
        }
        return ofNullable(this.player);
    }

    private Image getImageFromCurrentSong(Player t, int size) {
        Metadata.Track track = t.currentTrack();
        Metadata.Episode episode = t.currentEpisode();
        Metadata.Image image = null;
        if (track != null) image = track.getAlbum().getCoverGroupOrBuilder().getImage(size);
        if (episode != null) image = episode.getCoverImageOrBuilder().getImage(size);
        return ofNullable(image)
                .map(y -> new Image(y.getHeight(), y.getWidth(), y.getSize().getNumber(), ImageId.fromHex(Utils.bytesToHex(y.getFileId())).hexId()))
                .orElse(new Image(0, 0, 0, ""));
    }
}