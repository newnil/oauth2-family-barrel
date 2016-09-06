package com.newnil.cas.oauth2.provider.dao.repository;

import com.newnil.cas.oauth2.provider.dao.entity.ResourceIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResourceIdRepository extends JpaRepository<ResourceIdEntity, Long> {

    Optional<ResourceIdEntity> findOneByValue(String value);
}
