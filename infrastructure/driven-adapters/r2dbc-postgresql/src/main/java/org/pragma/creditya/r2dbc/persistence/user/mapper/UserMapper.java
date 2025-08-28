package org.pragma.creditya.r2dbc.persistence.user.mapper;

import org.pragma.creditya.model.user.User;
import org.pragma.creditya.r2dbc.helper.CustomMapper;
import org.pragma.creditya.r2dbc.persistence.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements CustomMapper<User, UserEntity> {
    @Override
    public UserEntity toData(User entity) {

        UserEntity data = new UserEntity();
        data.setUsername(entity.getId().getValue());
        data.setPassword(entity.getPassword().value());

        return data;
    }

    @Override
    public User toEntity(UserEntity data) {
        return User.create(data.getUsername(), data.getPassword());
    }
}
