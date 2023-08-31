package dev.shvetsova.ewmc.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EwmMainServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmMainServiceApp.class, args);
    }

}
