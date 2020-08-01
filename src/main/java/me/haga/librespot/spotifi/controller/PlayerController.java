package me.haga.librespot.spotifi.controller;


import com.spotify.metadata.Metadata;
import me.haga.librespot.spotifi.SpotifiApplication;
import me.haga.librespot.spotifi.model.CurrentSong;
import me.haga.librespot.spotifi.model.Image;
import me.haga.librespot.spotifi.model.NextTrack;
import me.haga.librespot.spotifi.model.PlayerLoadSong;
import me.haga.librespot.spotifi.util.SessionWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.gianlu.librespot.common.Utils;
import xyz.gianlu.librespot.dealer.ApiClient;
import xyz.gianlu.librespot.metadata.ImageId;
import xyz.gianlu.librespot.metadata.PlayableId;
import xyz.gianlu.librespot.metadata.TrackId;
import xyz.gianlu.librespot.player.Player;
import xyz.gianlu.librespot.player.TrackOrEpisode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("player")
public class PlayerController {

    private static final Logger log = LogManager.getLogger(PlayerController.class);

    private final SessionWrapper sessionWrapper;
    private static Player player = null;
    private static CurrentSong currentSong = new CurrentSong();
    private TrackOrEpisode trackOrEpisode;


    public PlayerController(SessionWrapper sessionWrapper) {
        this.sessionWrapper = sessionWrapper;
    }

    @GetMapping(value = "current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrentPlayList(@RequestParam(value = "imgsize", defaultValue = "0", required = false) Integer imgsize) {

        return getPlayer().map((Player song) -> {

            try {
                TrackOrEpisode toe = song.currentMetadata();
                if (toe == null) return ResponseEntity.notFound().build();
                currentSong.setTrackTime(song.time());
                if (!toe.equals(trackOrEpisode)) {
                    currentSong = new CurrentSong();
                    currentSong.setImage(getImageFromCurrentSong(song, imgsize));
                    trackOrEpisode = toe;
                    if (toe.isEpisode()) {
                        currentSong.setEpisode(toe.episode);
                    } else {
                        currentSong.setTrack(toe.track);
                        ApiClient api = sessionWrapper.getSession().api();
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
        if (this.sessionWrapper.getSession() != null && player == null) {
            player = new Player(SpotifiApplication.getLibrespotConf().toPlayer(), sessionWrapper.getSession());
            player.addEventsListener(getListener());
        }
        return ofNullable(player);
    }


    private Player.EventsListener getListener() {
        return new Player.EventsListener() {
            @Override
            public void onContextChanged(@NotNull String newUri) {

            }


            @Override
            public void onTrackChanged(@NotNull PlayableId id, @Nullable TrackOrEpisode metadata) {

            }

            @Override
            public void onPlaybackPaused(long trackTime) {

            }

            @Override
            public void onPlaybackResumed(long trackTime) {

            }

            @Override
            public void onTrackSeeked(long trackTime) {

            }

            @Override
            public void onMetadataAvailable(@NotNull TrackOrEpisode metadata) {

            }

            @Override
            public void onPlaybackHaltStateChanged(boolean halted, long trackTime) {

            }

            @Override
            public void onInactiveSession(boolean timeout) {
                getPlayer();
            }

            @Override
            public void onVolumeChanged(@Range(from = 0L, to = 1L) float volume) {

            }

            @Override
            public void onPanicState() {
                getPlayer();
            }
        };
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