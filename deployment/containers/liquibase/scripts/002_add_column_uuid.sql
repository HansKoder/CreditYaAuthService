--liquibase formatted sql

--changeset dev:auth_31082025_2300
ALTER TABLE users 
ADD COLUMN user_id UUID;

--changeset dev:auth_31082025_2301
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_pkey;

--changeset dev:auth_31082025_2302
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_pkey;

--changeset dev:auth_31082025_2303
ALTER TABLE users ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);

--changeset dev:auth_31082025_2304
ALTER TABLE users ADD CONSTRAINT username UNIQUE (username);