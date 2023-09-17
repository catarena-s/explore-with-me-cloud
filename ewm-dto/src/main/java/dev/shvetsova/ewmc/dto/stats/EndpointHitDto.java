package dev.shvetsova.ewmc.dto.stats;


import com.fasterxml.jackson.annotation.JsonFormat;
import dev.shvetsova.ewmc.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointHitDto {
    @NotBlank(message = "App cannot be empty or null")
    private String app;
    @NotBlank(message = "Uri cannot be empty or null")
    private String uri;
    @NotBlank(message = "Ip cannot be empty or null")
    private String ip;
    @NotNull(message = "Timestamp cannot be empty or null")
    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime timestamp;
}