package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CardService implements ICardService {

    private CardRepository repository;

    @Autowired
    public CardService(CardRepository repository) {
        this.repository = repository;
    }


    @Override
    public Card getEntityById(String id) {
        UUID uuid = UUID.fromString(id);
        Optional<Card> cardEntityOptional = repository.findById(uuid);
        Card card = null;
        if (cardEntityOptional.isPresent()) {
            card = cardEntityOptional.get();
        }
        return card;
    }

    // for both save and update, reason why here: https://www.netsurfingzone.com/hibernate/spring-data-crudrepository-save-method/
    public void addCard(Card card) {
        repository.save(card);
    }

    public void deleteCard(Card card){
        repository.delete(card);
    }

    public Iterable<Card> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Card> listAll() {
        return (List<Card>) findAll();
    }
}
