package dev.shvetsova.ewmc.subscription.dto.subs;

import dev.shvetsova.ewmc.subscription.dto.user.UserShortDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FriendshipDto {
    private Long id;
    private Long followerId;
    private UserShortDto friend;
    private String  state;
}
