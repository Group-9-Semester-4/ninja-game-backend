CREATE TABLE IF NOT EXISTS `card_sets` (
    `id` BINARY(16) NOT NULL PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `complete_time_limit` INT NOT NULL,
    `difficulty` INT NOT NULL,
    `multiplayer_suitable` BOOLEAN NOT NULL DEFAULT 1,
    `temporary` BOOLEAN not null default 0)
    ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS `card_set_cards` (
    `card_id` BINARY(16) NOT NULL,
    `card_set_id` BINARY(16) NOT NULL,
    FOREIGN KEY (card_id) REFERENCES cards (id) ON DELETE CASCADE,
    FOREIGN KEY (card_set_id) REFERENCES card_sets (id) ON DELETE CASCADE
    )
    ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;