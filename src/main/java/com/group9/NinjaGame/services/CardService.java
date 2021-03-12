package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CardService implements ICardService {

    private CardRepository repository;
    private List<Card> allCards;

    @Autowired
    public CardService(CardRepository repository) {
        this.repository = repository;
        Iterable<CardEntity> x = repository.findAll();
        this.allCards = new ArrayList<Card>();
        for (CardEntity c : x) {
            this.allCards.add(Card.fromCardEntity(c));
        }
    }


    @Override
    public Optional<CardEntity> getById(String id) {
        UUID uuid = UUID.fromString(id);
        return repository.findById(uuid);
    }

    @Override
    public List<Card> getAll() {
        return this.allCards;
    }

    public Iterable<CardEntity> getAllCustom() {
        return repository.getCustomCard();
    }

    @Override
    public Card drawRandomCard() {
        return this.allCards.get(new Random().nextInt(this.allCards.size()));
    }
}
