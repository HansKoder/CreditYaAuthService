package org.pragma.creditya.model.user.valueobject;

import org.pragma.creditya.model.shared.domain.model.valueobject.BaseId;
import org.pragma.creditya.model.user.exception.UserDomainException;

public class UserName extends BaseId<String> {
    public UserName(String value) {
        super(value);
        validationsPassword();
    }

    private void usernameMustBeMandatory () {
        if (getValue()==null || getValue().isBlank())
            throw new UserDomainException("Username must be mandatory");
    }

    private void validateFormatEmail () {
        if (!getValue().matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new UserDomainException("Username should have an valid email format");
    }

    private void validationsPassword () {
        usernameMustBeMandatory();
        validateFormatEmail();
    }
}
