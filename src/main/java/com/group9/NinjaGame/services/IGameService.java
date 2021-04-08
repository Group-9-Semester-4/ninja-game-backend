package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.models.params.JoinGameParam;
import com.group9.NinjaGame.models.params.StartGameParam;

import java.util.List;
import java.util.UUID;

public interface IGameService {
    Game initGame(InitGameParam param);

    Card draw(UUID uuid);

    Game startGame(StartGameParam param) throws Exception;

    boolean removeDoneCard(UUID gameId, UUID cardId);

    Game finishGame(UUID uuid) throws Exception;
    Iterable<Game> findAll();
}
