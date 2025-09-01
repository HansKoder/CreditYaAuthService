package org.pragma.creditya.model.user;


import lombok.Getter;
import org.pragma.creditya.model.shared.model.entity.AggregateRoot;
import org.pragma.creditya.model.user.valueobject.Password;
import org.pragma.creditya.model.user.valueobject.UserId;
import org.pragma.creditya.model.user.valueobject.UserName;

import java.util.UUID;

@Getter
public class User extends AggregateRoot<UserId> {
    private final UserName userName;
    private final Password password;

    private User(UserId userId, UserName userName, Password password) {
        setId(userId);
        this.userName = userName;
        this.password = password;
    }

    public static User create(String username, String password) {
        return new User(null, new UserName(username), new Password(password));
    }

    public static User rebuild (UUID userId, String username, String password) {
        return new User(new UserId(userId), new UserName(username), new Password(password));
    }

}
