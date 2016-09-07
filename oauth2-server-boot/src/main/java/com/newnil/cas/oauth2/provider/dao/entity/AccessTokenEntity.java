package com.newnil.cas.oauth2.provider.dao.entity;

import com.newnil.cas.oauth2.provider.dao.entity.util.OAuth2AccessTokenPersistenceConverters;
import lombok.*;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "access_token")
public class AccessTokenEntity extends AbstractAuditable<Long> {

    @Column(name = "token_id", nullable = false, unique = true, length = 36)
    private String tokenId;

    @Convert(converter = OAuth2AccessTokenPersistenceConverters.class)
    @Column(name = "serialized_token", nullable = false)
    private OAuth2AccessToken token;

    @Column(name = "authentication_id", nullable = false, length = 32)
    private String authenticationId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "client_id")
    private String clientId;

    @Lob
    @Column(name = "serialized_authentication")
    private OAuth2Authentication authentication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token_id")
    private RefreshTokenEntity refreshToken;

}
