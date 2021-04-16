package com.group9.NinjaGame.unit.services;

import com.corundumstudio.socketio.*;
import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;
import com.group9.NinjaGame.models.messages.MessageType;
import com.group9.NinjaGame.models.messages.SocketIOMessage;
import com.group9.NinjaGame.models.params.JoinGameParam;
import com.group9.NinjaGame.models.params.LeaveGameParam;
import com.group9.NinjaGame.models.params.StartGameParam;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import com.group9.NinjaGame.services.BasicGameModeService;
import com.group9.NinjaGame.services.MultiplayerGameService;
import com.group9.NinjaGame.resources.api.SocketIOResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MultiplayerGameServiceTest {
    @Mock
    private GameRepository gameRepository;
    @Mock
    private CardSetRepository cardSetRepository;
    @Mock
    private SocketIOServer server;
    @Mock
    private SocketIOClient client;
    @Mock
    private AckRequest ackRequest;
    @Mock
    private SocketIONamespace namespace;
    @Mock
    private BroadcastOperations broadcastOperations;
    @Mock
    private SocketIOResource socketIOResource;
    @InjectMocks
    private BasicGameModeService basicGameModeService;


    private GameInfo gameInfo;
    private final GameContainer gameContainer =GameContainer.getInstance();
    private final UUID uuid = UUID.randomUUID();
    private final UUID playerUUID = UUID.randomUUID();
    private final String lobbyCode = "123456";
    private Card card;
    private Card card1;
    private Game game;
    private CardSet cardSet;
    private List<Card> cards;

    private MultiplayerGameService multiplayerGameService;

    // This method checks if helper classes work properly and sets up a gameService & container objects
//    @BeforeEach
//    public void setUp() {
//        assertNotNull(gameRepository);
//        assertNotNull(cardSetRepository);
//        assertNotNull(server);
//        assertNotNull(basicGameModeService);
//        assertNotNull(socketIOResource);
//
//        gameInfo = new GameInfo(uuid,lobbyCode);
//        gameContainer.initGame(gameInfo);
//        lenient().doReturn(namespace).when(server).addNamespace("/game");
//        lenient().doReturn(broadcastOperations).when(namespace).getRoomOperations(uuid.toString());
//        lenient().doReturn(playerUUID).when(client).getSessionId();
//        multiplayerGameService = new MultiplayerGameService(gameRepository,cardSetRepository);
//        socketIOResource = new SocketIOResource(server, basicGameModeService, multiplayerGameService);
//
//    }
//    private void populateCardsAndSuch(){
//        game = new Game();
//        game.setId(gameInfo.gameId);
//        game.setMultiPlayer(true);
//
//        UUID cardSetUUID = UUID.randomUUID();
//        cardSet = new CardSet();
//        cardSet.setId(cardSetUUID);
//        cardSet.setName("cardSetName100");
//        card1 = new Card();
//        cards = new ArrayList<>();
//
//        card = new Card();
//        card.setId(UUID.randomUUID());
//        card.setName("cardName100");
//
//
//        card1.setId(UUID.randomUUID());
//        card1.setName("cardName200");
//        cards.add(card);
//        cards.add(card1);
//
//        cardSet.setCards(cards);
//    }
//
//    @Test
//    public void testLeaveGameGameInfoNotFound() {
//        LeaveGameParam leaveGameParam = new LeaveGameParam();
//        leaveGameParam.gameId = UUID.randomUUID();
//
//        multiplayerGameService.onLeave(client, leaveGameParam, ackRequest);
//
//        verify(client,times(1)).leaveRoom(leaveGameParam.gameId.toString());
//    }
//
//    @Test
//    public void testLeaveGameGameInfoFound() {
//        LeaveGameParam leaveGameParam = new LeaveGameParam();
//        leaveGameParam.gameId = uuid;
//        doReturn(uuid).when(client).getSessionId();
//
//
//        gameContainer.joinGame(uuid, new Player("John",uuid));
//
//        multiplayerGameService.onLeave(client, leaveGameParam, ackRequest);
//
//        verify(client,times(2)).leaveRoom(gameInfo.gameId.toString()); // todo - will change when refactoring
//    }
//
//    @Test
//    public void testOnJoinStartedAndMultiPlayer() {
//        JoinGameParam joinGameParam = new JoinGameParam();
//        joinGameParam.lobbyCode = lobbyCode;
//        joinGameParam.userName = "user";
//
//        multiplayerGameService.onJoin(client, joinGameParam, ackRequest);
//
//        verify(ackRequest,times(1)).sendAckData(any(SocketIOMessage.class));
//        verify(ackRequest).sendAckData(argThat((SocketIOMessage msg) -> msg.type.equals(MessageType.SUCCESS)));
//        verify(namespace, times (1)).getRoomOperations(gameInfo.gameId.toString());
//        verify(broadcastOperations,times(1)).sendEvent("lobby-update",gameInfo);
//        verify(client, times(1)).joinRoom(gameInfo.gameId.toString()); // 191
//    }
//    @Test
//    public void testOnJoinNotStartedAndMultiplayer() {
//        JoinGameParam joinGameParam = new JoinGameParam();
//        joinGameParam.lobbyCode = UUID.randomUUID().toString();
//        joinGameParam.userName = "user";
//
//        multiplayerGameService.onJoin(client, joinGameParam, ackRequest);
//
//        verify(ackRequest,times(1)).sendAckData(any(SocketIOMessage.class));
//        verify(ackRequest).sendAckData(argThat((SocketIOMessage msg) -> msg.type.equals(MessageType.ERROR)));
//        verify(namespace, times (0)).getRoomOperations(gameInfo.gameId.toString());
//        verify(broadcastOperations,times(0)).sendEvent("lobby-update",gameInfo);
//        verify(client, times(0)).joinRoom(gameInfo.gameId.toString()); // 191
//    }
//
//    @Test
//    public void testOnJoinUserAlreadyJoined() {
//        JoinGameParam joinGameParam = new JoinGameParam();
//        joinGameParam.lobbyCode = lobbyCode;
//        joinGameParam.userName = "user";
//
//        gameContainer.joinGame(gameInfo.gameId, new Player("Matej", playerUUID));
//
//        multiplayerGameService.onJoin(client, joinGameParam, ackRequest);
//
//        verify(ackRequest,times(1)).sendAckData(any(SocketIOMessage.class));
//        verify(ackRequest).sendAckData(argThat((SocketIOMessage msg) -> msg.type.equals(MessageType.ERROR)));
//        verify(namespace, times (0)).getRoomOperations(gameInfo.gameId.toString());
//        verify(broadcastOperations,times(0)).sendEvent("lobby-update",gameInfo);
//        verify(client, times(1)).joinRoom(gameInfo.gameId.toString()); // 191
//    }
//
//    @Test
//    public void testOnValidStart(){
//        populateCardsAndSuch();
//
//        StartGameParam startGameParam = new StartGameParam();
//        startGameParam.gameId = gameInfo.gameId;
//        startGameParam.gameMode = "basic";
//        startGameParam.unwantedCards = new ArrayList<>();
//        startGameParam.unwantedCards.add(card.getId());
//
//        Optional<Game> optionalGame = Optional.of(game);
//        Optional<CardSet> optionalCardSet = Optional.of(cardSet);
//
//
//        doReturn(optionalGame).when(gameRepository).findById(gameInfo.gameId);
//        doReturn(game).when(gameRepository).save(game);
//        doReturn(optionalCardSet).when(cardSetRepository).findById(startGameParam.cardSetId);
//
//        gameContainer.joinGame(gameInfo.gameId, new Player("Matej", playerUUID));
//
//        multiplayerGameService.onStart(client, startGameParam, ackRequest);
//
//        verify(namespace, times (1)).getRoomOperations(gameInfo.gameId.toString());
//        assertEquals(1, gameContainer.getGameInfo(gameInfo.gameId).gameModeData.getCards().size());
//        assertSame(gameContainer.getGameInfo(gameInfo.gameId).gameModeData.getCards().get(0).getName(), card1.getName());
//    }
//
//    @Test
//    public void testOnSecondPlayerStart(){
//        populateCardsAndSuch();
//
//        UUID Matuv_kod = UUID.randomUUID();
//
//        StartGameParam startGameParam = new StartGameParam();
//        startGameParam.gameId = gameInfo.gameId;
//        startGameParam.gameMode = "basic";
//        startGameParam.unwantedCards = new ArrayList<>();
//        startGameParam.unwantedCards.add(card.getId());
//
//        lenient().doReturn(Matuv_kod).when(client).getSessionId();
//
//        gameContainer.joinGame(gameInfo.gameId, new Player("Matej", playerUUID));
//        gameContainer.joinGame(gameInfo.gameId, new Player("Martin-Hotka aka bLaWaK", Matuv_kod));
//
//        multiplayerGameService.onStart(client, startGameParam, ackRequest);
//
//        verify(namespace, times (0)).getRoomOperations(gameInfo.gameId.toString());
//        verify(ackRequest,times(1)).sendAckData(any(SocketIOMessage.class));
//        verify(ackRequest).sendAckData(argThat((SocketIOMessage msg) -> msg.type.equals(MessageType.ERROR)));
//    }
//
//
//    @Test
//    public void testOnInvalidGameModeStart(){
//        populateCardsAndSuch();
//
//        StartGameParam startGameParam = new StartGameParam();
//        startGameParam.gameId = gameInfo.gameId;
//        //this is wrong
//        startGameParam.gameMode = "not basic";
//        startGameParam.unwantedCards = new ArrayList<>();
//        startGameParam.unwantedCards.add(card.getId());
//
//        Optional<Game> optionalGame = Optional.of(game);
//        Optional<CardSet> optionalCardSet = Optional.of(cardSet);
//
//
//        doReturn(optionalGame).when(gameRepository).findById(gameInfo.gameId);
//        doReturn(optionalCardSet).when(cardSetRepository).findById(startGameParam.cardSetId);
//
//        gameContainer.joinGame(gameInfo.gameId, new Player("Matej", playerUUID));
//
//        multiplayerGameService.onStart(client, startGameParam, ackRequest);
//
//        verify(namespace, times (0)).getRoomOperations(gameInfo.gameId.toString());
//        verify(ackRequest,times(1)).sendAckData(any(SocketIOMessage.class));
//        verify(ackRequest).sendAckData(argThat((SocketIOMessage msg) -> msg.type.equals(MessageType.ERROR)));
//    }
//    @Test
//    public void testOnInvalidGameOrCardSetStart(){
//        populateCardsAndSuch();
//
//        StartGameParam startGameParam = new StartGameParam();
//        startGameParam.gameId = gameInfo.gameId;
//        startGameParam.gameMode = "basic";
//        startGameParam.unwantedCards = new ArrayList<>();
//        startGameParam.unwantedCards.add(card.getId());
//        startGameParam.cardSetId = UUID.randomUUID();
//
//
//        gameContainer.joinGame(gameInfo.gameId, new Player("Matej", playerUUID));
//
//        multiplayerGameService.onStart(client, startGameParam, ackRequest);
//
//        verify(namespace, times (0)).getRoomOperations(gameInfo.gameId.toString());
//        verify(ackRequest,times(1)).sendAckData(any(SocketIOMessage.class));
//        verify(ackRequest).sendAckData(argThat((SocketIOMessage msg) -> msg.type.equals(MessageType.ERROR)));
//    }

}
