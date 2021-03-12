package com.group9.NinjaGame.services;

import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GameService implements IGameService {

    private ICardService cardService;
    @Override
    public Game startGame(int timeLimit, boolean singlePlayer, boolean playingAlone) {
        Game game = new Game(timeLimit,singlePlayer,playingAlone);
        game.setAllCards(cardService.getAll());
        return game;
    }

    @Override
    public Card draw(UUID uuid) {
        return null;
    }
}
