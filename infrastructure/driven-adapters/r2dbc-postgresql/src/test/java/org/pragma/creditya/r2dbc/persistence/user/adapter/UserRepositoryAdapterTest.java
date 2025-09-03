package org.pragma.creditya.r2dbc.persistence.user.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.r2dbc.persistence.user.entity.UserEntity;
import org.pragma.creditya.r2dbc.persistence.user.mapper.UserMapper;
import org.pragma.creditya.r2dbc.persistence.user.repository.UserReactiveRepository;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryAdapterTest {

    @InjectMocks
    UserRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    UserMapper mapper;

    @BeforeEach
    void setup () {
        repository = Mockito.mock(UserReactiveRepository.class);
        mapper = Mockito.mock(UserMapper.class);

        repositoryAdapter = new UserRepositoryAdapter(repository, mapper);
    }

    @Test
    void mustSaveValue() {
        UserEntity persisted = new UserEntity();
        persisted.setUsername("doe@gmail.com");
        persisted.setPassword("123");

        // User expected = User.create("doe@gmail.com", "123");
        User expected = User.Builder.anUser()
                .userName("doe@gmail.com")
                .password("123")
                .build();

        when(repository.save(any())).thenReturn(Mono.just(persisted));
        when(mapper.toData(any())).thenReturn(persisted);
        when(mapper.toEntity(any())).thenReturn(expected);

        StepVerifier.create(repositoryAdapter.save(expected))
                .expectNextMatches(value -> value.equals(expected))
                .verifyComplete();
    }

    @Test
    void mustFindValueById() {
        UUID userId = UUID.fromString("5b87a0d6-2fed-4db7-aa49-49663f719659");

        UserEntity persisted = new UserEntity();
        persisted.setUserId(userId);
        persisted.setUsername("doe@gmail.com");
        persisted.setPassword("123");

        // User expected = User.create("doe@gmail.com", "123");
        User expected = User.Builder.anUser()
                .userName("doe@gmail.com")
                .password("123")
                .build();


        when(repository.findById(userId)).thenReturn(Mono.just(persisted));
        when(mapper.toEntity(any())).thenReturn(expected);

        Mono<User> result = repositoryAdapter.findById(userId);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(expected))
                .verifyComplete();
    }


    @Test
    void shouldReturnTrueWhenUserWasPersistedBefore() {
        when(repository.exists(any(Example.class)))
                .thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(repositoryAdapter.existUsername("doe@gmail.com"))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }



}
