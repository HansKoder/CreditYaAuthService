package org.pragma.creditya;

import org.junit.jupiter.api.*;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.gateways.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Objects;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UserRepositoryIntegrationTest {

    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withInitScript("db/init/schema.sql");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("adapters.r2dbc.host", postgres::getHost);
        registry.add("adapters.r2dbc.username", postgres::getUsername);
        registry.add("adapters.r2dbc.password", postgres::getPassword);
        registry.add("adapters.r2dbc.database", postgres::getDatabaseName);
        registry.add("adapters.r2dbc.port", () -> postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT));
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        try (Connection conn = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword())) {

            try (Statement stmt = conn.createStatement()) {
                int count = stmt.executeUpdate("delete from users;");
                System.out.println(">>>>>>>>>>>>>>>>>>  Count users " + count);
            }

        } catch (Exception ex) {
            throw new RuntimeException("Error, detail: ", ex);
        }
    }


    @Test
    void shouldBePersistedWithSuccessful() {
        User entity = User.create("doe@gmail.com", "password");

        StepVerifier.create(userRepository.save(entity))
                .expectNextMatches(persisted -> !Objects.isNull(persisted)
                        && persisted.getId() != null
                        && persisted.getUserName() != null
                )
                .verifyComplete();
    }


    @Test
    void shouldBeFoundUserBeforeBeingPersisted() {
        User entity = User.create("doe@gmail.com", "password");

        StepVerifier.create(userRepository.save(entity))
                .expectNextMatches(persisted -> !Objects.isNull(persisted)
                        && persisted.getId() != null
                        && persisted.getUserName() != null
                )
                .verifyComplete();

        StepVerifier.create(userRepository.existUsername("doe@gmail.com"))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void shouldBeFalseUsernameIsNotPersistedBefore() {
        StepVerifier.create(userRepository.existUsername("doe@gmail.com"))
                .expectNext(Boolean.FALSE)
                .verifyComplete();
    }

}
