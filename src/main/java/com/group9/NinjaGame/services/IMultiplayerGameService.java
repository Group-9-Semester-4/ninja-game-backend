package com.group9.NinjaGame.services;

import com.group9.NinjaGame.models.GameInfo;
import java.util.UUID;

public interface IMultiplayerGameService {

    void startGame(UUID gameId, GameInfo gameInfo);
}
