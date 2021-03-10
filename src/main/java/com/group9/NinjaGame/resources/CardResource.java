package com.group9.NinjaGame.resources;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/card")
public class CardResource {
    private CardService cardService;
    @Autowired
    public CardResource(CardService service) {
        this.cardService = service;
    }

    @GetMapping(path = "/{name}")
    public CardEntity getCardByName(@PathVariable String name) {
        return cardService.getByName(name);
    }
}
