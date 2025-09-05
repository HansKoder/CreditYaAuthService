package org.pragma.creditya.r2dbc.persistence.user.mapper;

import org.pragma.creditya.model.user.User;
import org.pragma.creditya.r2dbc.helper.CustomMapper;
import org.pragma.creditya.r2dbc.persistence.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements CustomMapper<User, UserEntity> {
    @Override
    public UserEntity toData(User entity) {
        UserEntity data = UserEntity.builder()
                .username(entity.getUserName().getValue())
                .password(entity.getPassword().value())
                .lock(entity.getLock().isLock())
                .retry(entity.getRetry().cant())
                .build();

        if (entity.getId() != null) data.setUserId(entity.getId().getValue());

        return data;
    }

    @Override
    public User toEntity(UserEntity data) {
        // return User.rebuild(data.getUserId(), data.getUsername(), data.getPassword());
        return User.Builder.anUser()
                .id(data.getUserId())
                .userName(data.getUsername())
                .password(data.getPassword())
                .retry(data.getRetry())
                .lock(data.getLock())
                .build();
    }
}
