package org.pragma.creditya.usecase.machine.command;

public record AuthenticationMachineCommand(
        String clientId,
        String clientSecret
) { }
