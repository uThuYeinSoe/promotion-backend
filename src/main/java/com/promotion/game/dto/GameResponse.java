package com.promotion.game.dto;

import com.promotion.game.entity.Game;
import com.promotion.utility.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class GameResponse extends BaseResponse {
    List<Game> gameList;
}
