package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.Card;
import javassist.NotFoundException;

import java.util.List;

public interface ICardService {
    Card addCard(Card card);
    Card getEntityById(String id) throws NotFoundException;
    void deleteCard(Card card);
    List<Card> listAll();
}

