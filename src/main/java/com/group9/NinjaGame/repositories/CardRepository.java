package com.group9.NinjaGame.repositories;

import com.group9.NinjaGame.entities.CardEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
class CardRepository implements ICardRepository {
    @Override
    public CardEntity getById(UUID uuid) {
        return null;
    }

    @Override
    public CardEntity getByName(String name) {
        return null;
    }

    @Override
    public List<CardEntity> getAll() {
        return null;
    }
}
