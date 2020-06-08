SELECT 'CREATE DATABASE interview'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'interview');
CREATE USER candidate WITH PASSWORD 'java';
GRANT ALL PRIVILEGES ON DATABASE interview to candidate;
\connect interview

-- create sequence user_id_seq start 1;

CREATE TABLE public.users
(
    id bigint NOT NULL,
    active boolean NOT NULL,
    created_date timestamp without time zone,
    deleted boolean NOT NULL,
    last_modified_date timestamp without time zone,
    version integer NOT NULL,
    deleted_token character varying(255),
    email character varying(100),
    password character varying(100),
    username character varying(100),
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT ukep5x3xlagcmac02hu6emgpv84 UNIQUE (email, deleted_token),
    CONSTRAINT ukitid1lve77468p9m5ni0lfpej UNIQUE (username, deleted_token)
)

    TABLESPACE pg_default;


ALTER TABLE users
    OWNER to candidate;
