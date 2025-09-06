package org.pragma.creditya.r2dbc.persistence.role.adapter;

import org.pragma.creditya.model.role.Role;
import org.pragma.creditya.model.role.gateways.RoleRepository;
import org.pragma.creditya.model.shared.model.valueobject.RoleId;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.gateways.UserRepository;
import org.pragma.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.pragma.creditya.r2dbc.persistence.role.entity.RoleEntity;
import org.pragma.creditya.r2dbc.persistence.role.mapper.RoleMapper;
import org.pragma.creditya.r2dbc.persistence.role.repository.RoleReactiveRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RoleRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Long,
        RoleReactiveRepository
        > implements RoleRepository {
    public RoleRepositoryAdapter(RoleReactiveRepository repository, RoleMapper mapper) {
        super(repository, mapper, mapper::toEntity);
    }

    @Override
    public Mono<Role> findById(RoleId roleId) {
        return repository.findById(roleId.id())
                .map(this::toEntity);
    }
}
