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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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

        User expected = User.create("doe@gmail.com", "123");

        when(repository.save(any())).thenReturn(Mono.just(persisted));
        when(mapper.toData(any())).thenReturn(persisted);
        when(mapper.toEntity(any())).thenReturn(expected);

        StepVerifier.create(repositoryAdapter.save(expected))
                .expectNextMatches(value -> value.equals(expected))
                .verifyComplete();
    }


}
