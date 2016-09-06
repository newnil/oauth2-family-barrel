package com.newnil.cas.oauth2.provider.dao.repository;

import com.newnil.cas.oauth2.provider.dao.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findOneByName(String roleName);

}
