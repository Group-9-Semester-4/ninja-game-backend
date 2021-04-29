create table if not exists statistics_card_redraw (
    id BINARY(16) primary key,
    card_id BINARY(16),
    card_set_id BINARY(16),
    user_id BINARY(16),
    timestamp timestamp not null,
    index(card_id),
    index(card_set_id),
    index(user_id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

create table if not exists statistics_card_discard (
    id BINARY(16) primary key,
    card_id BINARY(16),
    card_set_id BINARY(16),
    user_id BINARY(16),
    timestamp timestamp not null,
    index(card_id),
    index(card_set_id),
    index(user_id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;