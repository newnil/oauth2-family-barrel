package com.newnil.cas.oauth2.provider.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
// 不要启用下面这个，否则yml中的security.ignored无法读取进来
// @EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO user details
		// 假用户，真实情况需要实现UserDetailsService，可参考jdbc系的实现
		auth.inMemoryAuthentication().withUser("marissa").password("koala").roles("USER")
				.and().withUser("paul").password("emu").roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
                 http
            .authorizeRequests()
            	.antMatchers(HttpMethod.GET, "/").permitAll()
            	.and()
            .authorizeRequests()
                .antMatchers("/login.html").permitAll()
                .anyRequest().hasRole("USER")
                .and()
            .exceptionHandling()
                .accessDeniedPage("/login.html?authorization_error=true")
                .and()
            .logout()
            	.logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .and()
            .formLogin()
            	.loginProcessingUrl("/login")
                .failureUrl("/login.html?authentication_error=true")
                .defaultSuccessUrl("/")
                .loginPage("/login.html");
        // @formatter:on
	}
}
