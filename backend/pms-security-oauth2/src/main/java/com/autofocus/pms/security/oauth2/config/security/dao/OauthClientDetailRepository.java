package com.autofocus.pms.security.oauth2.config.security.dao;

import com.autofocus.pms.security.oauth2.config.security.entity.CustomOauthClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthClientDetailRepository extends JpaRepository<CustomOauthClientDetail, String> {

    Optional<CustomOauthClientDetail> findByClientIdAndResourceIds(String clientId, String resourceIds);

}