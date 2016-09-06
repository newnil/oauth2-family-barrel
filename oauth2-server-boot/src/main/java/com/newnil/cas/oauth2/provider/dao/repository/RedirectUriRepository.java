package com.newnil.cas.oauth2.provider.dao.repository;

import com.newnil.cas.oauth2.provider.dao.entity.RedirectUriEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RedirectUriRepository extends JpaRepository<RedirectUriEntity, Long> {

    Optional<RedirectUriEntity> findOneByValue(String value);
}
