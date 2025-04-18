package com.promotion.ticket.repo;

import com.promotion.ticket.entity.AdminTicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminTicketHistoryRepo extends JpaRepository<AdminTicketHistory,Long> {
}
