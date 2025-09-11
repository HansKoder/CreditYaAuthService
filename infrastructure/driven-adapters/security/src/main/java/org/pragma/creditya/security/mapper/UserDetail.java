package org.pragma.creditya.security.mapper;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Stream;

@Builder
@Getter
public class UserDetail implements UserDetails {

    private String username;
    private String password;
    private boolean status;
    private String roles;

    @Override
    public boolean isAccountNonExpired() {
        return status;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(roles.split(", ")).map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "password=***" +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", roles='" + roles + '\'' +
                '}';
    }
}
