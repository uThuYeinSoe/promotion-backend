package com.promotion.ticketTransfer.dto;

import com.promotion.ticketTransfer.entity.TicketTransactionHistory;
import com.promotion.utility.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
@AllArgsConstructor
public class TicketTransactionHistoryResponse extends BaseResponse {
    private List<TicketTransactionHistory> ticketTransactionHistories;
}
