package com.promotion.user.repository;

import com.promotion.user.entity.UserLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLevelRepo extends JpaRepository<UserLevel,Long> {
    Optional<UserLevel> findByRandomIdAndParentId(String randomId,String parentId);
}
