package com.newnil.cas.oauth2.provider.dao.repository;

import com.newnil.cas.oauth2.provider.dao.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findOneByTokenId(String tokenId);

    void deleteByTokenId(String tokenId);
}
