package com.srmasset.ports.inbound.http.api.v1.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OutboundStartGameDTO {
    private Long gameId;
}
