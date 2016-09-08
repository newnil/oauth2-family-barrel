package com.newnil.cas.oauth2.provider.dao.entity;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(of = "name", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class RoleEntity extends AbstractPersistable<Long> {

    @NonNull
    @NotNull
    @Column(name = "role_name", nullable = false, unique = true, length = 100)
    private String name;

}
