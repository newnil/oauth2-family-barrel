package com.newnil.cas.oauth2.provider.dao.repository;

import com.newnil.cas.oauth2.provider.dao.entity.UserRoleXRef;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleXrefRepository extends JpaRepository<UserRoleXRef, Long> {
}
