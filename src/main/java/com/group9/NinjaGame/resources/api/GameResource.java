package com.group9.NinjaGame.resources.api;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.helpers.GameModeResolver;
import com.group9.NinjaGame.models.params.CardDoneParam;
import com.group9.NinjaGame.models.params.FinishGameParam;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.models.params.StartGameParam;
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
        Note about exceptions: They are all either bad_request or internal_server_error.

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

    /*
        Beginning of the game
     */
    @PostMapping(path = "/init", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> initGame(@RequestBody InitGameParam param) {
        try {
            Game game = gameService.initGame(param);
            return new ResponseEntity<>(game, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startGame(@RequestBody StartGameParam param) {
        try {
            Game game = gameService.startGame(param);
            // todo on next line, idk where to get playerUUID
            statisticsService.insertCardDiscards(param.unwantedCards, param.cardSetId, UUID.randomUUID());
            return new ResponseEntity<>(game, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
        During the game
     */
    @GetMapping(path = "/draw")
    public ResponseEntity<?> drawCard(@RequestParam UUID gameId) {
        try {
            Card card = gameService.draw(gameId);
            if (card == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(card, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/card-done", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cardDone(@RequestBody CardDoneParam param) {
        try {
            return new ResponseEntity<>(gameService.removeDoneCard(param.gameId, param.cardId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
            At the end of the game
     */
    @PostMapping(path = "/finish", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> finishGame(@RequestBody FinishGameParam param) {
        Game g = new Game();
        try {
            // Save Statistics
            statisticsService.insertGameStatistics(param);
            // Finish the game
            return new ResponseEntity<>(gameService.finishGame(param.gameId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
        Getters for data
     */
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
