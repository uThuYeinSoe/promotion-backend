package com.promotion.user.repository;

import com.promotion.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByRandomId(String randomId);
    List<User> findByParentId(String randomId);
}
