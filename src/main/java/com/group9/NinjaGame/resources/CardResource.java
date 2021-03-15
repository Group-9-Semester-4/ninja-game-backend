package com.group9.NinjaGame.resources;

import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.services.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
