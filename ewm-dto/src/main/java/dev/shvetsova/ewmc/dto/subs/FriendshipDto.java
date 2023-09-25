package dev.shvetsova.ewmc.dto.subs;

import dev.shvetsova.ewmc.dto.user.UserShortDto;

public class FriendshipDto {
    private Long id;
    private Long followerId;
    private UserShortDto friend;
    private String state;

    FriendshipDto(Long id, Long followerId, UserShortDto friend, String state) {
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

    public Long getFollowerId() {
        return this.followerId;
    }

    public UserShortDto getFriend() {
        return this.friend;
    }

    public String getState() {
        return this.state;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public void setFriend(UserShortDto friend) {
        this.friend = friend;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static class FriendshipDtoBuilder {
        private Long id;
        private Long followerId;
        private UserShortDto friend;
        private String state;

        FriendshipDtoBuilder() {
        }

        public FriendshipDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public FriendshipDtoBuilder followerId(Long followerId) {
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
    }
}
