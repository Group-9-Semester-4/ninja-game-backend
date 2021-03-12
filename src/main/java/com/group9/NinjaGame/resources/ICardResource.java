package com.group9.NinjaGame.resources;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface ICardResource {
    Optional<CardEntity> getCardById(@PathVariable String id);
    List<Card> getAll();
    Iterable<CardEntity> getCustom();
    Card drawRandomCard();
}
