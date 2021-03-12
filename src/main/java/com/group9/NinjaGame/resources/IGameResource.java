package com.group9.NinjaGame.resources;

import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;

import java.util.UUID;

public interface IGameResource {
    Game startGame(int timeLimit, boolean singleplayer, boolean playingAlone);
    Card draw(UUID uuid);
}
