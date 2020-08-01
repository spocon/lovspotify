package me.haga.librespot.spotifi;

import me.haga.librespot.spotifi.util.SessionWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import xyz.gianlu.librespot.ZeroconfServer;
import xyz.gianlu.librespot.player.FileConfiguration;

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
    public SessionWrapper getSessionWrapper() {
        try {
            if (zeroconfServer == null) {
                zeroconfServer = librespotConf.initZeroconfBuilder().create();
            }
            return SessionWrapper.fromZeroconf(zeroconfServer);
        } catch (IOException e) {
            log.info("could not start SessionWrapper: ", e);
        }
        return null;
    }

    public static FileConfiguration getLibrespotConf() {
        return librespotConf;
    }

    public static ZeroconfServer getZeroconfServer() {
        return zeroconfServer;
    }


}


