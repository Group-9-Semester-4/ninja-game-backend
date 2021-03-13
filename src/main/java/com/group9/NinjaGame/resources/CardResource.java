package com.group9.NinjaGame.resources;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.services.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/card")
public class CardResource implements ICardResource {

    private ICardService cardService;

    @Autowired
    public CardResource(ICardService service) {
        this.cardService = service;
    }

    @GetMapping(path = "/{id}")
    public Card getCardById(@PathVariable String id) {
        return cardService.getById(id);
    }

    @GetMapping(path = "/all")
    public List<Card> getAll() {
        return cardService.getAll();
    }

    @GetMapping(path = "/custom")
    public List<Card> getCustom() {
        return cardService.getAllCustom();
    }

    @GetMapping(path="/draw")
    public Card drawRandomCard() {
        return cardService.drawRandomCard();
    }

    @GetMapping(path="/customDeck")
    public List<Card> getCardsForCustomGame(List<Card> unwantedCards) {
        return cardService.getCardsForCustomGame(unwantedCards);
    }

    // TODO: get only the current deck, not all cards
    @GetMapping(path="/done")
    public List<Card> cardDone(List<Card> currentDeck, Card card) {
        return cardService.removeDoneCard(cardService.getAll(), card);
    }

}
