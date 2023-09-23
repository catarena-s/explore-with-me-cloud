package dev.shvetsova.ewmc.dto.user;

/**
 * Пользователь
 */
public class UserDto {
    private String id;
    private String userName;
    private String email;
    private String password;
    private Boolean isAutoSubscribe;


    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAutoSubscribe(Boolean autoSubscribe) {
        isAutoSubscribe = autoSubscribe;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getAutoSubscribe() {
        return isAutoSubscribe;
    }

    UserDto(String id, String email, String userName, Boolean isAutoSubscribe) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.isAutoSubscribe = isAutoSubscribe;
    }

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUserName() {
        return this.userName;
    }

    public Boolean getIsAutoSubscribe() {
        return this.isAutoSubscribe;
    }

    public static class UserDtoBuilder {
        private String id;
        private String email;
        private String name;
        private Boolean isAutoSubscribe;

        UserDtoBuilder() {
        }

        public UserDtoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserDtoBuilder isAutoSubscribe(Boolean isAutoSubscribe) {
            this.isAutoSubscribe = isAutoSubscribe;
            return this;
        }

        public UserDto build() {
            return new UserDto(this.id, this.email, this.name, this.isAutoSubscribe);
        }

    }
}
