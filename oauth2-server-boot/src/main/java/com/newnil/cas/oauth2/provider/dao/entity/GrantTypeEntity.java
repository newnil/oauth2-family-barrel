package com.newnil.cas.oauth2.provider.dao.entity;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "value", callSuper = false)
@ToString(of = "value", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "grant_type")
public class GrantTypeEntity extends AbstractPersistable<Long> {

    @Column(name = "value", nullable = false)
    private String value;

    @Singular
    @OneToMany(mappedBy = "grantType", fetch = FetchType.LAZY)
    private Set<ClientDetailsToAuthorizedGrantTypeXrefEntity> clientDetailsToAuthorizedGrantTypeXrefs;

}
