package dev.shvetsova.ewmc.dto.user;

/**
 * Пользователь (краткая информация)
 */
public class UserShortDto {
    private Long id;

    public UserShortDto(Long id) {
        this.id = id;
    }

    public UserShortDto() {
    }

    public static UserShortDtoBuilder builder() {
        return new UserShortDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static class UserShortDtoBuilder {
        private Long id;

        UserShortDtoBuilder() {
        }

        public UserShortDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserShortDto build() {
            return new UserShortDto(this.id);
        }
    }
}
