package dev.shvetsova.ewmc.main.utils;


import dev.shvetsova.ewmc.common.exception.NotFoundException;
import dev.shvetsova.ewmc.main.feign.UserUserFeignClient;

import static dev.shvetsova.ewmc.common.Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND;
import static dev.shvetsova.ewmc.common.Constants.USER_WITH_ID_D_WAS_NOT_FOUND;

public class UsersUtil {
    public static void checkExistUser(UserUserFeignClient userService, long userId) {
        if (!userService.checkExistById(userId))
            throw new NotFoundException(
                    String.format(USER_WITH_ID_D_WAS_NOT_FOUND, userId),
                    THE_REQUIRED_OBJECT_WAS_NOT_FOUND);
    }
}
