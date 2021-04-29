ALTER TABLE `games` ADD COLUMN `user_id` BINARY(16);

ALTER TABLE `games` ADD FOREIGN KEY (user_id) REFERENCES users (id) on delete cascade