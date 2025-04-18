package com.promotion.game.service;

import com.promotion.game.dto.GameAndAgentJoinRequest;
import com.promotion.game.dto.GameAndAgentJoinResponse;
import com.promotion.game.dto.GameRequest;
import com.promotion.game.dto.GameResponse;

public interface GameService {
    GameResponse saveGame(String randomId,GameRequest request);
    GameAndAgentJoinResponse assignGameToAgent(String randomId,GameAndAgentJoinRequest request);
    GameResponse getGameAll(String randomId);
    GameResponse updateGame(String randomId, GameRequest request);
}
