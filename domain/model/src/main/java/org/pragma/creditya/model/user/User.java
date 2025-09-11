package org.pragma.creditya.model.user;


import lombok.Getter;
import org.pragma.creditya.model.shared.model.valueobject.RoleId;
import org.pragma.creditya.model.shared.model.entity.AggregateRoot;
import org.pragma.creditya.model.user.exception.UserDomainException;
import org.pragma.creditya.model.user.exception.UserLockedDomainException;
import org.pragma.creditya.model.user.valueobject.*;

import java.util.UUID;

@Getter
public class User extends AggregateRoot<UserId> {
    private final UserName userName;
    private final Password password;
    private RoleId roleId;
    private Retry retry;
    private Lock lock;

    public final static Integer DEFAULT_THRESHOLD = 3;

    private User (Builder builder) {
        this.userName = builder.userName;
        this.password = builder.password;
        this.retry = builder.retry;
        this.lock = builder.lock;
        this.roleId = builder.roleId;

        this.setId(builder.id);
    }

    public static User createUser (String userName, String password) {
        return Builder.anUser()
                .userName(userName)
                .password(password)
                .retry(DEFAULT_THRESHOLD)
                .lock(Boolean.FALSE)
                .build();
    }

    public void checkCreationUser() {
        checkWithoutId();
        // checkIsLocked();

    }

    public void checkIsLocked () {
        if (lock != null && lock.isLock() != null && lock.isLock())
            throw new UserLockedDomainException("User " + getNameOrAnonymous() + " is Locked, invalid any operation until new order");
    }

    public boolean shouldBeBlocked () {
        if (retry.hasAnotherRetry()) return false;

        lock = lock.enabled();
        return true;
    }

    public void loginFailed () {
        retry = retry.decrease();
    }

    private void checkWithoutId () {
        if (getId() !=null && getId().getValue() != null)
            throw new UserDomainException("User initialization must be without ID");
    }

    private String getNameOrAnonymous () {
        return userName == null || userName.getValue() == null ? "anonymous" : userName.getValue();
    }

    public void assignRole (RoleId roleId) {
        if (this.roleId != null)
            throw new UserDomainException("Role already was assigned");

        this.roleId = roleId;
    }

    public void authenticated() {
        retry = new Retry(DEFAULT_THRESHOLD);
        lock = lock.disabled();
    }

    // Custom Builder
    public static final class Builder {
        private UserId id;
        private Password password;
        private UserName userName;
        private Retry retry;
        private Lock lock;
        private RoleId roleId;

        private Builder() {
        }

        public static Builder anUser() {
            return new Builder();
        }

        public Builder password(String password) {
            this.password = new Password(password);
            return this;
        }

        public Builder userName(String userName) {
            this.userName = new UserName(userName);
            return this;
        }

        public Builder retry(Integer retry) {
            this.retry = new Retry(retry);
            return this;
        }

        public Builder lock(Boolean lock) {
            this.lock = new Lock(lock);
            return this;
        }

        public Builder id(UUID id) {
            this.id = new UserId(id);
            return this;
        }

        public Builder roleId(Long roleId) {

            this.roleId = new RoleId(roleId);
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
