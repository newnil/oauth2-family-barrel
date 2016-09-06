package com.newnil.cas.oauth2.provider.dao.entity;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "scope")
public class ScopeEntity extends AbstractPersistable<Long> {

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @OneToMany(mappedBy = "scope")
    @Singular
    private Set<ClientDetailsToScopesXrefEntity> clientDetailsToScopesXrefs;

}
