package com.group9.NinjaGame.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;

import com.group9.NinjaGame.services.ICardService;
import com.group9.NinjaGame.services.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Override
    @PostMapping(path = "/{uuid}/start")
    public Game startGame(@PathVariable UUID uuid, @RequestBody List<String> json) {
        List<UUID> listUUIDs = json.stream().map(s -> UUID.fromString(s)).collect(Collectors.toList());
        return gameService.startGame(uuid, listUUIDs);
    }

    @Override
    @PostMapping(path = "/{uuid}/done")
    public List<Card> cardDone(@PathVariable UUID uuid, @RequestBody ObjectNode json) {
        return gameService.removeDoneCard(uuid,
                UUID.fromString(json.get("cardId").asText()));
    }

    @GetMapping(path = "/{uuid}/finish")
    public Game finishGame(@PathVariable UUID uuid) {
        return gameService.finishGame(uuid);
    }
}
