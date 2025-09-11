package org.pragma.creditya.api.mapper;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.api.dto.request.CreateUserRequest;
import org.pragma.creditya.api.dto.response.GetUserResponse;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserRestMapperTest {

    @Test
    void shouldMapToCommandWithSuccessful () {
        CreateUserRequest request = new CreateUserRequest("username", "password", 1L);
        CreateUserCommand cmd = UserRestMapper.toCommand(request);

        assertNotNull(cmd);
        assertEquals("username", cmd.username());
        assertEquals("password", cmd.password());
        assertEquals(1L, cmd.roleId());
    }

    @Test
    void shouldMapToResponse () {
        UUID userId = UUID.fromString("5b87a0d6-2fed-4db7-aa49-49663f719659");

        User entity = User.Builder.anUser()
                .id(userId)
                .userName("doe@gmail.com")
                .password("password")
                .build();

        GetUserResponse response = UserRestMapper.toResponse(entity);

        assertEquals("5b87a0d6-2fed-4db7-aa49-49663f719659", response.userId());
        assertEquals("doe@gmail.com", response.username());
    }

    @Test
    void shouldThrowErrorPrivateConstructor () {
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            Constructor<UserRestMapper> constructor = UserRestMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });

        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("Utility class", cause.getMessage());
    }

}
