package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.Card;
import javassist.NotFoundException;

import java.util.List;

public interface ICardService {
    void addCard(Card card);
    Card getEntityById(String id) throws NotFoundException;
    void deleteCard(Card card);
    Iterable<Card> findAll();
    List<Card> listAll();
}

