package io.github.patternhelloworld.tak.domain.crm.usermenuauth.dao;

import io.github.patternhelloworld.tak.domain.crm.usermenuauth.entity.UserMenuAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserMenuAuthRepository extends JpaRepository<UserMenuAuth, Long> {
    // Custom query methods if needed
    Optional<UserMenuAuth> findByUserMenuAuthIdxAndDelDt(Long userMenuIdx, LocalDateTime delDt);
}
