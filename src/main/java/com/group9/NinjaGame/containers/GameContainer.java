package com.group9.NinjaGame.containers;

import com.group9.NinjaGame.models.Game;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class GameContainer {
    private static GameContainer gameContainer;
    private List<Game> games;

    private GameContainer() {
        this.games = new ArrayList<Game>();
    }
    public static GameContainer getInstance() {
        if(gameContainer == null) {
            gameContainer = new GameContainer();
        }
        return gameContainer;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void endGame(Game game) {
        games.remove(game);
    }

    public Game findGame(UUID uuid) {
        for (Game g : games) {
            if(g.getId().equals(uuid)) return g;
        }
        return null;
    }
}
