package com.promotion.gameItem.service;

import com.promotion.gameItem.dto.GameItemRequest;
import com.promotion.gameItem.dto.GameItemResp;

public interface GameItemService {
    GameItemResp saveGameItem(String randomId, GameItemRequest request);
    GameItemResp getGameItemAll(String randomId);
    GameItemResp updateStatusGameItemS(String randomId,GameItemRequest request);
    GameItemResp getGameItemByGame(Long id,String randomId);

}
