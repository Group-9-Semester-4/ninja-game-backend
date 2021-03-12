package com.group9.NinjaGame.resources;

import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;
import com.group9.NinjaGame.services.CardService;
import com.group9.NinjaGame.services.GameService;
import com.group9.NinjaGame.services.ICardService;
import com.group9.NinjaGame.services.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/start")
    public Game startGame(int timeLimit, boolean singlePlayer, boolean playingAlone) {
        return gameService.startGame(timeLimit, singlePlayer, playingAlone);
    }

    @Override
    @GetMapping("/{uuid}/draw")
    public Card draw(@PathVariable UUID uuid) {

        return null;
    }
}
