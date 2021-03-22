package com.group9.NinjaGame.resources.api;

import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.services.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/card")
public class CardWebResource {

    private ICardService cardService;

    @Autowired
    public CardWebResource(ICardService service) {
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
