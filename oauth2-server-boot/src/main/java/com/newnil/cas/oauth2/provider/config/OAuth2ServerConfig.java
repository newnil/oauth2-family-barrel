package com.newnil.cas.oauth2.provider.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.newnil.cas.oauth2.provider.oauth.SparklrUserApprovalHandler;

/**
 * 填坑注意：
 * 
 * OAuthProvider不可以和OAuthClient在同一个contextPath下（即使是不同端口也不行，
 * 不要问我为什么会知道）。否则会发生意料不到难以想象甚至你调查不出的错误。
 * https://github.com/spring-projects/spring-security-oauth/issues/322#issuecomment-
 * 64951927
 *
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private TokenStore tokenStore;

	@Bean
	public TokenStore tokenStore() {
		// TODO in-memory for test，以后改成数据库保持
		// db实现可参考：JdbcTokenStore
		// redis实现可参考：RedisTokenStore
		return new InMemoryTokenStore();
	}

	@Autowired
	private UserApprovalHandler userApprovalHandler;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		// 配置授权endpoint
		endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security)
			throws Exception {
		// 配置授权endpoint权限
		security.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// TODO in-memory for test
		// DB实现可参考：JdbcClientDetailsService
		// 授权client
		// @formatter:off
		clients.inMemory().withClient("test-clientId")
					.secret("test-clientId-secret-123")
					.authorizedGrantTypes("authorization_code", "refresh_token", "password")
					.scopes("read", "write", "trust")
					// 自动授权
					//.autoApprove(true)
					.and()
				/////////////////////////////////////
				// 下面这个.inMemory()害我调查了2个小时……
				// 留在这里纪念我浪费的这2小时
				// .inMemory()
				/////////////////////////////////////
				.withClient("test-res-client")
				.secret("test-res-client-secret-123")
				.autoApprove(true)
				.and()
		;
		// @formatter:on
		
		clientIds.add("test-clientId");
		clientIds.add("test-res-client");
	}
	
	@Autowired
	private ClientIdsInMemory clientIds;
	
	@Bean
	public ClientIdsInMemory getClientIdsInMemory (){
		return new ClientIdsInMemory();
	}
	
	@SuppressWarnings("serial")
	public static class ClientIdsInMemory extends ArrayList<String> {
		
	}

	/**
	 * 以下配置是可选项
	 * 
	 * @author dewafer
	 *
	 */
	public static class OptionalConfigs {
		@Autowired
		private ClientDetailsService clientDetailsService;

		@Autowired
		private TokenStore tokenStore;

		@Bean
		public ApprovalStore approvalStore() throws Exception {
			TokenApprovalStore store = new TokenApprovalStore();
			store.setTokenStore(tokenStore);
			return store;
		}

		@Bean
		@Lazy
		@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
		public SparklrUserApprovalHandler userApprovalHandler() throws Exception {
			SparklrUserApprovalHandler handler = new SparklrUserApprovalHandler();
			handler.setApprovalStore(approvalStore());
			handler.setRequestFactory(
					new DefaultOAuth2RequestFactory(clientDetailsService));
			handler.setClientDetailsService(clientDetailsService);
			handler.setUseApprovalStore(true);
			return handler;
		}
	}
}
