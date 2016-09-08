package com.newnil.cas.oauth2.provider.dao.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(of = "username", callSuper = false)
@ToString(exclude = "password", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity extends AbstractAuditable<Long> {

    @NonNull
    @NotNull
    @Size(max = 50)
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @NonNull
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Singular
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRoleXRef> roles;
}
