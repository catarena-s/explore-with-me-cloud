package dev.shvetsova.ewmc.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class EwmConfigApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmConfigApp.class, args);
    }

}
