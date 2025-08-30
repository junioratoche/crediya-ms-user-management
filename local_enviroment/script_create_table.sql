CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE public.roles (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100)  NOT NULL UNIQUE,
    description VARCHAR(50)
);

CREATE TABLE public.users (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name       VARCHAR(100) NOT NULL,
    last_name        VARCHAR(100) NOT NULL,
    email            VARCHAR(100) NOT NULL,
    document_number  VARCHAR(50),
    phone_number     VARCHAR(30),
    role_id          UUID NOT NULL,
    salary           NUMERIC(15,2) NOT NULL CHECK (salary >= 0),

    -- FK
    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id)
        REFERENCES public.roles (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);