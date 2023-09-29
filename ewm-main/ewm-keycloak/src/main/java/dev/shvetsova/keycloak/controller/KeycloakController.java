package dev.shvetsova.keycloak.controller;

import dev.shvetsova.ewmc.dto.user.NewUserRequest;
import dev.shvetsova.ewmc.dto.user.UserDto;
import dev.shvetsova.keycloak.service.KeycloakService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kc/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class KeycloakController {

    private final KeycloakService keycloakService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@Valid @RequestBody NewUserRequest body) {
        return keycloakService.registerUser(body);
    }

    @GetMapping("/{userId}")
    public UserDto getUsers(@PathVariable String userId) {
        log.info("Request received GET /kc/users{}", userId);
        return keycloakService.findUserById(userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "userId") String userId) {
        log.debug("Request received DELETE /kc/users{}", userId);
        keycloakService.delete(userId);
    }
}
