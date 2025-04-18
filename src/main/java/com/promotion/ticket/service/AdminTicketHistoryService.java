package com.promotion.ticket.service;

import com.promotion.ticket.dto.AdminTicketHistoryResponse;

public interface AdminTicketHistoryService {
    public AdminTicketHistoryResponse craetAdminTicketHistory(String randomId);
    public AdminTicketHistoryResponse getAdminTicketHistory();
}
