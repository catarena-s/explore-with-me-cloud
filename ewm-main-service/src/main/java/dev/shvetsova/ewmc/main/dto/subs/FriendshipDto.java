package dev.shvetsova.ewmc.main.dto.subs;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.shvetsova.ewmc.main.enums.FriendshipState;
import dev.shvetsova.ewmc.main.dto.user.UserShortDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FriendshipDto {
    private Long id;
    private Long followerId;
    private UserShortDto friend;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private FriendshipState state;
}
