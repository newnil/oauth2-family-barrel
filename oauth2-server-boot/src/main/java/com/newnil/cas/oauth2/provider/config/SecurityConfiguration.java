package com.newnil.cas.oauth2.provider.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableJpaAuditing
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // @formatter:off
        web
            .ignoring()
                .antMatchers("/webjars/**")
                .antMatchers("/h2-console/**")
        ;
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
        auth
            .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        // @formatter:on
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
