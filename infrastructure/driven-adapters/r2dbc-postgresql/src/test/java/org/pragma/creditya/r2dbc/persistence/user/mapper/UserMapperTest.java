package org.pragma.creditya.r2dbc.persistence.user.mapper;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.r2dbc.persistence.user.entity.UserEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMapperTest {

    private UserMapper userMapper = new UserMapper();

    @Test
    void shouldBeMappedToEntity () {
        UserEntity data = new UserEntity();
        data.setUsername("doe@gmail.com");
        data.setPassword("1234");

        User entity = userMapper.toEntity(data);

        assertNotNull(entity.getId());
        assertEquals("doe@gmail.com", entity.getUserName().getValue());
        assertEquals("1234", entity.getPassword().value());
    }

    @Test
    void shouldBeMappedToData () {
        UUID userId = UUID.fromString("5b87a0d6-2fed-4db7-aa49-49663f719659");
        User entity = User.rebuild(userId, "doe@gmail.com", "1234");

        UserEntity data = userMapper.toData(entity);

        assertEquals("5b87a0d6-2fed-4db7-aa49-49663f719659", data.getUserId().toString());
        assertEquals("doe@gmail.com", data.getUsername());
        assertEquals("1234", data.getPassword());
    }

}
