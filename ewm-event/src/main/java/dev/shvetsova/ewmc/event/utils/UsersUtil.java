package dev.shvetsova.ewmc.event.utils;


import dev.shvetsova.ewmc.event.exception.NotFoundException;
import dev.shvetsova.ewmc.event.http.UserClient;

import static dev.shvetsova.ewmc.event.utils.Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND;
import static dev.shvetsova.ewmc.event.utils.Constants.USER_WITH_ID_D_WAS_NOT_FOUND;

public class UsersUtil {
    public static void checkExistUser(UserClient userService, long userId) {
        if (!userService.checkExistById(userId))
            throw new NotFoundException(
                    String.format(USER_WITH_ID_D_WAS_NOT_FOUND, userId),
                    THE_REQUIRED_OBJECT_WAS_NOT_FOUND);
    }
}
