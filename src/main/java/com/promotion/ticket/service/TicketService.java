package com.promotion.ticket.service;

import com.promotion.ticket.dto.TicketRequest;
import com.promotion.ticket.dto.TicketResponse;

public interface TicketService {
    public TicketResponse saveTicket(String randomId, TicketRequest request);
}
