package org.pragma.creditya.model.role.gateways;

import org.pragma.creditya.model.role.Role;
import org.pragma.creditya.model.shared.model.valueobject.RoleId;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> findById (RoleId roleId);
}
