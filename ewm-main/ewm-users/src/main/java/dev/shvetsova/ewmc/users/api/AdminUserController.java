package dev.shvetsova.ewmc.users.api;

import dev.shvetsova.ewmc.dto.user.NewUserRequest;
import dev.shvetsova.ewmc.dto.user.UserDto;
import dev.shvetsova.ewmc.users.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.utils.Constants.FROM;
import static dev.shvetsova.ewmc.utils.Constants.PAGE_SIZE;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerUser(@Valid @RequestBody NewUserRequest body) {
        log.debug("Request received POST /admin/users : {}", body);
        return userService.registerUser(body);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(value = "ids", required = false) List<String> ids,
                                  @PositiveOrZero
                                  @RequestParam(value = "from", defaultValue = FROM) Integer from,
                                  @Positive
                                  @RequestParam(value = "size", defaultValue = PAGE_SIZE) Integer size) {
        if (ids != null) {
            log.debug("Request received GET /admin/users?{}&from={}&size={}",
                    ids.stream().map(aLong -> "ids=" + aLong).collect(Collectors.joining("&")),
                    from, size);
        } else {
            log.debug("Request received GET /admin/users?from={}&size={}", from, size);
        }
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "userId") String userId) {
        log.debug("Request received DELETE /admin/users{}", userId);
        userService.delete(userId);
    }
}
