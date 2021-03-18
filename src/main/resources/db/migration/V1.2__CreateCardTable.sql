CREATE TABLE IF NOT EXISTS `cards` (
    `id` BINARY(16) NOT NULL PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `description` VARCHAR(50) NOT NULL,
    `points` INT NOT NULL,
    `difficulty_type` BOOLEAN NOT NULL,
    `difficulty` INT NOT NULL,
    # difficulty_type -> boolean for difficulty type, 0 means difficulty is stored in seconds
    # 1 means difficulty stored in # of repetitions
    `singleplayer` BOOLEAN NOT NULL DEFAULT 1,
    `filepath` VARCHAR(50) NOT NULL DEFAULT 'default.png'

)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;