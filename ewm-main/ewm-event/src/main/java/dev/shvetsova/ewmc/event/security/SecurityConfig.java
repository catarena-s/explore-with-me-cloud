package dev.shvetsova.ewmc.event.security;

import dev.shvetsova.ewmc.utils.converter.KCRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // конвертер для настройки spring security
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        // подключаем конвертер ролей
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KCRoleConverter());

        // разрешить всем заходить по адресу /test/login
        http
                .authorizeHttpRequests(auth -> auth.requestMatchers("/users/*").hasRole("user"))

                .authorizeHttpRequests(auth -> auth.requestMatchers("/admin/*").hasRole("admin"))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        http.oauth2ResourceServer(oauth2Configurer -> oauth2Configurer
                .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));


        return http.build();
    }
}
