CREATE TABLE IF NOT EXISTS `users` (
    `id` BINARY(16) NOT NULL PRIMARY KEY,
    `email` VARCHAR(50) NOT NULL UNIQUE,
    `registered` timestamp not null,
    `last_visited` timestamp not null
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;