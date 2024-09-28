package com.nobelcareers.ports.inbound.http.api.v1.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InboundGameResultDTO {
    private MovementValueDTO playerMovement;
}
