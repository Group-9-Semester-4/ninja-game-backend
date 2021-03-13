package com.group9.NinjaGame.resources;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.services.CardService;
import com.group9.NinjaGame.services.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/card")
public class CardResource implements ICardResource {

    private CardService cardService;

    @Autowired
    public CardResource(CardService service) {
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




}
