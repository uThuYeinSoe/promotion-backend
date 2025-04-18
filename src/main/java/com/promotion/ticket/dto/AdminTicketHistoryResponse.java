package com.promotion.ticket.dto;

import com.promotion.ticket.entity.AdminTicketHistory;
import com.promotion.utility.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
public class AdminTicketHistoryResponse extends BaseResponse {
    private List<AdminTicketHistory> adminTicketHistories;
}
