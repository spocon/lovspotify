package me.haga.librespot.spotifi;

import me.haga.librespot.spotifi.util.PlayerWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import xyz.gianlu.librespot.ZeroconfServer;
import xyz.gianlu.librespot.player.FileConfiguration;
import xyz.gianlu.librespot.player.Player;

import java.io.IOException;


@SpringBootApplication
public class SpotifiApplication {

    private static final Logger log = LogManager.getLogger(SpotifiApplication.class);
    private static FileConfiguration librespotConf;
    private static ZeroconfServer zeroconfServer;

    public static void main(String[] args) {
        try {
            librespotConf = new FileConfiguration(args);
        } catch (IOException e) {
            log.info("could not load config: ", e);
        }
        SpringApplication.run(SpotifiApplication.class, args);
    }

    @Bean
    public PlayerWrapper getPlayerWrapper() {
        PlayerWrapper playerWrapper = new PlayerWrapper();
        try {
            if (zeroconfServer == null) {
                zeroconfServer = librespotConf.initZeroconfBuilder().create();
                zeroconfServer.addSessionListener(session -> {
                    playerWrapper.setPlayer(new Player(librespotConf.toPlayer(), session));
                    playerWrapper.setSession(session);
                });
            }
        } catch (IOException e) {
            log.info("could not start SessionWrapper: ", e);
        }
        return playerWrapper;
    }

    public static FileConfiguration getLibrespotConf() {
        return librespotConf;
    }

    public static ZeroconfServer getZeroconfServer() {
        return zeroconfServer;
    }


}


