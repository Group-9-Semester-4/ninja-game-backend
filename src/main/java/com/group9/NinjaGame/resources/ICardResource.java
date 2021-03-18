package com.group9.NinjaGame.resources;

import com.group9.NinjaGame.models.Card;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ICardResource {
    Card getCardById(@PathVariable String id);

    List<Card> getAll();

}
