package org.pragma.creditya.r2dbc.persistence.user.adapter;

import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.gateways.UserRepository;
import org.pragma.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.pragma.creditya.r2dbc.persistence.user.entity.UserEntity;
import org.pragma.creditya.r2dbc.persistence.user.mapper.UserMapper;
import org.pragma.creditya.r2dbc.persistence.user.repository.UserReactiveRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        String,
        UserReactiveRepository
        > implements UserRepository {
    public UserRepositoryAdapter(UserReactiveRepository repository, UserMapper mapper) {
        super(repository, mapper, mapper::toEntity);
    }
}
