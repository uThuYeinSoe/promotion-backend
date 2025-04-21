package com.promotion.game.repo;

import com.promotion.game.entity.Game;
import com.promotion.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepo extends JpaRepository<Game,Long> {

    @Query("SELECT u.games FROM User u WHERE u.randomId = :randomId")
    List<Game> findGamesByRandomId(@Param("randomId") String randomId);
    Optional<Game> findByGameCode(String gameCode);
    Optional<Game> findByIdAndAgentsContains(Integer gameId, User user);

}
