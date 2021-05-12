package com.group9.NinjaGame.unit.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.entities.User;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.modes.SinglePlayerGameMode;
import com.group9.NinjaGame.models.params.FinishGameParam;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.models.params.StartGameParam;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import com.group9.NinjaGame.repositories.UserRepository;
import com.group9.NinjaGame.services.GameService;
import com.group9.NinjaGame.services.IStatisticsService;
import com.group9.NinjaGame.services.StatisticsService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    // Helper classes used throughout testing
    GameService gameService;
    private Game game;
    private Card card;
    private CardSet cardSet;
    private Optional<Game> optionalGame;
    private Optional<CardSet> optionalCardSet;
    private UUID uuid = UUID.randomUUID();
    private String email = "tester@test.com";
    private GameInfo gameInfo;

    @Mock
    private GameRepository gameRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private IStatisticsService statisticsService;

    // This method checks if helper classes work properly and sets up a gameService & container objects
    @BeforeEach
    public void setUp() {
        assertNotNull(gameRepository);
        assertNotNull(userRepository);
        assertNotNull(statisticsService);

        gameService = new GameService(gameRepository, userRepository, statisticsService);

        assertNotNull(GameContainer.getInstance());
    }


    @Test
    public void testInitGame() {
        InitGameParam testParam = new InitGameParam();
        testParam.lobbyCode = "123456";
        testParam.multiPlayer = true;
        testParam.playingAlone = true;
        testParam.timeLimit = 60;
        testParam.email = email;

        User user = new User(email);
        user.setId(UUID.randomUUID());

        game = new Game(testParam.timeLimit, testParam.multiPlayer, testParam.playingAlone, user);
        doReturn(game).when(gameRepository).save(any(Game.class));
        doReturn(user).when(userRepository).findByEmail(email);

        Game createdGame = gameService.initGame(testParam);

        assertEquals(game, createdGame);
    }

    @Test
    public void testFinishGame() {
        try {
            createGameInProgress();
            doReturn(optionalGame).when(gameRepository).findById(game.getId());

            FinishGameParam param = new FinishGameParam();
            param.gameId = game.getId();

            assertDoesNotThrow(() -> {
                gameService.finishGame(param);
            });
        } catch (Exception e) {
            fail();
        }

        verify(gameRepository, times(1)).delete(game);
    }

    @Test
    public void testFindAllGames() {
        List<Game> games = new ArrayList<>();
        games.add(game);
        doReturn(games).when(gameRepository).findAll();

        assertEquals(gameService.findAll(), games);
        verify(gameRepository, times(1)).findAll();
    }


    // description: helper method used for any tests testing features "during a game"
    // explanation: creates a game and initializes it in gameContainer
    private void createGameInProgress() {
        game = new Game();
        game.setId(uuid);
        game.setMultiPlayer(true);
        cardSet = new CardSet();
        cardSet.setId(uuid);
        List<Card> list = new ArrayList<>();
        card = new Card();
        card.setId(uuid);
        list.add(card);
        cardSet.setCards(list);
        optionalGame = Optional.of(game);
        optionalCardSet = Optional.of(cardSet);
        gameInfo = new GameInfo(uuid, "asdf");
        gameInfo.gameModeData = new SinglePlayerGameMode();
        gameInfo.gameModeData.init(gameInfo, list, 0);
        GameContainer.getInstance().initGame(gameInfo);
    }

}
