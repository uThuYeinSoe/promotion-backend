package com.promotion.gameTicket.repo;

import com.promotion.gameTicket.entity.GameTicket;
import com.promotion.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameTicketRepo extends JpaRepository<GameTicket,Long> {
    List<GameTicket> findByAgent(User agent);
}
