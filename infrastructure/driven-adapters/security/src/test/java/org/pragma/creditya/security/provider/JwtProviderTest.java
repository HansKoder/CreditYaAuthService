package org.pragma.creditya.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.security.exception.SecurityInfraException;
import org.pragma.creditya.security.jwt.provider.JwtProvider;
import org.pragma.creditya.security.mapper.UserDetail;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JwtProviderTest {

    private JwtProvider jwtProvider;

    private final UserDetail USER_DETAIL = UserDetail.builder()
            .username("doe@gmail.com")
            .password("***")
            .roles("ROLE_CUSTOMER")
            .build();

    @BeforeEach
    void setUp() throws Exception {
        jwtProvider = new JwtProvider();
        setupEnvs(jwtProvider);
    }

    private void setFinalField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void setupEnvs (JwtProvider jwtProvider) throws Exception {
        String secret = "mySuperSecretKey123456789012345678901234567890"; // >= 32 chars
        int expiration = 3600000; // 1h

        ReflectionTestUtils.setField(jwtProvider, "SECRET_KEY", secret);
        ReflectionTestUtils.setField(jwtProvider, "EXPIRATION", expiration);

        SecretKey key = new SecretKeySpec("01234567890123456789012345678901"
                .getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        setFinalField(jwtProvider, "KEY", key);
    }


    @Test
    void generateToken_success() {
        String token = jwtProvider.generateToken(USER_DETAIL);

        assertNotNull(token);
        assertTrue(token.length() > 10);
    }

    @Test
    void generateToken_error_userDetailsIsNull() {
        SecurityInfraException ex = assertThrows(SecurityInfraException.class, () -> jwtProvider.generateToken(null));
        assertEquals("[infra.security] user detail is mandatory", ex.getMessage());
    }

    @Test
    void getClaims_success() {
        String token = jwtProvider.generateToken(USER_DETAIL);
        Claims claims = jwtProvider.getClaims(token);

        assertEquals("doe@gmail.com", claims.getSubject());
        assertInstanceOf(List.class, claims.get("roles"));

        @SuppressWarnings("unchecked")
        var roles = (List<AuthorityGranter>) claims.get("roles");

        assertFalse(roles.isEmpty());
        assertEquals(1, roles.size());

        assertInstanceOf(Map.class, roles.getFirst());

        @SuppressWarnings("unchecked")
        Map<String, Object> roleMap = (Map<String, Object>) roles.get(0);
        assertEquals("ROLE_CUSTOMER", roleMap.get("authority"));
    }

    @Test
    void getClaims_error_invalidToken() {
        String fakeToken = "invalid.token.string";
        assertThrows(JwtException.class, () -> jwtProvider.getClaims(fakeToken));
    }

    @Test
    void validate_success() {
        String token = jwtProvider.generateToken(USER_DETAIL);
        assertTrue(jwtProvider.validate(token));
    }

    @Test
    void validate_error_invalidToken() {
        String fakeToken = "invalid.token.string";
        assertFalse(jwtProvider.validate(fakeToken));
    }

}
