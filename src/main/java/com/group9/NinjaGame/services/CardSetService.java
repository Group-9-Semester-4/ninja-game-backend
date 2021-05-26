package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.repositories.CardSetRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CardSetService implements ICardSetService {

    private final CardSetRepository cardSetRepository;

    @Autowired
    public CardSetService(CardSetRepository cardSetRepository) {
        this.cardSetRepository = cardSetRepository;
    }

    public CardSet createCardSet(CardSet cardSet) {

        try {
            cardSetRepository.save(cardSet);
        }
        catch (Exception e){
            throw e;
        }
        return cardSet;
    }

    public CardSet getById(String id) throws NotFoundException {
        try {
            Optional<CardSet> cardSetEntityOptional = cardSetRepository.findById(UUID.fromString(id));
            CardSet cardSet = null;
            if (cardSetEntityOptional.isPresent()) {
                cardSet = cardSetEntityOptional.get();
                return cardSet;
            }
            else {
                throw new NotFoundException("Can't find Card set with this ID");
            }

        }
        catch (Exception e){
            throw e;
        }

    }

    public void deleteCardSet(CardSet cardSet){
        try {
            cardSetRepository.delete(cardSet);
        }
        catch (Exception e){
            throw e;
        }
    }

    public List<CardSet> findAll() {
        try {
            return (List<CardSet>) cardSetRepository.findAll();
        }
        catch (Exception e){
            throw e;
        }
    }

}
