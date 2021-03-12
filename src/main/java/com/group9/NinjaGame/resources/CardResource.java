package com.group9.NinjaGame.resources;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.services.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/card")
public class CardResource implements ICardResource {

    private ICardService cardService;

    @Autowired
    public CardResource(ICardService service) {
        this.cardService = service;
    }

    @GetMapping(path = "/{id}")
    public Optional<CardEntity> getCardById(@PathVariable String id) {
        return cardService.getById(id);
    }

    @GetMapping(path = "/all")
    public List<Card> getAll() {
        return cardService.getAll();
    }

    @GetMapping(path = "/custom")
    public Iterable<CardEntity> getCustom() {
        return cardService.getAllCustom();
    }

    @GetMapping(path="/draw")
    public Card drawRandomCard() {
        return cardService.drawRandomCard();
    }
}
