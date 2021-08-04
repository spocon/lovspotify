package me.haga.librespot.spotifi.controller;


import com.spotify.metadata.Metadata;
import me.haga.librespot.spotifi.model.CurrentSong;
import me.haga.librespot.spotifi.model.Image;
import me.haga.librespot.spotifi.model.NextTrack;
import me.haga.librespot.spotifi.model.PlayerLoadSong;
import me.haga.librespot.spotifi.util.PlayerWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.gianlu.librespot.audio.MetadataWrapper;
import xyz.gianlu.librespot.common.Utils;
import xyz.gianlu.librespot.dealer.ApiClient;
import xyz.gianlu.librespot.metadata.ImageId;
import xyz.gianlu.librespot.metadata.TrackId;
import xyz.gianlu.librespot.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("player")
public class PlayerController {

    private static final Logger log = LogManager.getLogger(PlayerController.class);

    private final PlayerWrapper playerWrapper;
    private static CurrentSong currentSong = new CurrentSong();
    private MetadataWrapper trackOrEpisode;

    public PlayerController(PlayerWrapper playerWrapper) {
        this.playerWrapper = playerWrapper;
    }


    @GetMapping(value = "current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrentPlayList(@RequestParam(value = "imgsize", defaultValue = "0", required = false) Integer imgsize) {

        return getPlayer().map((Player song) -> {

            try {
                MetadataWrapper toe = song.currentMetadata();
                if (!song.isActive()) return ResponseEntity.notFound().build();


                if (!toe.equals(trackOrEpisode)) {
                    currentSong = new CurrentSong();
                    currentSong.setImage(getImageFromCurrentSong(song, imgsize));
                    trackOrEpisode = toe;
                }
                if (toe.isEpisode()) {
                    currentSong.setEpisode(toe.episode);
                } else {
                    currentSong.setTrack(toe.track);
                    ApiClient api = playerWrapper.getSession().api();
                    List<NextTrack> nextTracks = new ArrayList<>();
                    song.tracks(true).next.stream().limit(2).forEach(track -> {
                        try {
                            Metadata.Track nextrack = api.getMetadata4Track(TrackId.fromUri(track.getUri()));
                            nextTracks.add(new NextTrack(nextrack, getImageNextSong(nextrack)));
                        } catch (Exception ignored) {
                        }
                    });
                    currentSong.setNextTracks(nextTracks);
                }
                return ResponseEntity.ok().body(currentSong);
            } catch (Exception e) {
                return ResponseEntity.notFound().build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "load", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loadSong(@RequestBody PlayerLoadSong loadSong) {
        return getPlayer().map(t -> {
            t.load(loadSong.getUri(), loadSong.getPlay(), false);
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
            try {
                t.previous();
            } catch (IllegalArgumentException ignored) {
            }
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("next")
    public ResponseEntity<?> next() {
        return getPlayer().map(t -> {
            try {
                t.next();
            } catch (IllegalArgumentException ignored) {
            }

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
        return ofNullable(playerWrapper.getPlayer());
    }


    private Image getImageFromCurrentSong(Player t, int size) {
        return ofNullable(t.currentMetadata())
                .map(y -> {
                    Metadata.Image image = y.getCoverImage().getImage(size);
                    return new Image(image.getHeight(), image.getWidth(), image.getSize().getNumber(), ImageId.fromHex(Utils.bytesToHex(image.getFileId())).hexId());
                })
                .orElse(new Image(0, 0, 0, ""));
    }

    private Image getImageNextSong(Metadata.Track track) {
        Metadata.Image image = track.getAlbum().getCoverGroup().getImage(Metadata.Image.Size.SMALL_VALUE);
        return new Image(image.getHeight(), image.getWidth(), image.getSize().getNumber(), ImageId.fromHex(Utils.bytesToHex(image.getFileId())).hexId());

    }
}