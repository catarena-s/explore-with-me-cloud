package dev.shvetsova.ewmc.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EwmStatsApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmStatsApp.class, args);
    }

}
