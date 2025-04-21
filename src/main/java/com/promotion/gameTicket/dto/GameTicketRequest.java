package com.promotion.gameTicket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameTicketRequest {
    private Integer gameId;
    private Integer gameItemId;
    private Integer ticketAmt;
}
