package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.repositories.CardRepository;
import javassist.NotFoundException;
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
    public Card getEntityById(String id) throws NotFoundException {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<Card> cardEntityOptional = repository.findById(uuid);
            Card card = null;
            if (cardEntityOptional.isPresent()) {
                 card = cardEntityOptional.get();
            }
            else {
                throw new NotFoundException("Can't find Game with this ID");
            }
            return card;
        } catch (NotFoundException e) {
            throw e;
        }
    }

    // for both save and update, reason why here: https://www.netsurfingzone.com/hibernate/spring-data-crudrepository-save-method/
    public Card addCard(Card card) {
        try {
            repository.save(card);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return card;
    }

    public void deleteCard(Card card){
        try {
            repository.delete(card);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Card> listAll() {
        try {
            return (List<Card>) repository.findAll();
        }
        catch (Exception e) {
            throw e;
        }
    }
}
