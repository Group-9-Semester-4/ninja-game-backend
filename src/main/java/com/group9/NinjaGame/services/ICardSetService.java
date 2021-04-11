package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardSet;
import javassist.NotFoundException;

public interface ICardSetService {
    CardSet createCardSet(CardSet cardSet);
    CardSet getById(String id) throws NotFoundException;
    void deleteCardSet(CardSet cardSet);
    Iterable<CardSet> findAll();

}
