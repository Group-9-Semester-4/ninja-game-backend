package com.group9.NinjaGame.unit.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.models.params.StartGameParam;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import com.group9.NinjaGame.services.GameService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {


    GameService gameService;
    private Game game;
    private UUID uuid = UUID.randomUUID();

    @Mock
    private GameRepository gameRepository;
    @Mock
    private CardSetRepository cardSetRepository;


    //maybe should be BeforeAll but can't get it to work
    @BeforeEach
    public void setUp() {
        assertNotNull(gameRepository);
        assertNotNull(cardSetRepository);
        //assertNotNull(gameContainer);

        gameService = new GameService(cardSetRepository, gameRepository);
    }


    @Test
    public void testInitGame() {
        InitGameParam testParam = new InitGameParam();
        testParam.lobbyCode = "123456";
        testParam.multiPlayer = true;
        testParam.playingAlone = true;
        testParam.timeLimit = 60;

        game = new Game(testParam.timeLimit, testParam.multiPlayer, testParam.playingAlone);

        doReturn(game).when(gameRepository).save(Mockito.any(Game.class)); // hotfix'd

        Game createdGame = gameService.initGame(testParam);

        assertEquals(game, createdGame);
    }

    @Test
    public void testStartGame() {
        Game g = new Game();
        g.setId(uuid);
        CardSet c = new CardSet();
        List<Card> list = new ArrayList<>();
        Card card = new Card();
        card.setId(uuid);
        list.add(card);
        c.setCards(list);
        Optional<Game> oG = Optional.of(g);
        Optional<CardSet> oC = Optional.of(c);
        GameInfo gI = new GameInfo(uuid, "asdf");

        MockedStatic mocked = mockStatic(GameContainer.class);

        /*

            TODO: GameContainer is a singleton which is untestable.


         */



        //doReturn(gI).when(mocked).getGameInfo(uuid);
        doReturn(g).when(gameRepository).save(g);
        doReturn(oG).when(gameRepository).findById(uuid);
        doReturn(oC).when(cardSetRepository).findById(uuid);

        StartGameParam testParam = new StartGameParam();
        testParam.gameId = uuid;
        testParam.gameMode = "basic";
        testParam.cardSetId = uuid;
        testParam.unwantedCards = new ArrayList<>();

        try
        {
            Game testG = gameService.startGame(testParam);
            assertTrue(testG instanceof Game);

        } catch (NotFoundException e)
        {
            System.out.println("asdf");
        }

        //verify(gameContainer, times(1)).getGameInfo(any(UUID.class));
    }
}
