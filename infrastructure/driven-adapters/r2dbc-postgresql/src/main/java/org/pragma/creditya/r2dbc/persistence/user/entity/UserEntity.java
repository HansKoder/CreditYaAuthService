package org.pragma.creditya.r2dbc.persistence.user.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "users", schema = "public")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column("user_id")
    private UUID userId;

    private String username;
    private String password;

    private Boolean lock;
    private Integer retry;
}
