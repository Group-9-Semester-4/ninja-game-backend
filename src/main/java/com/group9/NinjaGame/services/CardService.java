package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

interface ICardService{
    public CardEntity getById(UUID uuid);
    public CardEntity getByName(String name);
    public List<CardEntity> getAll();
}

@Component
public class CardService implements ICardService {
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
