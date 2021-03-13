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
    public Card getById(String id) {
        Card card = null;
        UUID uuid = UUID.fromString(id);
        Optional<CardEntity> cardEntityOptional = repository.findById(uuid);
        if(cardEntityOptional.isPresent()) {
            CardEntity cardEntity = cardEntityOptional.get();
            card = Card.fromCardEntity(cardEntity);
        }
        return card;
    }

    @Override
    public List<Card> getAll() {
        return this.allCards;
    }

    // from CrudRepo we are getting a Iterable<CardEntity>, but we wanna work with models, so here in the service
    // the retyping is done using a new list and an iterator
    public List<Card> getAllCustom() {
        Iterable<CardEntity> cardEntities = repository.getCustomCard();
        List<Card> cards = new ArrayList<Card>();
        Iterator<CardEntity> itr = cardEntities.iterator();
        while (itr.hasNext()) {
            cards.add(Card.fromCardEntity(itr.next()));
        }
        return cards;
    }

    @Override
    public Card drawRandomCard() {
        return this.allCards.get(new Random().nextInt(this.allCards.size()));
    }

    @Override
    public List<Card> getCardsForCustomGame(List<Card> unwantedCards) {
        for (Card c : unwantedCards) {
            allCards.remove(c);
        }
        return allCards;
    }

    @Override
    public List<Card> removeDoneCard(List<Card> currentDeck, Card cardDone) {
        List<Card> newDeck = currentDeck;
        newDeck.remove(cardDone);
        return newDeck;
    }
}
