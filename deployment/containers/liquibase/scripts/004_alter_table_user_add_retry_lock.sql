--liquibase formatted sql

--changeset dev:auth_05092025_0500
ALTER TABLE users 
ADD COLUMN lock BOOLEAN DEFAULT FALSE;

ALTER TABLE users 
ADD COLUMN retry INTEGER;
