package com.srmasset.ports.inbound.http.api.v1.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OutboundStatisticsDTO {
    private Integer countOfGames;
    private Integer countOfVictories;
    private Integer countOfDefeats;
    private Integer countOfDraws;
}
