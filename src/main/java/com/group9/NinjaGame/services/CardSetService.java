package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.repositories.CardSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CardSetService implements ICardSetService {

    private final CardSetRepository cardSetRepository;

    @Autowired
    public CardSetService(CardSetRepository cardSetRepository) {
        this.cardSetRepository = cardSetRepository;
    }
    public void createCardSet(CardSet cardSet) {
        cardSetRepository.save(cardSet);
    }

    public CardSet getById(String id){
        Optional<CardSet> cardSetEntityOptional = cardSetRepository.findById(UUID.fromString(id));
        CardSet cardSet = null;
        if (cardSetEntityOptional.isPresent()) {
            cardSet = cardSetEntityOptional.get();
        }
        return cardSet;
    }

    public void deleteCardSet(CardSet cardSet){
        cardSetRepository.delete(cardSet);
    }

    public Iterable<CardSet> findAll() {
        return cardSetRepository.findAll();
    }

}
