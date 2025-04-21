package com.promotion.gameItem.repo;

import com.promotion.game.entity.Game;
import com.promotion.gameItem.entity.GameItem;
import com.promotion.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameItemRepo extends JpaRepository<GameItem,Long> {

    @Query("SELECT gi FROM GameItem gi WHERE gi.id = :id AND gi.agent.randomId = :randomId")
    Optional<GameItem> findByIdAndAgent(@Param("id") Integer id, @Param("randomId") String randomId);
    Optional<GameItem> findByIdAndGame(Integer id, Game game);
    List<GameItem> findByAgentAndGame(User agent, Game game);

}
