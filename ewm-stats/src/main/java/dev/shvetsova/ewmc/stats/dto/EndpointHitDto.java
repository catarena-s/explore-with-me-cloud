package dev.shvetsova.ewmc.stats.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static dev.shvetsova.ewmc.stats.utils.Constants.YYYY_MM_DD_HH_MM_SS;


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
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime timestamp;
}