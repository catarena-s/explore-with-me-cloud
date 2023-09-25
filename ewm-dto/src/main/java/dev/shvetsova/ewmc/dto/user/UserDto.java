package dev.shvetsova.ewmc.dto.user;

/**
 * Пользователь
 */
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private Boolean isAutoSubscribe;

    UserDto(Long id, String email, String name, Boolean isAutoSubscribe) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.isAutoSubscribe = isAutoSubscribe;
    }

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public Boolean getIsAutoSubscribe() {
        return this.isAutoSubscribe;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsAutoSubscribe(Boolean isAutoSubscribe) {
        this.isAutoSubscribe = isAutoSubscribe;
    }

    public static class UserDtoBuilder {
        private Long id;
        private String email;
        private String name;
        private Boolean isAutoSubscribe;

        UserDtoBuilder() {
        }

        public UserDtoBuilder id(Long id) {
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
