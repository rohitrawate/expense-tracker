CREATE TABLE users (
               id BIGSERIAL PRIMARY KEY,
               uuid UUID NOT NULL UNIQUE,
               first_name VARCHAR(100) NOT NULL,
               last_name VARCHAR(100) NOT NULL,
               email VARCHAR(255) NOT NULL UNIQUE,
               password VARCHAR(255) NOT NULL,
               enabled BOOLEAN NOT NULL DEFAULT TRUE,
               account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
               created_at TIMESTAMP NOT NULL,
               updated_at TIMESTAMP NOT NULL,
               version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE roles (
               id BIGSERIAL PRIMARY KEY,
               uuid UUID NOT NULL UNIQUE,
               name VARCHAR(50) UNIQUE NOT NULL,
               created_at TIMESTAMP NOT NULL,
               updated_at TIMESTAMP NOT NULL,
               version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE user_roles (
                user_id BIGINT NOT NULL,
                role_id BIGINT NOT NULL,
                PRIMARY KEY (user_id, role_id),   -- Combination

                CONSTRAINT fk_user_roles_user
                    FOREIGN KEY (user_id)
                        REFERENCES users(id),

                CONSTRAINT fk_user_roles_role
                    FOREIGN KEY (role_id)
                        REFERENCES roles(id)
);

CREATE TABLE refresh_tokens (
                id BIGSERIAL PRIMARY KEY,
                uuid UUID NOT NULL UNIQUE,
                token VARCHAR(512) NOT NULL UNIQUE,
                expiry_date TIMESTAMP NOT NULL,
                revoked BOOLEAN NOT NULL DEFAULT FALSE,
                user_id BIGINT NOT NULL,
                created_at TIMESTAMP NOT NULL,
                updated_at TIMESTAMP NOT NULL,
                version BIGINT NOT NULL DEFAULT 0,

                CONSTRAINT fk_refresh_tokens_user
                    FOREIGN KEY (user_id)   -- foreign keys still reference id (BIGINT), not uuid.
                        REFERENCES users(id)
);