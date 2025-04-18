package com.promotion.ticketTransfer.repo;

import com.promotion.ticketTransfer.entity.TicketTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketTransactionHistoryRepo extends JpaRepository<TicketTransactionHistory,Long> {
    List<TicketTransactionHistory> findByReceiverRandomId(String receiverRandomId);
}
