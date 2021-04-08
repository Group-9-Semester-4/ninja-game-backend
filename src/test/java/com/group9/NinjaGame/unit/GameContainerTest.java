package com.group9.NinjaGame.unit;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.models.GameInfo;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class GameContainerTest {

    final UUID uuid = UUID.randomUUID();
    GameInfo gameInfo = new GameInfo(uuid, "123456");
    GameContainer gameContainer = GameContainer.getInstance();


    @Test
    void initGameTest() {

    }
}