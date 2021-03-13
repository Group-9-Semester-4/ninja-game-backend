package com.group9.NinjaGame.services;

import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;

import java.util.List;
import java.util.UUID;

public interface IGameService {
    Game initGame(int timeLimit, boolean singlePlayer, boolean playingAlone);
    Card draw(UUID uuid);
    Game startGame(UUID uuid, List<Card> unwantedCards);
    List<Card> removeDoneCard(UUID gameId, Card cardDone);
}
