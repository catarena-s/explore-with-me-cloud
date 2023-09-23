
package dev.shvetsova.ewmc.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * Данные нового пользователя
 */
public class NewUserRequest {
    @NotBlank(message = "User name cannot be empty or null")
    @Size(min = 2, max = 250, message = "size must be between 2 and 250")
    private String name;

    @NotBlank(message = "Email cannot be empty or null")
    @Email(message = "Email must be valid")
    @Size(min = 6, max = 254, message = "size must be between 6 and 254")
    private String email;

    private String password;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public NewUserRequest(@NotBlank(message = "User name cannot be empty or null")
                          @Size(min = 2, max = 250, message = "size must be between 2 and 250") String name,
                          @NotBlank(message = "Email cannot be empty or null") @Email(message = "Email must be valid")
                          @Size(min = 6, max = 254, message = "size must be between 6 and 254") String email,
                          String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public NewUserRequest() {
    }

    public static NewUserRequestBuilder builder() {
        return new NewUserRequestBuilder();
    }

    public @NotBlank(message = "User name cannot be empty or null") @Size(min = 2, max = 250, message = "size must be between 2 and 250") String getName() {
        return this.name;
    }

    public @NotBlank(message = "Email cannot be empty or null") @Email(message = "Email must be valid") @Size(min = 6, max = 254, message = "size must be between 6 and 254") String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public static class NewUserRequestBuilder {
        @NotBlank(message = "User name cannot be empty or null")
        @Size(min = 2, max = 250, message = "size must be between 2 and 250")
        private String name;
        @NotBlank(message = "Email cannot be empty or null")
        @Email(message = "Email must be valid")
        @Size(min = 6, max = 254, message = "size must be between 6 and 254")
        private String email;
        private String password;

        NewUserRequestBuilder() {
        }

        public NewUserRequestBuilder name(@NotBlank(message = "User name cannot be empty or null") @Size(min = 2, max = 250, message = "size must be between 2 and 250") String name) {
            this.name = name;
            return this;
        }

        public NewUserRequestBuilder email(@NotBlank(message = "Email cannot be empty or null") @Email(message = "Email must be valid") @Size(min = 6, max = 254, message = "size must be between 6 and 254") String email) {
            this.email = email;
            return this;
        }

        public NewUserRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public NewUserRequest build() {
            return new NewUserRequest(this.name, this.email, this.password);
        }

//        public String toString() {
//            return "NewUserRequest.NewUserRequestBuilder(name=" + this.name + ", email=" + this.email + ")";
//        }
    }
}
