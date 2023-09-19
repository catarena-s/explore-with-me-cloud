package dev.shvetsova.ewmc.dto.subs;

import dev.shvetsova.ewmc.dto.user.UserShortDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FriendshipDto {
    private Long id;
    private String followerId;
    private UserShortDto friend;
    private String  state;
}
