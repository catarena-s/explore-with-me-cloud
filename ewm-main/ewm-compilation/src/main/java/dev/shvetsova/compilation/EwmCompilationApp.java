package dev.shvetsova.compilation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EwmCompilationApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmCompilationApp.class, args);
    }

}
