package com.group9.NinjaGame.resources;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;

import com.group9.NinjaGame.services.ICardService;
import com.group9.NinjaGame.services.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameResource implements IGameResource {

    ICardService cardService;
    IGameService gameService;

    @Autowired
    public GameResource(ICardService cardService, IGameService gameService) {
        this.cardService = cardService;
        this.gameService = gameService;
    }

    @Override
    @PostMapping(path = "/init", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Game initGame(@RequestBody ObjectNode json) {
        return gameService.initGame(json.get("timeLimit").asInt(), json.get("singlePlayer").asBoolean(), json.get("playingAlone").asBoolean());
    }

    @Override
    @GetMapping("/{uuid}/draw")
    public Card draw(@PathVariable UUID uuid) {

        return gameService.draw(uuid);
    }

    @GetMapping(path = "/{uuid}/start")
    public Game startGame(@PathVariable UUID uuid, List<Card> unwantedCards) {
        return gameService.startGame(uuid, unwantedCards);
    }

    @GetMapping(path = "/{uuid}/done")
    public List<Card> cardDone(@PathVariable UUID uuid, Card card) {
        return gameService.removeDoneCard(uuid, card);
    }

    @GetMapping(path = "/{uuid}/finish")
    public Game finishGame(@PathVariable UUID uuid) {
        return gameService.finishGame(uuid);
    }
}
