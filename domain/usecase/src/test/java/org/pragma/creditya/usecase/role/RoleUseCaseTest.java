package org.pragma.creditya.usecase.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.pragma.creditya.model.role.Role;
import org.pragma.creditya.model.role.exception.RoleDomainException;
import org.pragma.creditya.model.role.exception.RoleIsNotFoundDomainException;
import org.pragma.creditya.model.role.gateways.RoleRepository;
import org.pragma.creditya.model.shared.model.valueobject.RoleId;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


public class RoleUseCaseTest {

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private RoleUseCase roleUseCase;

    @BeforeEach
    void setup () {
        repository = Mockito.mock(RoleRepository.class);
        roleUseCase = new RoleUseCase(repository);
    }

    @Test
    void shouldThrowException_WhenRoleIdIsNull () {
        var resp = roleUseCase.checkRole(null);
        StepVerifier.create(resp)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Role Id must be mandatory", throwable.getMessage());
                    assertInstanceOf(RoleDomainException.class, throwable);
                }).verify();

        verify(repository, never()).findById(any());
    }

    @Test
    void shouldThrowException_WhenRoleIsNotFound () {
        Mockito.when(repository.findById(new RoleId(1L)))
                .thenReturn(Mono.empty());

        var resp = roleUseCase.checkRole(1L);
        StepVerifier.create(resp)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Role Id: 1 Is not found", throwable.getMessage());
                    assertInstanceOf(RoleIsNotFoundDomainException.class, throwable);
                }).verify();

        verify(repository, Mockito.times(1)).findById(any());
    }

    @Test
    void shouldBeFound () {
        Role role = Role.RoleBuilder.aRole()
                .id(1L)
                .name("Customer")
                .build();

        Mockito.when(repository.findById(new RoleId(1L)))
                .thenReturn(Mono.just(role));

        var resp = roleUseCase.checkRole(1L);
        StepVerifier.create(resp)
                .expectNext(role)
                        .verifyComplete();

        verify(repository, Mockito.times(1)).findById(any());
    }
}
