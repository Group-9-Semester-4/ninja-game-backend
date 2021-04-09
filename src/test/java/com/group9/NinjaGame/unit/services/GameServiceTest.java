package com.group9.NinjaGame.unit.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.modes.SinglePlayerGameMode;
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
    private GameInfo gameInfo;

    @Mock
    private GameRepository gameRepository;
    @Mock
    private CardSetRepository cardSetRepository;

    // This method checks if helper classes work properly and sets up a gameService & container objects
    @BeforeEach
    public void setUp() {
        assertNotNull(gameRepository);
        assertNotNull(cardSetRepository);

        gameService = new GameService(cardSetRepository, gameRepository);

        assertNotNull(GameContainer.getInstance());
    }


    @Test
    public void testInitGame() {
        InitGameParam testParam = new InitGameParam();
        testParam.lobbyCode = "123456";
        testParam.multiPlayer = true;
        testParam.playingAlone = true;
        testParam.timeLimit = 60;

        game = new Game(testParam.timeLimit, testParam.multiPlayer, testParam.playingAlone);
        doReturn(game).when(gameRepository).save(any(Game.class));

        Game createdGame = gameService.initGame(testParam);

        assertEquals(game, createdGame);
    }

    @Test
    public void testStartGame() {
        createGameInProgress();
        GameContainer.getInstance().initGame(gameInfo);
        doReturn(game).when(gameRepository).save(game);
        doReturn(optionalGame).when(gameRepository).findById(uuid);
        doReturn(optionalCardSet).when(cardSetRepository).findById(uuid);

        StartGameParam testParam = new StartGameParam();
        testParam.gameId = uuid;
        testParam.gameMode = "basic";
        testParam.cardSetId = uuid;
        testParam.unwantedCards = new ArrayList<>();

        try {
            Game testG = gameService.startGame(testParam);
            assertNotNull(testG);
        } catch (NotFoundException e) {
            fail();
        }

        verify(gameRepository, times(1)).save(game);
    }

    @Test
    public void testDrawCardDuringGame() {
        createGameInProgress();

        Card cardDrawn = gameService.draw(uuid);

        assertEquals(card.getName(), cardDrawn.getName());
        assertEquals(card.getId(), cardDrawn.getId());
        assertNotNull(cardDrawn);
    }

    @Test
    public void testRemoveDoneCardDuringGame() {
        createGameInProgress();
        boolean res = gameService.removeDoneCard(uuid, uuid);
        assertTrue(res);
    }

    @Test
    public void testFinishGame() {
        try {
            createGameInProgress();
            doReturn(optionalGame).when(gameRepository).findById(uuid);
            assertDoesNotThrow(() -> {
                gameService.finishGame(uuid);
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
        List<Card> list = new ArrayList<>();
        card = new Card();
        card.setId(uuid);
        list.add(card);
        cardSet.setCards(list);
        optionalGame = Optional.of(game);
        optionalCardSet = Optional.of(cardSet);
        gameInfo = new GameInfo(uuid, "asdf");
        gameInfo.gameModeData = new SinglePlayerGameMode();
        gameInfo.gameModeData.setCards(list);
        GameContainer.getInstance().initGame(gameInfo);
    }

}
