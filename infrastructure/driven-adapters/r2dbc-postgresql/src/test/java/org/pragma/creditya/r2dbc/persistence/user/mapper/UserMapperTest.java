package org.pragma.creditya.r2dbc.persistence.user.mapper;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.r2dbc.persistence.user.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    @Test
    void shouldBeMappedToEntity () {
        UserEntity data = new UserEntity();
        data.setUsername("doe@gmail.com");
        data.setPassword("1234");

        User entity = User.create(data.getUsername(), data.getPassword());

        assertEquals("doe@gmail.com", entity.getId().getValue());
        assertEquals("1234", entity.getPassword().value());
    }

    @Test
    void shouldBeMappedToData () {
        User entity = User.create("doe@gmail.com", "1234");

        UserEntity data = new UserEntity();
        data.setUsername(entity.getId().getValue());
        data.setPassword(entity.getPassword().value());

        assertEquals("doe@gmail.com", data.getUsername());
        assertEquals("1234", data.getPassword());
    }

}
