CREATE TABLE IF NOT EXISTS `users` (
    `id` BINARY(16) NOT NULL PRIMARY KEY,
    `email` VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;