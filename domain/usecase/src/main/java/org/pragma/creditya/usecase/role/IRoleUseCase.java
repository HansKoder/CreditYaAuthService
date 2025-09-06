package org.pragma.creditya.usecase.role;

import org.pragma.creditya.model.role.Role;
import org.pragma.creditya.model.shared.model.valueobject.RoleId;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IRoleUseCase {

    Mono<Role> checkRole (Long roleId);

}
