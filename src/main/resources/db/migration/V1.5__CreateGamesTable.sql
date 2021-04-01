CREATE TABLE IF NOT EXISTS `games` (
        `id` BINARY(16) NOT NULL PRIMARY KEY,
        `card_set_id` BINARY(16),
        `points` INT NOT NULL DEFAULT 0,
        `time_limit` INT NOT NULL DEFAULT 0,
        `cards_done` INT NOT NULL DEFAULT 0,
        `minigame_attempts` INT NOT NULL DEFAULT 0,
        `singleplayer` BOOLEAN NOT NULL,
        `playing_alone` BOOLEAN NOT NULL,
        FOREIGN KEY (card_set_id) REFERENCES card_sets (id) ON DELETE NO ACTION)
        ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;