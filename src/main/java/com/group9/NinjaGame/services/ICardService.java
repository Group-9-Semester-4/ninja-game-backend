package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ICardService {
    Card getById(String uuid);

    List<Card> getAll();

    List<Card> getAllCustom();

    Card drawRandomCard();

    List<Card> getCardsForCustomGame(List<Card> unwantedCards);

    List<Card> removeDoneCard(List<Card> currentDeck, Card cardDone);

}

