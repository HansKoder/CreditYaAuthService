

CREATE TABLE public.users (
	username varchar(100) NOT NULL,
	"password" varchar(100) NOT NULL,
	user_id uuid DEFAULT gen_random_uuid() NOT NULL,
	"lock" bool DEFAULT false NULL,
	retry int4 NULL,
	CONSTRAINT username UNIQUE (username),
	CONSTRAINT users_pkey PRIMARY KEY (user_id)
);