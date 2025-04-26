package com.promotion.game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GameResponseObj {
    private Integer gameId;
    private String gameCode;
    private String gameRoute;
    private String gameName;
    private String gameConversion;
}
