package com.group9.NinjaGame.repositories;

import com.group9.NinjaGame.models.Card;

import java.util.List;
import java.util.UUID;

interface ICardRepository{
    public Card getById(UUID uuid);
    public Card getByName(String name);
    public List<Card> getAll();
}
public class CardRepository implements ICardRepository {
    @Override
    public Card getById(UUID uuid) {
        return null;
    }

    @Override
    public Card getByName(String name) {
        return null;
    }

    @Override
    public List<Card> getAll() {
        return null;
    }
}
