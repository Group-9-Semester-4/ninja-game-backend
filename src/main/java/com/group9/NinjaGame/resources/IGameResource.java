package com.group9.NinjaGame.resources;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface IGameResource {
    Game initGame(ObjectNode json);
    Card draw(UUID uuid);
    Game startGame(@PathVariable UUID gameId, @RequestBody List<Card> unwantedCards);
    List<Card> cardDone(@PathVariable UUID gameId, @RequestBody Card card);
    Game finishGame(@PathVariable UUID uuid);
}
