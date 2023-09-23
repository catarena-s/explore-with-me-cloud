package dev.shvetsova.ewmc.dto.subs;

import dev.shvetsova.ewmc.dto.user.UserShortDto;

public class FriendshipDto {
    private Long id;
    private String followerId;
    private UserShortDto friend;
    private String state;

    FriendshipDto(Long id, String followerId, UserShortDto friend, String state) {
        this.id = id;
        this.followerId = followerId;
        this.friend = friend;
        this.state = state;
    }

    public static FriendshipDtoBuilder builder() {
        return new FriendshipDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getFollowerId() {
        return this.followerId;
    }

    public UserShortDto getFriend() {
        return this.friend;
    }

    public String getState() {
        return this.state;
    }

    public static class FriendshipDtoBuilder {
        private Long id;
        private String followerId;
        private UserShortDto friend;
        private String state;

        FriendshipDtoBuilder() {
        }

        public FriendshipDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public FriendshipDtoBuilder followerId(String followerId) {
            this.followerId = followerId;
            return this;
        }

        public FriendshipDtoBuilder friend(UserShortDto friend) {
            this.friend = friend;
            return this;
        }

        public FriendshipDtoBuilder state(String state) {
            this.state = state;
            return this;
        }

        public FriendshipDto build() {
            return new FriendshipDto(this.id, this.followerId, this.friend, this.state);
        }

        public String toString() {
            return "FriendshipDto.FriendshipDtoBuilder(id=" + this.id + ", followerId=" + this.followerId + ", friend=" + this.friend + ", state=" + this.state + ")";
        }
    }
}
