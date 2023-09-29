package dev.shvetsova.keycloak.service;


import dev.shvetsova.ewmc.dto.user.NewUserRequest;
import dev.shvetsova.ewmc.dto.user.UserDto;

public interface KeycloakService {
    /**
     * Добавление нового пользователя
     *
     * @param body Данные добавляемого пользователя
     */
    String registerUser(NewUserRequest body);

    /**
     * Возвращает информацию обо всех пользователях
     * (учитываются параметры ограничения выборки),
     * либо о конкретных (учитываются указанные идентификаторы)
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одного пользователя, возвращает пустой список
     *
     */
    UserDto findUserById(String id);

    /**
     * Удаление пользователя
     *
     */
    void delete(String userId);

}
