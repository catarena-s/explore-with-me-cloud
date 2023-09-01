package dev.shvetsova.ewmc.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EwmSubscriptionApp {

	public static void main(String[] args) {
		SpringApplication.run(EwmSubscriptionApp.class, args);
	}

}
