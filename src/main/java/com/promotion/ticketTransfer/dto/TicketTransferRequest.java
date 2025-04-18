package com.promotion.ticketTransfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketTransferRequest {
    private String receiverRandomId;
    private Integer ticketAmt;
}
