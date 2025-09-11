package org.pragma.creditya.r2dbc.persistence.role.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "roles", schema = "public")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {

    @Id
    @Column("role_id")
    private Long roleId;
    private String name;
}
