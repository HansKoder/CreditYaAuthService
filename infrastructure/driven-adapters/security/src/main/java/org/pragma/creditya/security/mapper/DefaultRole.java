package org.pragma.creditya.security.mapper;


// this mapped role name with the code, but this will be temp, in the next hu this will be removed
// and adjusted with the role name from database
// dev.hu3 - 09062025
public enum DefaultRole {
    ADMIN(1L, "ADMIN"),
    ADVISOR(2L, "ADVISOR"),
    CUSTOMER(3L, "CUSTOMER");

    private final long id;
    private final String name;

    DefaultRole(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static DefaultRole fromId(Long id) {
        if (id == null) {
            return CUSTOMER; // default
        }
        return switch (id.intValue()) {
            case 1 -> ADMIN;
            case 2 -> ADVISOR;
            case 3 -> CUSTOMER;
            default -> CUSTOMER;
        };
    }
}