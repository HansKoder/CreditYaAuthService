package org.pragma.creditya.machine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "machine.client")
public record MachineConfigProperties (
    String id,
    String secret
) { }
