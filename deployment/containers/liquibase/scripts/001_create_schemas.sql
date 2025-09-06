--liquibase formatted sql

--changeset dev:hu3_01
CREATE TABLE roles (
    role_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

--changeset dev:hu3_02
CREATE TABLE public.users (
    user_id uuid DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY,
    role_id BIGINT NOT NULL,
	username varchar(100) NOT NULL,
	"password" varchar(100) NOT NULL,
	"lock" bool DEFAULT false NULL,
	retry int4 NULL,
	CONSTRAINT username UNIQUE (username),
	CONSTRAINT fk_role
            FOREIGN KEY (role_id)
            REFERENCES roles(role_id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
);

--changeset dev:hu3_03
insert into roles (name) values ('ADMIN'), ('ADVISOR'), ('CUSTOMER');

--changeset dev:hu3_04
INSERT INTO users (username, password, lock, retry, role_id)
VALUES ('test@credit.com', '$2a$10$ZUBZVDbNbiell/k7TR2YX.gvd3syZuxr1z.pfSVbn9YUk27WHya.m', false, 3, 1);