package com.group9.NinjaGame.resources.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.entities.CardSetEntity;
import com.group9.NinjaGame.entities.GameEntity;
import com.group9.NinjaGame.models.Game;
import com.group9.NinjaGame.services.ICardService;
import com.group9.NinjaGame.services.ICardSetService;
import com.group9.NinjaGame.services.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game")
public class GameResource {

    /*
        ToDo -> Handle Exceptions
        Every method that takes a UUID or some other parameter, needs to raise an exception
        whenever that parameter is invalid. This exception will be handled here, and passed
        on as the API response.
        https://www.baeldung.com/exception-handling-for-rest-with-spring (something like this?)
        Marek research pls
     */

    ICardService cardService;
    IGameService gameService;
    ICardSetService cardSetService;

    @Autowired
    public GameResource(ICardService cardService, IGameService gameService, ICardSetService cardSetService) {
        this.cardService = cardService;
        this.gameService = gameService;
        this.cardSetService = cardSetService;

    }


    @PostMapping(path = "/init", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> initGame(@RequestBody ObjectNode json) {
        Game g = gameService.initGame(json.get("timeLimit").asInt(),
                json.get("singlePlayer").asBoolean(), json.get("playingAlone").asBoolean());
        return new ResponseEntity<>(g, HttpStatus.OK);
    }


    @PostMapping(path = "/{uuid}/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startGame(@PathVariable UUID uuid, @RequestBody List<String> unwantedCards) {
        List<UUID> unwantedCardsUUIDs = unwantedCards.stream().map(s -> UUID.fromString(s)).collect(Collectors.toList());
        Game g = gameService.startGame(uuid, unwantedCardsUUIDs);
        return new ResponseEntity<>(g, HttpStatus.OK);
    }

    //TODO:change pathvariable to requestbody
    @PostMapping(path = "/{gameid}/{cardsetid}/start")
    public ResponseEntity<?> startGame(@PathVariable String gameid, @PathVariable UUID cardsetid) {
        Game g = gameService.startGame(UUID.fromString(gameid), cardsetid);
        return new ResponseEntity<>(g, HttpStatus.OK);
    }

    @GetMapping(path = "/draw")
    public ResponseEntity<?> drawCard(@RequestParam UUID gameId) {
        if (gameService.draw(gameId) == null) {
            return new ResponseEntity<>(gameService.finishGame(gameId), HttpStatus.NO_CONTENT); // todo - make sure frontend knows about this behavior
        } else {
            return new ResponseEntity<>(gameService.draw(gameId), HttpStatus.OK);
        }
    }


    @PostMapping(path = "/done", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cardDone(@RequestBody ObjectNode json) {
        return new ResponseEntity<>(gameService.removeDoneCard(UUID.fromString(json.get("gameId").asText()),
                UUID.fromString(json.get("cardId").asText())), HttpStatus.OK);
    }

    @PostMapping(path = "/finish")
    public ResponseEntity<?> finishGame(@PathVariable UUID uuid) {
        return new ResponseEntity<>(gameService.finishGame(uuid), HttpStatus.OK);
    }


    @GetMapping(path = "/cards")
    public ResponseEntity<?> getAllCards() {
        Iterable<CardEntity> allCards = cardService.findAll();
        return new ResponseEntity<>(allCards, HttpStatus.OK);
    }

    @GetMapping(path = "/cardsets")
    public ResponseEntity<?> getAllCardsets() {
        Iterable<CardSetEntity> allCardsets = cardSetService.findAll();
        return new ResponseEntity<>(allCardsets, HttpStatus.OK);
    }

    @GetMapping(path = "/games")
    public ResponseEntity<?> getAllGames() {
        Iterable<GameEntity> allGames = gameService.findAll();
        return new ResponseEntity<>(allGames, HttpStatus.OK);
    }


}
