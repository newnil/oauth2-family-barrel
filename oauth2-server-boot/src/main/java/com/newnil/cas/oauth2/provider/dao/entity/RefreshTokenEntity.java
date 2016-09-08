package com.newnil.cas.oauth2.provider.dao.entity;

import com.newnil.cas.oauth2.provider.dao.entity.util.OAuth2RefreshTokenPersistenceConverters;
import lombok.*;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "tokenId", callSuper = false)
@ToString(of = "tokenId", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity extends AbstractAuditable<Long> {

    @NonNull
    @NotNull
    @Column(name = "token_id", nullable = false, unique = true, length = 36)
    private String tokenId;

    @NonNull
    @NotNull
    @Convert(converter = OAuth2RefreshTokenPersistenceConverters.class)
    @Column(name = "serialized_token", nullable = false)
    private OAuth2RefreshToken token;

    @NonNull
    @NotNull
    @Lob
    @Column(name = "serialized_authentication", nullable = false)
    private OAuth2Authentication authentication;

    @OneToMany(mappedBy = "refreshToken", fetch = FetchType.LAZY)
    private Set<AccessTokenEntity> accessTokens;

}
