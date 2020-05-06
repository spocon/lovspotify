package me.haga.librespot.spotifi.service;

import com.electronwill.nightconfig.core.file.FileConfig;
import me.haga.librespot.spotifi.SpotifiApplication;
import me.haga.librespot.spotifi.model.ConfigData;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.gianlu.librespot.AbsConfiguration;

import javax.sound.sampled.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ConfigService {


    public static final AudioFormat OUTPUT_FORMAT = new AudioFormat(44100, 16, 2, true, false);


    @Value("${server.port}")
    private String port;

    @Value("${librespot.config}")
    private String configFileLocation;

    public ConfigData getConfigData() {
        AbsConfiguration librespotConf = SpotifiApplication.getLibrespotConf();
        ConfigData configData = new ConfigData();
        configData.setDeviceId(librespotConf.deviceId());
        configData.setDeviceName(librespotConf.deviceName());
        configData.setDeviceType(librespotConf.deviceType());
        configData.setChosenMixer(librespotConf.mixerSearchKeywords().length != 0 ? librespotConf.mixerSearchKeywords()[0] : "");
        configData.setLogLevel(librespotConf.loggingLevel().toString());

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, OUTPUT_FORMAT, AudioSystem.NOT_SPECIFIED);
        List<String> availableMixers = findSupportingMixersFor(info).stream()
                .map(mixer -> mixer.getMixerInfo().getName())
                .collect(Collectors.toList());
        configData.setAvailableMixers(availableMixers);

        try {
            configData.setUrl("http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return configData;
    }

    public void setConfigData(ConfigData configData) {
        FileConfig conf = FileConfig.of(configFileLocation);
        conf.load();
        conf.set("deviceName", configData.getDeviceName());
        conf.set("player.mixerSearchKeywords", configData.getChosenMixer());
        conf.set("logLevel", configData.getLogLevel());
        conf.save();
        conf.close();
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
