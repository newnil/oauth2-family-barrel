package com.newnil.cas.oauth2.provider.config;

import com.newnil.cas.oauth2.provider.service.DatabaseTokenStoreService;
import com.newnil.cas.oauth2.provider.service.OAuth2DatabaseClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;

/**
 * 填坑注意：
 * <p>
 * OAuthProvider不可以和OAuthClient在同一个contextPath下（即使是不同端口也不行，
 * 不要问我为什么会知道）。否则会发生意料不到难以想象甚至你调查不出的错误。
 * https://github.com/spring-projects/spring-security-oauth/issues/322#issuecomment-
 * 64951927
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DatabaseTokenStoreService tokenStoreService;

    @Autowired
    private OAuth2DatabaseClientDetailsService oAuth2DatabaseClientDetailsService;

    @Bean
    public ApprovalStore approvalStore() {
        TokenApprovalStore tokenStore = new TokenApprovalStore();
        tokenStore.setTokenStore(tokenStoreService);
        return tokenStore;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        // 配置授权endpoint
        endpoints.tokenStore(tokenStoreService).approvalStore(approvalStore());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
            throws Exception {
        // 配置授权endpoint权限
        security.checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(oAuth2DatabaseClientDetailsService);
    }

}
