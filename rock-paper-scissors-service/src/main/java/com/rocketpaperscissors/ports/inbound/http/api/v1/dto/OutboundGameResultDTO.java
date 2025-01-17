package com.rocketpaperscissors.ports.inbound.http.api.v1.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OutboundGameResultDTO {
    private String hash;
    private String salt;
    private String serverMove;
    private String result;
}
