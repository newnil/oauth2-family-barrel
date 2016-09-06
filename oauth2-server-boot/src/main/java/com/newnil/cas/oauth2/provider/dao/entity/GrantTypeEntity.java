package com.newnil.cas.oauth2.provider.dao.entity;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "grant_type")
public class GrantTypeEntity extends AbstractPersistable<Long> {

    @Column(name = "value", nullable = false)
    private String value;

    @OneToMany(mappedBy = "grantType")
    @Singular
    private Set<ClientDetailsToAuthorizedGrantTypeXrefEntity> clientDetailsToAuthorizedGrantTypeXrefs;

}
