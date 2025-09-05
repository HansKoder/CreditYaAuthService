package org.pragma.creditya.security;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.user.gateways.EncodeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EncoderProviderImpl implements EncodeProvider {

    private final Logger logger = LoggerFactory.getLogger(EncoderProviderImpl.class);
    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String pass) {
        if (pass != null)
            return passwordEncoder.encode(pass);

        logger.warn("Without encode the param is null or empty");
        return "";
    }

    @Override
    public Boolean matches(String inputPassword, String hashedPassword) {
        return passwordEncoder.matches(inputPassword, hashedPassword);
    }
}
