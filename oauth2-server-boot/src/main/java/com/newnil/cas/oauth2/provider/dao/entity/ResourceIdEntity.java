package com.newnil.cas.oauth2.provider.dao.entity;

import lombok.*;

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
@Table(name = "resource")
public class ResourceIdEntity extends AbstractAuditable<Long> {

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @OneToMany(mappedBy = "resourceId")
    @Singular
    private Set<ClientDetailsToResourceIdXrefEntity> clientDetailsToResourceIdXrefs;
}
