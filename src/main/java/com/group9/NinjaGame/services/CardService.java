package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.repositories.ICardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
class CardService implements ICardService {

    private ICardRepository cardRepository;
    @Autowired
    public CardService(ICardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }


    @Override
    public CardEntity getById(String id) {
        UUID uuid = UUID.fromString(id);
        return cardRepository.getById(uuid);
    }

    @Override
    public CardEntity getByName(String name) {
        return cardRepository.getByName(name);
    }

    @Override
    public List<CardEntity> getAll() {
        return cardRepository.getAll();
    }
}
