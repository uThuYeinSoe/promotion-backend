package com.promotion.winner.repo;

import com.promotion.winner.entity.Winner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WinnerRepo extends JpaRepository<Winner,Long> {
}
