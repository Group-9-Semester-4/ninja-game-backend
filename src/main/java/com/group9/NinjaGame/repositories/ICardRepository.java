package com.group9.NinjaGame.repositories;
import com.group9.NinjaGame.entities.CardEntity;

import java.util.List;
import java.util.UUID;

public interface ICardRepository{
    public CardEntity getById(UUID uuid);
    public CardEntity getByName(String name);
    public List<CardEntity> getAll();
}


