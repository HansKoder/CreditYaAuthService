package org.pragma.creditya.security.mapper;

import org.pragma.creditya.model.user.User;

public class SecurityMapper {

    public static UserDetail toUserDetail (User entity) {
        return UserDetail.builder()
                .username(entity.getUserName().getValue())
                .password(entity.getPassword().value())
                .roles("ADMIN")
                .status(entity.getLock().isLock())
                .build();
    }

}
