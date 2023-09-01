package dev.shvetsova.ewmc.common.dto.subs;

import dev.shvetsova.ewmc.common.dto.user.UserShortDto;
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
