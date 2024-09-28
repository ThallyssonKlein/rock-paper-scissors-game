package com.nobelcareers.domain.statistics;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StatisticsBO {
    private Integer countOfGames;
    private Integer countOfVictories;
    private Integer countOfDefeats;
    private Integer countOfDraws;
}
