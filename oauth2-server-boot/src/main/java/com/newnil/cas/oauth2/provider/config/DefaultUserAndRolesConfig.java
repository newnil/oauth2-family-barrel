package com.newnil.cas.oauth2.provider.config;

import com.newnil.cas.oauth2.provider.dao.entity.RoleEntity;
import com.newnil.cas.oauth2.provider.dao.entity.UserEntity;
import com.newnil.cas.oauth2.provider.dao.entity.UserRoleXRef;
import com.newnil.cas.oauth2.provider.dao.repository.RoleRepository;
import com.newnil.cas.oauth2.provider.dao.repository.UserRepository;
import com.newnil.cas.oauth2.provider.dao.repository.UserRoleXrefRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("default-user-and-roles")
public class DefaultUserAndRolesConfig implements InitializingBean {

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin888";

    private static final String[] DEFAULT_ROLES = {"ADMIN", "USER"};

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleXrefRepository userRoleXrefRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void afterPropertiesSet() throws Exception {
        UserEntity defaultUserEntity = userRepository.findOneByUsername(DEFAULT_USERNAME).orElseGet(() -> {
                    UserEntity u = UserEntity.builder()
                            .username(DEFAULT_USERNAME)
                            .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                            .build();
                    u.setCreatedBy(DEFAULT_USERNAME);
                    u.setLastModifiedBy(DEFAULT_USERNAME);
                    return userRepository.save(u);
                }
        );

        List<RoleEntity> defaultRoleEntities = new ArrayList<>();
        Arrays.stream(DEFAULT_ROLES).map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role).forEach(
                role -> defaultRoleEntities.add(roleRepository.findOneByName(role).orElseGet(
                        () -> roleRepository.save(RoleEntity.builder().name(role).build())
                ))
        );

        defaultRoleEntities.stream().forEach(
                roleEntity -> {
                    UserRoleXRef urx = UserRoleXRef.builder().user(defaultUserEntity).role(roleEntity).build();
                    urx.setCreatedBy(DEFAULT_USERNAME);
                    urx.setLastModifiedBy(DEFAULT_USERNAME);
                    userRoleXrefRepository.save(urx);
                }
        );
    }
}
