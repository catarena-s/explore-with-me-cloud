package dev.shvetsova.ewmc.common.dto.subs;

import dev.shvetsova.ewmc.common.dto.user.UserShortDto;
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
