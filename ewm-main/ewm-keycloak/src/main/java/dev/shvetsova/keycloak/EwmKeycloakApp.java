package dev.shvetsova.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@SpringBootApplication
@EnableFeignClients
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class EwmKeycloakApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmKeycloakApp.class, args);
    }

}
