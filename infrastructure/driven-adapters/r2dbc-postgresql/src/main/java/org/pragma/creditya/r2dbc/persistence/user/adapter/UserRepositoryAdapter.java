package org.pragma.creditya.r2dbc.persistence.user.adapter;

import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.gateways.UserRepository;
import org.pragma.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.pragma.creditya.r2dbc.persistence.user.entity.UserEntity;
import org.pragma.creditya.r2dbc.persistence.user.mapper.UserMapper;
import org.pragma.creditya.r2dbc.persistence.user.repository.UserReactiveRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        UUID,
        UserReactiveRepository
        > implements UserRepository {
    public UserRepositoryAdapter(UserReactiveRepository repository, UserMapper mapper) {
        super(repository, mapper, mapper::toEntity);
    }

    @Override
    public Mono<Boolean> existUsername(String username) {
        UserEntity filter = new UserEntity();
        filter.setUsername(username);

        return repository.exists(Example.of(filter));
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return null;
    }
}
