package dev.shvetsova.ewmc.users.service.user;


import dev.shvetsova.ewmc.dto.user.NewUserRequest;
import dev.shvetsova.ewmc.dto.user.UserDto;

import java.util.List;

public interface UserService {
    /**
     * Добавление нового пользователя
     *
     * @param body Данные добавляемого пользователя
     * @return
     */
    UserDto registerUser(NewUserRequest body);

    /**
     * Возвращает информацию обо всех пользователях
     * (учитываются параметры ограничения выборки),
     * либо о конкретных (учитываются указанные идентификаторы)
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одного пользователя, возвращает пустой список
     *
     * @param ids  id пользователей
     * @param from количество элементов, которые нужно пропустить для формирования
     *             текущего набора default: 0
     * @param size количество элементов в наборе default: 10
     * @return
     */
    List<UserDto> getUsers(List<String> ids, Integer from, Integer size);

    /**
     * Удаление пользователя
     *
     * @param userId
     */
    void delete(String userId);

//    UserDto getUserById(String userId);

    UserDto changeSubscribeMode(String userId, boolean isAutoSubscribe);

    List<UserDto> getUsersList(List<String> userId);
}
