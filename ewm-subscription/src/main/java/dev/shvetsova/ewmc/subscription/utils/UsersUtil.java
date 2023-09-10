package dev.shvetsova.ewmc.subscription.utils;


import dev.shvetsova.ewmc.subscription.exception.NotFoundException;
import dev.shvetsova.ewmc.subscription.http.UserClient;

import static dev.shvetsova.ewmc.subscription.utils.Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND;
import static dev.shvetsova.ewmc.subscription.utils.Constants.USER_WITH_ID_D_WAS_NOT_FOUND;
public class UsersUtil {
    public static void checkExistUser(UserClient userService, long userId) {
        if (!userService.checkExistById(userId))
            throw new NotFoundException(
                    String.format(USER_WITH_ID_D_WAS_NOT_FOUND, userId),
                    THE_REQUIRED_OBJECT_WAS_NOT_FOUND);
    }
}
