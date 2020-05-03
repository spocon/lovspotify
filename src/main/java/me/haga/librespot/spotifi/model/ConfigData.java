package me.haga.librespot.spotifi.model;

import com.spotify.connectstate.Connect;
import org.apache.log4j.Level;

import java.util.List;

public class ConfigData {

    private String deviceId;
    private String deviceName;
    private Connect.DeviceType deviceType;
    private String logLevel;
    private String url;
    private List<String> availableMixers;
    private String chosenMixer;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Connect.DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Connect.DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getAvailableMixers() {
        return availableMixers;
    }

    public void setAvailableMixers(List<String> availableMixers) {
        this.availableMixers = availableMixers;
    }

    public String getChosenMixer() {
        return chosenMixer;
    }

    public void setChosenMixer(String chosenMixer) {
        this.chosenMixer = chosenMixer;
    }

    /*deviceId = "" ### Device ID (40 chars, leave empty for random)  ###
        deviceName = "Lovspotify" ### Device name ###
        deviceType = "COMPUTER" ### Device type (COMPUTER, TABLET, SMARTPHONE, SPEAKER, TV, AVR, STB, AUDIO_DONGLE, GAME_CONSOLE, CAST_VIDEO, CAST_AUDIO, AUTOMOBILE, WEARABLE, UNKNOWN_SPOTIFY, CAR_THING, UNKNOWN) ###
        preferredLocale = "en" ### Preferred locale ###
        logLevel = "TRACE" ### Log level (OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL) ###

        [auth] ### Authentication ###
        strategy = "ZEROCONF" # Strategy (USER_PASS, ZEROCONF, BLOB, FACEBOOK)
        username = "" # Spotify username (BLOB, USER_PASS only)
        password = "" # Spotify password (USER_PASS only)
        blob = "" # Spotify authentication blob (BLOB only)
        storeCredentials = false # Whether to store reusable credentials on disk (not a plain password)
        credentialsFile = "" # Credentials file (JSON)

        [zeroconf] ### Zeroconf ###
        listenPort = -1 # Listen on this TCP port (`-1` for random)
        listenAll = true # Listen on     all interfaces (overrides `zeroconf.interfaces`)
        interfaces = "" # Listen on these interfaces (comma separated list of names)

        [cache] ### Cache ###
        enabled = true # Cache enabled
        dir = "./cache/"
        doCleanUp = true

        [preload] ### Preload ###
        enabled = true # Preload enabled

        [time] ### Time correction ###
        synchronizationMethod = "NTP" # Time synchronization method (NTP, PING, MELODY, MANUAL)
        manualCorrection = 0 # Manual time correction in millis

        [player] ### Player ###
        autoplayEnabled = true # Autoplay similar songs when your music ends
        preferredAudioQuality = "VORBIS_160" # Preferred audio quality (VORBIS_96, VORBIS_160, VORBIS_320)
        enableNormalisation = true # Whether to apply the Spotify loudness normalisation
        normalisationPregain = 0.0 # Normalisation pregain
        initialVolume = 65536 # Initial volume (0-65536)
        logAvailableMixers = true # Log available mixers
        mixerSearchKeywords = "" # Mixer/backend search keywords (semicolon separated)
        crossfadeDuration = 0 # Crossfade overlap time (in milliseconds)
        output = "MIXER" # Audio output device (MIXER, PIPE, STDOUT)
        releaseLineDelay = 20 # Release mixer line after set delay (in seconds)
        pipe = "" # Output raw (signed) PCM to this file (`player.output` must be PIPE)
        stopPlaybackOnChunkError = false # Whether the playback should be stopped when the current chunk cannot be downloaded
        metadataPipe = "" # Output metadata in Shairport Sync format (https://github.com/mikebrady/shairport-sync-metadata-reader)
*/
}