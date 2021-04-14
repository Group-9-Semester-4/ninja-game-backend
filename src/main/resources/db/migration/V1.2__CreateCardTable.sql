CREATE TABLE IF NOT EXISTS `cards` (
    `id` BINARY(16) NOT NULL PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `description` VARCHAR(50) NOT NULL,
    `points` INT NOT NULL,
    `has_timer` TINYINT NOT NULL,
    `difficulty` INT NOT NULL,
    `singleplayer` BOOLEAN NOT NULL DEFAULT 1,
    `filepath` VARCHAR(50) NOT NULL DEFAULT 'default.png'

)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;