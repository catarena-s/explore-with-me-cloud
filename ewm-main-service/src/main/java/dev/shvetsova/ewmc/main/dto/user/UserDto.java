package dev.shvetsova.ewmc.main.dto.user;

import lombok.Builder;
import lombok.Data;

/**
 * Пользователь
 */
@Data
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private Boolean isAutoSubscribe;
}
