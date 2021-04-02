package com.group9.NinjaGame.resources.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.GameEntity;
import com.group9.NinjaGame.models.Game;
import com.group9.NinjaGame.models.params.CardDoneParam;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.models.params.StartGameParam;
import com.group9.NinjaGame.services.ICardService;
import com.group9.NinjaGame.services.ICardSetService;
import com.group9.NinjaGame.services.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseEntity<?> initGame(@RequestBody InitGameParam param) {
        GameEntity game = gameService.initGame(param.timeLimit, param.singlePlayer, param.playingAlone);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PostMapping(path = "/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startGame(@RequestBody StartGameParam param) {

        GameEntity game;

        if (param.cardSetId != null) {
            game = gameService.startGame(param.gameId, param.cardSetId);
        } else {
            game = gameService.startGame(param.gameId, param.unwantedCards);
        }

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @GetMapping(path = "/draw")
    public ResponseEntity<?> drawCard(@RequestParam UUID gameId) {
        if (gameService.draw(gameId) == null) {
            return new ResponseEntity<>(gameService.finishGame(gameId), HttpStatus.NO_CONTENT); // todo - make sure frontend knows about this behavior
        } else {
            return new ResponseEntity<>(gameService.draw(gameId), HttpStatus.OK);
        }
    }


    @PostMapping(path = "/card-done", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cardDone(@RequestBody CardDoneParam param) {
        return new ResponseEntity<>(gameService.removeDoneCard(param.gameId, param.cardId), HttpStatus.OK);
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
        Iterable<CardSet> allCardsets = cardSetService.findAll();
        return new ResponseEntity<>(allCardsets, HttpStatus.OK);
    }

    @GetMapping(path = "/games")
    public ResponseEntity<?> getAllGames() {
        Iterable<GameEntity> allGames = gameService.findAll();
        return new ResponseEntity<>(allGames, HttpStatus.OK);
    }


}
