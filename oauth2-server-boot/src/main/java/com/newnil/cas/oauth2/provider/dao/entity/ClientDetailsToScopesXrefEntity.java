package com.newnil.cas.oauth2.provider.dao.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "client_details_scope_xref")
public class ClientDetailsToScopesXrefEntity extends AbstractAuditable<Long> {

    @Column(name = "auto_approve", nullable = false)
    private Boolean autoApprove;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_details_id")
    private ClientDetailsEntity clientDetails;

    @ManyToOne(optional = false)
    @JoinColumn(name = "scope_id")
    private ScopeEntity scope;

}
