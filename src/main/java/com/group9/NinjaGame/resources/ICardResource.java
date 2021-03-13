package com.group9.NinjaGame.resources;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface ICardResource {
    Card getCardById(@PathVariable String id);
    List<Card> getAll();
    List<Card> getCustom();
    Card drawRandomCard();
    List<Card> getCardsForCustomGame(@RequestBody List<Card> unwantedCards);
    List<Card> cardDone(@RequestBody List<Card> currentDeck, Card card);
}
