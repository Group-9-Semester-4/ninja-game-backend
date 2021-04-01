package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardSetEntity;
import com.group9.NinjaGame.entities.GameEntity;
import com.group9.NinjaGame.repositories.CardSetRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CardSetService implements ICardSetService {

    //private CardRepository repository;
    private CardSetRepository cardSetRepository;
    private List<CardSetEntity> allCardSets;



    @Autowired
    public CardSetService(CardSetRepository cardSetRepository) {
        this.cardSetRepository = cardSetRepository;
        Iterable<CardSetEntity> x = cardSetRepository.findAll();
        this.allCardSets = new ArrayList<CardSetEntity>();
        for (CardSetEntity cs : x) {
            this.allCardSets.add(cs);
        }
    }
    public void createCardSet(CardSetEntity cardSetEntity) {
        cardSetRepository.save(cardSetEntity);
    }

    public List<CardSetEntity> getAllCardSets(){
        return this.allCardSets;
    }

    public CardSetEntity getById(String id){
        Optional<CardSetEntity> cardSetEntityOptional = cardSetRepository.findById(UUID.fromString(id));
        CardSetEntity cardSetEntity = null;
        if (cardSetEntityOptional.isPresent()) {
            cardSetEntity = cardSetEntityOptional.get();
        }
        return cardSetEntity;
    }

    public void deleteCardSet(CardSetEntity cardSetEntity){
        cardSetRepository.delete(cardSetEntity);
    }

    public Iterable<CardSetEntity> findAll() {
        return cardSetRepository.findAll();
    }

}
