package org.pragma.creditya.model.user;


import lombok.Getter;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;
import org.pragma.creditya.model.user.valueobject.Password;
import org.pragma.creditya.model.user.valueobject.UserName;

@Getter
public class User extends AggregateRoot<UserName> {
    private final Password password;

    private User(UserName userName, Password password) {
        setId(userName);
        this.password = password;
    }

    public static User create(String username, String password) {
        return new User(new UserName(username), new Password(password));
    }

}
