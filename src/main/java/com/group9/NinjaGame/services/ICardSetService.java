package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardSetEntity;

import java.util.List;

public interface ICardSetService {
    void createCardSet(CardSetEntity cardSetEntity);
    List<CardSetEntity> getAllCardSets();
    CardSetEntity getById(String id);
    void deleteCardSet(CardSetEntity cardSetEntity);

}
