create table if not exists statistics_cardset_completion (
    id BINARY(16) primary key,
    game_id BINARY(16),
    card_set_id BINARY(16),
    percentage tinyint,
    timestamp timestamp not null,
    index(game_id),
    index(card_set_id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

create table if not exists statistics_time_played_per_game (
    id BINARY(16) primary key,
    game_id BINARY(16),
    card_set_id BINARY(16),
    time_in_seconds smallint,
    timestamp timestamp not null,
    index(game_id),
    index(card_set_id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;