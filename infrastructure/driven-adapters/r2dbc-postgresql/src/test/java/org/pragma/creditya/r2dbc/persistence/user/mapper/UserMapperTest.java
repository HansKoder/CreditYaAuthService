package org.pragma.creditya.r2dbc.persistence.user.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.r2dbc.persistence.user.entity.UserEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void shouldBeMappedToEntity () {
        UUID userId = UUID.fromString("5b87a0d6-2fed-4db7-aa49-49663f719659");

        UserEntity data = UserEntity.builder()
                .userId(userId)
                .username("doe@gmail.com")
                .password("1234")
                .lock(Boolean.FALSE)
                .retry(3)
                .build();

        User entity = userMapper.toEntity(data);

        assertNotNull(entity.getId().getValue());

        assertEquals("5b87a0d6-2fed-4db7-aa49-49663f719659", entity.getId().getValue().toString());
        assertEquals("doe@gmail.com", entity.getUserName().getValue());
        assertEquals("1234", entity.getPassword().value());
    }

    @Test
    @DisplayName("Should be mapped to data, this data should have userID")
    void shouldBeMappedToData () {
        UUID userId = UUID.fromString("5b87a0d6-2fed-4db7-aa49-49663f719659");
        // User entity = User.rebuild(userId, "doe@gmail.com", "1234");
        User entity = User.Builder.anUser()
                .id(userId)
                .userName("doe@gmail.com")
                .password("1234")
                .retry(3)
                .lock(Boolean.FALSE)
                .build();

        UserEntity data = userMapper.toData(entity);

        assertEquals("5b87a0d6-2fed-4db7-aa49-49663f719659", data.getUserId().toString());
        assertEquals("doe@gmail.com", data.getUsername());
        assertEquals("1234", data.getPassword());
    }

    @Test
    @DisplayName("Should be mapped to data, this data should be without userId")
    void shouldBeMappedToDataWithoutUserId () {
        User entity = User.Builder.anUser()
                .userName("doe@gmail.com")
                .password("1234")
                .lock(Boolean.TRUE)
                .retry(3)
                .build();

        UserEntity data = userMapper.toData(entity);

        assertNull(data.getUserId());
        assertEquals("doe@gmail.com", data.getUsername());
        assertEquals("1234", data.getPassword());
    }

}
