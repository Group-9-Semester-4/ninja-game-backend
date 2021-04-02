package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.ListUtils;

import java.util.*;

@Component
public class CardService implements ICardService {

    private CardRepository repository;

    @Autowired
    public CardService(CardRepository repository) {
        this.repository = repository;
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

    // for both save and update, reason why here: https://www.netsurfingzone.com/hibernate/spring-data-crudrepository-save-method/
    public void addCard(CardEntity cardEntity) {
        repository.save(cardEntity);
    }

    public void deleteCard(CardEntity cardEntity){
        repository.delete(cardEntity);
    }

    public Iterable<CardEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public List<CardEntity> listAll() {
        return (List<CardEntity>) ListUtils.toList(findAll());
    }
}
