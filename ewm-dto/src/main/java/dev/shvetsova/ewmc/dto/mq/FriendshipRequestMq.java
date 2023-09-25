package dev.shvetsova.ewmc.dto.mq;

import java.io.Serializable;

public class FriendshipRequestMq implements Serializable {
    private Long consumerId;
    private Long requesterId;
    private Long friendshipId;

    @Override
    public String toString() {
        return "FriendshipRequestMq{" +
                "consumerId=" + consumerId +
                ", requesterId=" + requesterId +
                ", friendshipId=" + friendshipId +
                '}';
    }

    public FriendshipRequestMq(Long consumerId, Long requesterId, Long friendshipId) {
        this.consumerId = consumerId;
        this.requesterId = requesterId;
        this.friendshipId = friendshipId;
    }

    public FriendshipRequestMq() {
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public Long getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(Long friendshipId) {
        this.friendshipId = friendshipId;
    }
}
