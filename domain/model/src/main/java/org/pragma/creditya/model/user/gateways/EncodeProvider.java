package org.pragma.creditya.model.user.gateways;

import reactor.core.publisher.Mono;

public interface EncodeProvider {
    String encode (String pass);
    Boolean matches (String inputPassword, String hashedPassword);
}
