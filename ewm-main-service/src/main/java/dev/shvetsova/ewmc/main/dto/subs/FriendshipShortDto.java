package dev.shvetsova.ewmc.main.dto.subs;

import dev.shvetsova.ewmc.main.enums.FriendshipState;
import dev.shvetsova.ewmc.main.dto.user.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipShortDto {
    private long id;
    private FriendshipState state;
    private UserShortDto friend;
}
