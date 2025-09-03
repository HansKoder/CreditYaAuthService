package org.pragma.creditya.model.user;


import lombok.Getter;
import org.pragma.creditya.model.shared.model.entity.AggregateRoot;
import org.pragma.creditya.model.user.exception.UserDomainException;
import org.pragma.creditya.model.user.exception.UserLockedDomainException;
import org.pragma.creditya.model.user.valueobject.*;

import java.util.UUID;

@Getter
public class User extends AggregateRoot<UserId> {
    private final UserName userName;
    private final Password password;
    private Retry retry;
    private Lock lock;

    public final static Integer DEFAULT_THRESHOLD = 3;

    private User (Builder builder) {
        this.userName = builder.userName;
        this.password = builder.password;
        this.retry = builder.retry;
        this.lock = builder.lock;

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
        checkIsLocked();
    }

    public void checkLogin () {
        checkIsLocked();
        hasAnotherRetry();
    }

    private void lockUser () {
        lock = lock.disabled();
        throw new UserLockedDomainException("User " + getNameOrAnonymous() + " is Locked, invalid any operation until new order");
    }

    private void hasAnotherRetry () {
        if (retry.cant() == 0)
            lockUser();

        retry = retry.decrease();
    }

    private void checkWithoutId () {
        if (getId() !=null && getId().getValue() != null)
            throw new UserDomainException("User initialization must be without ID");
    }

    private String getNameOrAnonymous () {
        return userName == null || userName.getValue() == null ? "anonymous" : userName.getValue();
    }

    private void checkIsLocked () {
        if (lock != null && lock.isLock() != null && lock.isLock())
            throw new UserLockedDomainException("User " + getNameOrAnonymous() + " is Locked, invalid any operation until new order");
    }



    // Custom Builder
    public static final class Builder {
        private UserId id;
        private Password password;
        private UserName userName;
        private Retry retry;
        private Lock lock;

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

        public User build() {
            return new User(this);
        }
    }
}
