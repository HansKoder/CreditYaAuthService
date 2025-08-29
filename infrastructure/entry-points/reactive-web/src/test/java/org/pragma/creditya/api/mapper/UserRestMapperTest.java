package org.pragma.creditya.api.mapper;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.api.dto.request.CreateUserRequest;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class UserRestMapperTest {

    @Test
    void shouldMapToCommandWithSuccessful () {
        CreateUserRequest request = new CreateUserRequest("username", "password");
        CreateUserCommand cmd = UserRestMapper.toCommand(request);

        assertNotNull(cmd);
        assertEquals("username", cmd.username());
        assertEquals("password", cmd.password());
    }

    @Test
    void shouldThrowErrorPrivateConstructor () {
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            Constructor<UserRestMapper> constructor = UserRestMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });

        Throwable cause = exception.getCause();
        assertTrue(cause instanceof UnsupportedOperationException);
        assertEquals("Utility class", cause.getMessage());
    }

}
