package com.promotion.navigation.repo;

import com.promotion.navigation.entity.Navigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NavigationRepo extends JpaRepository<Navigation,Long> {

    @Query("SELECT n FROM Navigation n JOIN n.roles r WHERE r.roleName = :roleName")
    List<Navigation> findAllByRoleName(@Param("roleName") String roleName);
}
