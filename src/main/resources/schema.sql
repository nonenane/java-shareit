CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  varchar(255)                            NOT NULL,
    email varchar(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT users_email_unique UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    owner_id    BIGINT                                  NOT NULL REFERENCES users (id),
    name        VARCHAR(255)                            NOT NULL,
    description VARCHAR(1024)                           NOT NULL,
    available   BIT                                     NOT NULL,
    CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bookings (
                                        booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
                                        start_time timestamp,
                                        end_time timestamp,
                                        item_id BIGINT REFERENCES items (id) ON DELETE CASCADE,
                                        booker_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
                                        status VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS comments (
                                        comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
                                        text VARCHAR(512),
                                        item_id BIGINT REFERENCES items (id) ON DELETE CASCADE,
                                        author_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
                                        created timestamp
);