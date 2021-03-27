package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.entities.CardSetEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.repositories.CardSetRepository;
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

    // wont be used later probably, just to give an idea how it works
    // lag machine
    @Override
    public Card getById(String id) {
        Card card = null;
        UUID uuid = UUID.fromString(id);
        Optional<CardEntity> cardEntityOptional = repository.findById(uuid);
        if (cardEntityOptional.isPresent()) {
            CardEntity cardEntity = cardEntityOptional.get();
            card = Card.fromCardEntity(cardEntity);
        }
        return card;
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

    // helper method to convert Iterable<CardEntity> into List<Card>
    public List<Card> fromIterator(Iterable<CardEntity> cardEntities) {
        List<Card> cards = new ArrayList<Card>();
        Iterator<CardEntity> itr = cardEntities.iterator();
        while (itr.hasNext()) {
            cards.add(Card.fromCardEntity(itr.next()));
        }
        return cards;
    }

    // for both save and update, reason why here: https://www.netsurfingzone.com/hibernate/spring-data-crudrepository-save-method/
    public void addCard(CardEntity cardEntity) {
        repository.save(cardEntity);
    }

    public void deleteCard(CardEntity cardEntity){
        repository.delete(cardEntity);
    }


}
