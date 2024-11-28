package io.github.patternhelloworld.tak.domain.common.user.dao;

import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    Optional<User> findByUserId(String userId);
    List<User> findByName(String name);
}