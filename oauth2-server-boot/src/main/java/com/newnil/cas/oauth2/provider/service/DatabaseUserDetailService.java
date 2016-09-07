package com.newnil.cas.oauth2.provider.service;

import com.newnil.cas.oauth2.provider.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class DatabaseUserDetailService implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneByUsername(username).map(userEntity ->
                new User(userEntity.getUsername(),
                        userEntity.getPassword(),
                        userEntity.getRoles().stream().map(userRoleXRef ->
                                new SimpleGrantedAuthority(prefixRoleName(userRoleXRef.getRole().getName())))
                                .collect(Collectors.toList())))
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found in the database"));
    }

    private String prefixRoleName(String roleName){
        if (!StringUtils.isEmpty(roleName) && !roleName.startsWith(ROLE_PREFIX)){
            return ROLE_PREFIX + roleName;
        }
        return roleName;
    }
}
