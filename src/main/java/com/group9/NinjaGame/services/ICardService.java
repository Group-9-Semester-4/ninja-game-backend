package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;

import java.util.List;

public interface ICardService {
    Card getById(String uuid);

    List<Card> getAll();
    void addCard(CardEntity cardEntity);
    CardEntity getEntityById(String id);
    void deleteCard(CardEntity cardEntity);

}

