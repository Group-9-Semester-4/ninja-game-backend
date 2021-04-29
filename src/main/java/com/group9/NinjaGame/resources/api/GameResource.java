package com.group9.NinjaGame.resources.api;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.helpers.GameModeResolver;
import com.group9.NinjaGame.models.params.*;
import com.group9.NinjaGame.services.ICardService;
import com.group9.NinjaGame.services.ICardSetService;
import com.group9.NinjaGame.services.IGameService;
import com.group9.NinjaGame.services.IStatisticsService;
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
    IStatisticsService statisticsService;

    @Autowired
    public GameResource(ICardService cardService, IGameService gameService, ICardSetService cardSetService, IStatisticsService statisticsService) {
        this.cardService = cardService;
        this.gameService = gameService;
        this.cardSetService = cardSetService;
        this.statisticsService = statisticsService;
    }

    @PostMapping(path = "/init", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> initGame(@RequestBody InitGameParam param) {
        Game game = gameService.initGame(param);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PostMapping(path = "/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startGame(@RequestBody StartGameParam param) throws Exception {

        Game game = gameService.startGame(param);

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @GetMapping(path = "/draw")
    public ResponseEntity<?> drawCard(@RequestParam UUID gameId) {
        Card card = gameService.draw(gameId);

        if (card == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(card, HttpStatus.OK);
    }


    @PostMapping(path = "/card-done", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cardDone(@RequestBody CardDoneParam param) {
        return new ResponseEntity<>(gameService.removeDoneCard(param.gameId, param.cardId), HttpStatus.OK);
    }

    @PostMapping(path = "/finish", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> finishGame(@RequestBody FinishGameParam param) throws Exception {
        return new ResponseEntity<>(gameService.finishGame(param.gameId), HttpStatus.OK);
    }

    @PostMapping(path = "/statistics", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cardDone(@RequestBody FinishGameParam param) {
        return new ResponseEntity<>(statisticsService.insertGameStatistics(param), HttpStatus.OK);
    }


    @GetMapping(path = "/cards")
    public ResponseEntity<?> getAllCards() {
        Iterable<Card> allCards = cardService.listAll();
        return new ResponseEntity<>(allCards, HttpStatus.OK);
    }

    @GetMapping(path = "/cardsets")
    public ResponseEntity<?> getAllCardsets() {
        Iterable<CardSet> allCardsets = cardSetService.findAll();
        return new ResponseEntity<>(allCardsets, HttpStatus.OK);
    }

    @GetMapping(path = "/games")
    public ResponseEntity<?> getAllGames() {
        Iterable<Game> allGames = gameService.findAll();
        return new ResponseEntity<>(allGames, HttpStatus.OK);
    }

    @GetMapping(path = "/game-modes")
    public ResponseEntity<?> getGameModes() {
        return new ResponseEntity<>(GameModeResolver.GAME_MODES, HttpStatus.OK);
    }



}
