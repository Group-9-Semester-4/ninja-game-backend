package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.params.FinishGameParam;
import com.group9.NinjaGame.models.params.InitGameParam;

public interface IGameService {
    Game initGame(InitGameParam param);

    Game finishGame(FinishGameParam param) throws Exception;

    Iterable<Game> findAll();
}
