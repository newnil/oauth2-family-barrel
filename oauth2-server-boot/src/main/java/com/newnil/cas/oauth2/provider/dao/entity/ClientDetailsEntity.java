package com.newnil.cas.oauth2.provider.dao.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "client_details")
public class ClientDetailsEntity extends AbstractAuditable<Long> {

    @NotNull
    @Column(name = "client_id", unique = true, nullable = false, length = 200)
    private String clientId;

    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    @Column(name = "access_token_validity_seconds")
    private Integer accessTokenValiditySeconds;

    @Column(name = "refresh_token_validity_seconds")
    private Integer refreshTokenValiditySeconds;

    @OneToMany(mappedBy = "clientDetails", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @Singular
    private Set<ClientDetailsToAuthorizedGrantTypeXrefEntity> authorizedGrantTypeXrefs;

    @OneToMany(mappedBy = "clientDetails", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @Singular
    private Set<ClientDetailsToScopesXrefEntity> scopeXrefs;

    @OneToMany(mappedBy = "clientDetails", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @Singular
    private Set<ClientDetailsToResourceIdXrefEntity> resourceIdXrefs;

    @OneToMany(mappedBy = "clientDetails", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @Singular("redirectUri")
    private Set<RedirectUriEntity> redirectUris;

}
