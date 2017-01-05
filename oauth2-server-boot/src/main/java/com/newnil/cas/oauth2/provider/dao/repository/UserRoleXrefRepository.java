package com.newnil.cas.oauth2.provider.dao.repository;

import com.newnil.cas.oauth2.provider.dao.entity.UserRoleXrefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@Deprecated
public interface UserRoleXrefRepository extends JpaRepository<UserRoleXrefEntity, Long> {
}
