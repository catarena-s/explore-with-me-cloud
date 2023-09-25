package dev.shvetsova.ewmc.dto.subs;

import dev.shvetsova.ewmc.dto.user.UserShortDto;

public class FriendshipShortDto {
    private long id;
    private String state;
    private UserShortDto friend;

    public FriendshipShortDto(long id, String state, UserShortDto friend) {
        this.id = id;
        this.state = state;
        this.friend = friend;
    }

    public FriendshipShortDto() {
    }

    public static FriendshipShortDtoBuilder builder() {
        return new FriendshipShortDtoBuilder();
    }

    public long getId() {
        return this.id;
    }

    public String getState() {
        return this.state;
    }

    public UserShortDto getFriend() {
        return this.friend;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setFriend(UserShortDto friend) {
        this.friend = friend;
    }

    public static class FriendshipShortDtoBuilder {
        private long id;
        private String state;
        private UserShortDto friend;

        FriendshipShortDtoBuilder() {
        }

        public FriendshipShortDtoBuilder id(long id) {
            this.id = id;
            return this;
        }

        public FriendshipShortDtoBuilder state(String state) {
            this.state = state;
            return this;
        }

        public FriendshipShortDtoBuilder friend(UserShortDto friend) {
            this.friend = friend;
            return this;
        }

        public FriendshipShortDto build() {
            return new FriendshipShortDto(this.id, this.state, this.friend);
        }
    }
}
