SET @randomUUID = uuid();

INSERT INTO card_sets VALUES(uuid_to_bin(@randomUUID), 'All cards', 60, 1, 1, 1);

INSERT INTO card_set_cards SELECT id,uuid_to_bin(@randomUUID) FROM cards;