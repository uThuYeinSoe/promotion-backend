package com.promotion.gameTicket.service;

import com.promotion.gameTicket.dto.GameTicketRequest;
import com.promotion.gameTicket.dto.GameTicketResp;

public interface GameTicketService {
    GameTicketResp saveGameTicket(String randomId, GameTicketRequest request);
    GameTicketResp getTicketAllByAgent(String randomId);
}
