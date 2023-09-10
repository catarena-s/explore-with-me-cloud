package dev.shvetsova.ewmc.subscription.http;

import dev.shvetsova.ewmc.subscription.dto.user.UserDto;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ewm-users", fallbackFactory  = UserClientFallback.class)
public interface UserClient {

    @GetMapping("/users/{userId}")
    UserDto findUserById(@PathVariable(value = "userId") long userId);

    @GetMapping("/users/{userId}/list?ids={ids}")
    List<UserDto> getUserList(@PathVariable(value = "userId") long userId,
                              @RequestParam(value = "ids") List<Long> ids);

    @GetMapping("/users/{userId}/check")
    Boolean checkExistById(@PathVariable(value = "userId") long userId);

}

@Component
class UserClientFallback implements FallbackFactory<UserClient> {


    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public UserDto findUserById(long userId) {
                return null;
            }

            @Override
            public List<UserDto> getUserList(long userId, List<Long> ids) {
                return null;
            }

            @Override
            public Boolean checkExistById(long userId) {
                return null;
            }
        };
    }
}