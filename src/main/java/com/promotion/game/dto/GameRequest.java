package com.promotion.game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameRequest {
    private Integer id;
    private String gameCode;
    private String gameName;
    private Boolean gameStatus;
    private Integer conversationRate;
}
