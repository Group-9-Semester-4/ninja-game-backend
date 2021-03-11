package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
class CardService implements ICardService {

    private CardRepository repository;

    @Autowired
    public CardService(CardRepository repository) {
        this.repository = repository;
    }


    @Override
    public Optional<CardEntity> getById(String id) {
        UUID uuid = UUID.fromString(id);
        return repository.findById(uuid);
    }

    @Override
    public Iterable<CardEntity> getAll() {
        return repository.findAll();
    }

    public Iterable<CardEntity> getAllCustom() {
        return repository.getCustomCard();
    }
}
