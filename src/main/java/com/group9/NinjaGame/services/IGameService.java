package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.entities.GameEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;

import java.util.List;
import java.util.UUID;

public interface IGameService {
    GameEntity initGame(int timeLimit, boolean singlePlayer, boolean playingAlone);

    Card draw(UUID uuid);

    GameEntity startGame(UUID uuid, List<UUID> unwantedCards);
    GameEntity startGame(UUID gameId, UUID cardSetId);

    List<CardEntity> removeDoneCard(UUID gameId, UUID cardId);

    Game finishGame(UUID uuid);
    Iterable<GameEntity> findAll();
}
