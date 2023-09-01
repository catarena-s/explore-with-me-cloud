package dev.shvetsova.ewmc.subscription.dto.subs;

import dev.shvetsova.ewmc.subscription.dto.user.UserShortDto;
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
    private String state;
    private UserShortDto friend;
}
