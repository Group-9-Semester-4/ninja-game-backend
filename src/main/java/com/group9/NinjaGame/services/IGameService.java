package com.group9.NinjaGame.services;

import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;
import java.util.UUID;

public interface IGameService {
    public Game startGame(int timeLimit, boolean singlePlayer, boolean playingAlone);
    public Card draw(UUID uuid);
}
