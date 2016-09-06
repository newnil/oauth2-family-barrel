package com.newnil.cas.oauth2.provider.dao.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "client_details_resource_xref")
public class ClientDetailsToResourceIdXrefEntity extends AbstractAuditable<Long> {


    @ManyToOne(optional = false)
    @JoinColumn(name = "client_details_id")
    private ClientDetailsEntity clientDetails;

    @ManyToOne(optional = false)
    @JoinColumn(name = "resource_id")
    private ResourceIdEntity resourceId;

}
