package com.newnil.cas.oauth2.provider.dao.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "redirect_uri")
public class RedirectUriEntity extends AbstractAuditable<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_details_id", nullable = false)
    private ClientDetailsEntity clientDetails;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

}
