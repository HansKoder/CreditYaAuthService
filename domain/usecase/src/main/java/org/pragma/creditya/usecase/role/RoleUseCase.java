package org.pragma.creditya.usecase.role;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.role.Role;
import org.pragma.creditya.model.role.exception.RoleIsNotFoundDomainException;
import org.pragma.creditya.model.role.gateways.RoleRepository;
import org.pragma.creditya.model.shared.model.valueobject.RoleId;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoleUseCase implements IRoleUseCase{

    private final RoleRepository roleRepository;

    @Override
    public Mono<Role> checkRole(Long roleId) {
        return Mono.fromCallable(() -> new RoleId(roleId))
                .flatMap(roleRepository::findById)
                .switchIfEmpty(Mono.error(new RoleIsNotFoundDomainException("Role Id: 1 Is not found")));
    }
}
