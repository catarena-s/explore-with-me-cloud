
package dev.shvetsova.ewmc.main.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Данные нового пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    @NotBlank(message = "User name cannot be empty or null")
    @Size(min = 2, max = 250, message = "size must be between 2 and 250")
    private String name;

    @NotBlank(message = "Email cannot be empty or null")
    @Email(message = "Email must be valid")
    @Size(min = 6, max = 254, message = "size must be between 6 and 254")
    private String email;
}
