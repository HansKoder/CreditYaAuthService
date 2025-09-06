package org.pragma.creditya.security.mapper;

import org.pragma.creditya.model.user.User;

public class SecurityMapper {

    public static UserDetail toUserDetail (User entity) {
        return UserDetail.builder()
                .username(entity.getUserName().getValue())
                .password(entity.getPassword().value())
                .roles(getRoleName(entity.getRoleId().id()))
                .status(entity.getLock().isLock())
                .build();
    }


    // this mapped role name with the code, but this will be temp, in the next hu this will be removed
    // and adjusted with the role name from database
    // dev.hu3 - 09062025
    private static String getRoleName (Long id) {
        return DefaultRole.fromId(id).getName();
    }

}
