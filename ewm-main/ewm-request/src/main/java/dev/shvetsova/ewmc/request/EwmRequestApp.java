package dev.shvetsova.ewmc.request;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EwmRequestApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmRequestApp.class, args);
    }

}
