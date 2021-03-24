package com.group9.NinjaGame.resources.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.group9.NinjaGame.models.Game;
import com.group9.NinjaGame.services.ICardService;
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

    @Autowired
    public GameResource(ICardService cardService, IGameService gameService) {
        this.cardService = cardService;
        this.gameService = gameService;
    }


    @PostMapping(path = "/init", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> initGame(@RequestBody ObjectNode json) {
        Game g = gameService.initGame(json.get("timeLimit").asInt(), json.get("singlePlayer").asBoolean(), json.get("playingAlone").asBoolean());
        return new ResponseEntity<>(g, HttpStatus.OK);
    }



    @GetMapping(path = "/{uuid}/draw")
    public ResponseEntity<?> drawCard(@PathVariable UUID uuid) {
        if (gameService.draw(uuid) == null) {
            return new ResponseEntity<>(gameService.finishGame(uuid), HttpStatus.NO_CONTENT); // todo - make sure frontend knows about this behavior
        } else {
            return new ResponseEntity<>(gameService.draw(uuid), HttpStatus.OK);
        }
    }

    @PostMapping(path = "/{uuid}/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startGame(@PathVariable UUID uuid, @RequestBody List<String> unwantedCards) {
        List<UUID> unwantedCardsUUIDs = unwantedCards.stream().map(s -> UUID.fromString(s)).collect(Collectors.toList());
        Game g = gameService.startGame(uuid, unwantedCardsUUIDs);
        return new ResponseEntity<>(g, HttpStatus.OK);
    }


    @PostMapping(path = "/{uuid}/done", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cardDone(@PathVariable UUID uuid, @RequestBody ObjectNode cardCompletedUUID) {
        return new ResponseEntity<>(gameService.removeDoneCard(uuid, UUID.fromString(cardCompletedUUID.get("id").asText())), HttpStatus.OK);
    }

    @PostMapping(path = "/{uuid}/finish")
    public ResponseEntity<?> finishGame(@PathVariable UUID uuid) {
        return new ResponseEntity<>(gameService.finishGame(uuid), HttpStatus.OK);
    }

    //TODO:change pathvariable to requestbody
    @PostMapping(path = "/{gameid}/{cardsetid}/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startGame(@PathVariable UUID gameid, @PathVariable UUID cardsetid) {
        Game g = gameService.startGame(gameid, cardsetid);
        return new ResponseEntity<>(g, HttpStatus.OK);
    }


}
