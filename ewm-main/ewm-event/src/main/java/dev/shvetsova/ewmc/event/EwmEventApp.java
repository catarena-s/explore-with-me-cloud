package dev.shvetsova.ewmc.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EwmEventApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmEventApp.class, args);
    }

}
