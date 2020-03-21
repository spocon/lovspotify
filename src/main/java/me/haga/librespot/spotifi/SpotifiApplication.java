package me.haga.librespot.spotifi;

import me.haga.librespot.spotifi.util.SessionWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import xyz.gianlu.librespot.AbsConfiguration;
import xyz.gianlu.librespot.FileConfiguration;
import xyz.gianlu.librespot.core.ZeroconfServer;

import java.io.IOException;


@SpringBootApplication
public class SpotifiApplication {

    private static final Logger logger = LogManager.getLogger(SpotifiApplication.class);
    private static AbsConfiguration librespotConf;
    private static ZeroconfServer zeroconfServer;

    public static void main(String[] args) {
        try {
            librespotConf = new FileConfiguration(args);
        } catch (IOException e) {
            logger.info("could not load config: ", e);
        }
        SpringApplication.run(SpotifiApplication.class, args);
    }

    @Bean
    public SessionWrapper getSessionWrapper() {
        try {
            if(zeroconfServer == null) {
                zeroconfServer = ZeroconfServer.create(librespotConf);
            }
            return SessionWrapper.fromZeroconf(zeroconfServer);
        } catch (IOException e) {
            logger.info("could not start SessionWrapper: ", e);
        }
        return null;
    }
}


