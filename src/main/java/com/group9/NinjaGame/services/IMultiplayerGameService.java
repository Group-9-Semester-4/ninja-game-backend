package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.Player;

import java.util.List;
import java.util.UUID;

public interface IMultiplayerGameService {

    void initGame(UUID gameId, String lobbyCode);

    boolean joinGame(UUID gameId, Player player);

    void startGame(UUID gameId, List<Card> cards);
}
