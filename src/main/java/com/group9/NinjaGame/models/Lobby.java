package com.group9.NinjaGame.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lobby {

    public String lobbyCode;

    public UUID lobbyOwnerId;

    public List<Player> players;

    public Lobby(String lobbyCode) {
        this.lobbyCode = lobbyCode;
        lobbyOwnerId = null;
        players = new ArrayList<>();
    }

}
