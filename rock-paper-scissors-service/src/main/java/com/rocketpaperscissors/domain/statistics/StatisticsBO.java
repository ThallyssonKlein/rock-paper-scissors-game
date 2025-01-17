package com.rocketpaperscissors.domain.statistics;

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

    public void addResult(ResultValueBO resultType, Integer count) {
        switch (resultType) {
            case WIN:
                this.countOfVictories = count;
                break;
            case LOSE:
                this.countOfDefeats = count;
                break;
            case DRAW:
                this.countOfDraws = count;
                break;
        }
        this.countOfGames = (this.countOfVictories != null ? this.countOfVictories : 0) +
                (this.countOfDefeats != null ? this.countOfDefeats : 0) +
                (this.countOfDraws != null ? this.countOfDraws : 0);
    }
}
