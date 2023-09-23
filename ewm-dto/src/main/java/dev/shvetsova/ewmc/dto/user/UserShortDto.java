package dev.shvetsova.ewmc.dto.user;

/**
 * Пользователь (краткая информация)
 */
public class UserShortDto {
    private String id;

    public UserShortDto(String id) {
        this.id = id;
    }

    public UserShortDto() {
    }

    public static UserShortDtoBuilder builder() {
        return new UserShortDtoBuilder();
    }

    public String getId() {
        return this.id;
    }

    public static class UserShortDtoBuilder {
        private String id;

        UserShortDtoBuilder() {
        }

        public UserShortDtoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserShortDto build() {
            return new UserShortDto(this.id);
        }

        public String toString() {
            return "UserShortDto.UserShortDtoBuilder(id=" + this.id + ")";
        }
    }
}
