package dev.shvetsova.ewmc.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EwmUsersApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmUsersApp.class, args);
    }

}
