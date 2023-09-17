package dev.shvetsova.ewmc.utils;


import dev.shvetsova.ewmc.exception.NotFoundException;

import static dev.shvetsova.ewmc.utils.Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND;
import static dev.shvetsova.ewmc.utils.Constants.USER_WITH_ID_D_WAS_NOT_FOUND;


public class UsersUtil {
    public static void checkExistUser(UserFeignClient userService, long userId) {
        if (!userService.checkExistById(userId))
            throw new NotFoundException(
                    String.format(USER_WITH_ID_D_WAS_NOT_FOUND, userId),
                    THE_REQUIRED_OBJECT_WAS_NOT_FOUND);
    }
}
