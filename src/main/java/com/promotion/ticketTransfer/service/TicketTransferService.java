package com.promotion.ticketTransfer.service;

import com.promotion.ticketTransfer.dto.TicketTransactionHistoryResponse;
import com.promotion.ticketTransfer.dto.TicketTransferRequest;
import com.promotion.ticketTransfer.dto.TicketTransferResponse;

public interface TicketTransferService {
    public TicketTransferResponse saveTicketTransfer(String senderRandomId, TicketTransferRequest request);
    public TicketTransactionHistoryResponse getTicketTransactionHistory(String randomId);
}
