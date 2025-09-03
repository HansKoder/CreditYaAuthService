CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    username VARCHAR(100) UNIQUE,
    password VARCHAR(100) NOT NULL
);

ALTER TABLE public.users
    ALTER COLUMN user_id SET DEFAULT gen_random_uuid();