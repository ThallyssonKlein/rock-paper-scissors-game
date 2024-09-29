CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE game (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    next_move VARCHAR(50),
    status VARCHAR(50),
    owner_id BIGINT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES user (id)
);

CREATE TABLE result (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    result VARCHAR(4) NOT NULL,
    player_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    CONSTRAINT fk_player FOREIGN KEY (player_id) REFERENCES user(id),
    CONSTRAINT fk_game FOREIGN KEY (game_id) REFERENCES game(id)
);

CREATE TABLE movement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    value VARCHAR(8) NOT NULL,
    salt VARCHAR(32) NOT NULL,
    hash VARCHAR(64) NOT NULL,
    player_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES user (id),
    FOREIGN KEY (game_id) REFERENCES game (id)
);

INSERT INTO user (name, password) VALUES ('server_player', 'secure_password');

INSERT INTO user (name, password) VALUES ('test_player', 'secure_password');