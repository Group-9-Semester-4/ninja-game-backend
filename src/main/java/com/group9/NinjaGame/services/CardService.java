package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
class CardService implements ICardService {

    private CardRepository repository;
    private List<CardEntity> allCards;

    @Autowired
    public CardService(CardRepository repository) {
        this.repository = repository;
        Iterable<CardEntity> x = repository.findAll();
        this.allCards = new ArrayList<CardEntity>();
        x.forEach(allCards::add);
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

    @Override
    public CardEntity drawRandomCard() {
        return this.allCards.get(new Random().nextInt(this.allCards.size()));
    }
}
