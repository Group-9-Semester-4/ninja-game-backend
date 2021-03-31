package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.entities.CardSetEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.repositories.CardSetRepository;
import org.springframework.beans.BeanUtils;
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
        for (CardEntity ce : x) {
            Card card = new Card();
            BeanUtils.copyProperties(ce, card);
            this.allCards.add(card);
        }
    }


    @Override
    public CardEntity getEntityById(String id) {
        Card card = null;
        UUID uuid = UUID.fromString(id);
        Optional<CardEntity> cardEntityOptional = repository.findById(uuid);
        CardEntity cardEntity = null;
        if (cardEntityOptional.isPresent()) {
            cardEntity = cardEntityOptional.get();
        }
        return cardEntity;
    }

    @Override
    public List<Card> getAll() {
        return this.allCards;
    }

    // for both save and update, reason why here: https://www.netsurfingzone.com/hibernate/spring-data-crudrepository-save-method/
    public void addCard(CardEntity cardEntity) {
        repository.save(cardEntity);
    }

    public void deleteCard(CardEntity cardEntity){
        repository.delete(cardEntity);
    }


}
