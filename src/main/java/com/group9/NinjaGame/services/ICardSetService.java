package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardSet;

public interface ICardSetService {
    void createCardSet(CardSet cardSet);
    CardSet getById(String id);
    void deleteCardSet(CardSet cardSet);
    Iterable<CardSet> findAll();

}
