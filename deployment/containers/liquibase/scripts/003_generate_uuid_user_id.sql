--liquibase formatted sql

--changeset dev:auth_31082025_2330
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

--changeset dev:auth_31082025_2331
ALTER TABLE public.users
    ALTER COLUMN user_id SET DEFAULT gen_random_uuid();
