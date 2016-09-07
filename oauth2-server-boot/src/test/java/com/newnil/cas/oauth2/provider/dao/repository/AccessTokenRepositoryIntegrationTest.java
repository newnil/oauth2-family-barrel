package com.newnil.cas.oauth2.provider.dao.repository;

import com.newnil.cas.oauth2.provider.dao.entity.AccessTokenEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class AccessTokenRepositoryIntegrationTest {

    @Autowired
    private AccessTokenRepository repository;

    @Test
    public void testSave(){

        AccessTokenEntity saved = repository.save(AccessTokenEntity.builder()
                .tokenId("some-token-id")
                .token(new DefaultOAuth2AccessToken("some-token-id"))
                .authenticationId("authentication-id")
                .authentication(new OAuth2Authentication(new OAuth2Request(Collections.emptyMap(),"test-clientId", null,true,Collections.emptySet(),null,"test:uri",null, null), null))
                .build());

        System.out.println(saved);

    }

}