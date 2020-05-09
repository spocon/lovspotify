package me.haga.librespot.spotifi.service;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.spotify.connectstate.Connect;
import me.haga.librespot.spotifi.SpotifiApplication;
import me.haga.librespot.spotifi.model.ConfigData;
import me.haga.librespot.spotifi.model.config.LogLevelConfig;
import me.haga.librespot.spotifi.model.config.MainConfig;
import me.haga.librespot.spotifi.model.config.ServerPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Service;
import xyz.gianlu.librespot.AbsConfiguration;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ConfigService {


    private static final Logger log = LogManager.getLogger(ConfigService.class);
    public static final AudioFormat OUTPUT_FORMAT = new AudioFormat(44100, 16, 2, true, false);


    @Value("${server.port}")
    private String port;

    private static final String rootDir = "/opt/lovspotify";
    String appFile = rootDir + "/gui.yml";
    String librespotFile = rootDir+"/config.toml";
    private static ObjectMapper objectMapper;
    private static MainConfig mainConfig;

    public ConfigData getConfigData() {

        objectMapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        try {
            mainConfig = objectMapper.readValue(new File(appFile), MainConfig.class);
        } catch (IOException e) {
            log.warn("Couldn't read your config gui.yml");
        }


        AbsConfiguration librespotConf = SpotifiApplication.getLibrespotConf();
        ConfigData configData = new ConfigData();
        configData.setDeviceId(librespotConf.deviceId());
        configData.setDeviceName(librespotConf.deviceName());
        configData.setDeviceType(librespotConf.deviceType());
        configData.setChosenMixer(librespotConf.mixerSearchKeywords().length != 0 ? librespotConf.mixerSearchKeywords()[0] : "");
        configData.setLogLevel(mainConfig.getLogging().getLevel().getRoot());
        configData.setServerPort(mainConfig.getServer().getPort());

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, OUTPUT_FORMAT, AudioSystem.NOT_SPECIFIED);
        List<String> availableMixers = findSupportingMixersFor(info).stream()
                .map(mixer -> mixer.getMixerInfo().getName())
                .collect(Collectors.toList());
        configData.setAvailableMixers(availableMixers);
        configData.setAvailableLogLevels(Arrays.stream(LogLevel.values()).map(Enum::name).collect(Collectors.toList()));
        configData.setAvailableDeviceTypes(Arrays.stream(Connect.DeviceType.values()).map(Enum::name).collect(Collectors.toList()));

        try {
            configData.setUrl("http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return configData;
    }

    public void setConfigData(ConfigData configData) {
        FileConfig conf = FileConfig.of("/opt/lovspotify/config.toml");
        conf.load();
        conf.set("deviceName", configData.getDeviceName());
        conf.set("deviceType", configData.getDeviceType());
        conf.set("player.mixerSearchKeywords", configData.getChosenMixer());
        conf.save();
        conf.close();

        try {
            mainConfig.setLogging(new LogLevelConfig(configData.getLogLevel()));
            mainConfig.setServer(new ServerPort(configData.getServerPort()));
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(new File(appFile), mainConfig);
        } catch (IOException e) {
            log.warn("Could not write value to gui.yml", e);
        }
        log.info("Shutdown of ZeroConfServer");
        try {
            SpotifiApplication.getZeroconfServer().closeSession();
            SpotifiApplication.getZeroconfServer().close();
        } catch (IOException e) {
            log.error("Could not shutdown ZeroConfServer: ", e);
        }
        System.exit(0);
    }


    private static List<Mixer> findSupportingMixersFor(@NotNull Line.Info info) {
        List<Mixer> mixers = new ArrayList<>();
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(info))
                mixers.add(mixer);
        }
        return mixers;

    }


}
