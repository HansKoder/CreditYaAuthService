--liquibase formatted sql

--changeset dev:auth_05092025_0600
INSERT INTO users (username, password, lock, retry)
VALUES ('test@credit.com', '$2y$10$X0Ix./OAjN8.RgkolvdYzOmSBGuTFIsKfu6BmYFBPVMgdcNyD2sxK', false, 3);