package com.newnil.cas.oauth2.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;

/**
 * Created by dewafer on 2017/1/6.
 */
@Configuration
public class TrustResolverConfig {

    @Bean
    public AuthenticationTrustResolver authenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }
}
