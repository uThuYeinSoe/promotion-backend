package com.promotion.gameTicket.repo;

import com.promotion.game.entity.Game;
import com.promotion.gameItem.entity.GameItem;
import com.promotion.gameTicket.entity.GameTicket;
import com.promotion.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameTicketRepo extends JpaRepository<GameTicket,Long> {
    List<GameTicket> findByAgent(User agent);

    @Query("SELECT gt.gameItem FROM GameTicket gt WHERE gt.agent = :agent AND gt.game = :game AND gt.useStatus = true")
    List<GameItem> findActiveGameItemsByAgentAndGame(@Param("agent") User agent, @Param("game") Game game);

    List<GameTicket> findByAgentAndGameAndGameItemAndUseStatusTrue(User agent, Game game, GameItem gameItem);
}
