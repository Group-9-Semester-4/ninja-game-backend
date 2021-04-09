package com.group9.NinjaGame.unit.services;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import com.group9.NinjaGame.services.CardService;
import com.group9.NinjaGame.services.CardSetService;
import com.group9.NinjaGame.services.GameService;
import com.group9.NinjaGame.services.ICardService;
import com.group9.NinjaGame.services.ICardSetService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {


    GameService gameService;

    @Mock
    private GameRepository gameRepository;
    @Mock
    private CardSetRepository cardSetRepository;


    //maybe should be BeforeAll but can't get it to work
    @BeforeEach
    public void setUp(){
        assertNotNull(gameRepository);
        assertNotNull(cardSetRepository);

        gameService = new GameService(cardSetRepository, gameRepository);
    }


    @Test
    public void testInitGame() {
        //create gameparameter
        InitGameParam testParam = new InitGameParam();
        testParam.lobbyCode = "123456";
        testParam.multiPlayer = true;
        testParam.playingAlone = true;
        testParam.timeLimit = 60;

        Game game = new Game(testParam.timeLimit, testParam.multiPlayer, testParam.playingAlone);
        //gamerepo save



        doReturn(game).when(gameRepository).save(new Game(testParam.timeLimit, testParam.multiPlayer, testParam.playingAlone));
        Game createdGame = gameService.initGame(testParam);

        assertEquals(game, createdGame);
    }
}
