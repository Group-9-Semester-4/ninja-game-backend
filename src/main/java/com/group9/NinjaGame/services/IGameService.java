package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.models.params.JoinGameParam;

import java.util.List;
import java.util.UUID;

public interface IGameService {
    Game initGame(InitGameParam param);

    Game joinGame(JoinGameParam param) throws Exception;

    Card draw(UUID uuid);

    Game startGame(UUID uuid, List<UUID> unwantedCards);
    Game startGame(UUID gameId, UUID cardSetId) throws Exception;

    boolean removeDoneCard(UUID gameId, UUID cardId);

    Game finishGame(UUID uuid) throws Exception;
    Iterable<Game> findAll();
}
