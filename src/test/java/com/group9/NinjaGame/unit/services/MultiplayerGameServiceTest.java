package com.group9.NinjaGame.unit.services;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;
import com.group9.NinjaGame.models.messages.SocketIOMessage;
import com.group9.NinjaGame.models.params.JoinGameParam;
import com.group9.NinjaGame.models.params.LeaveGameParam;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import com.group9.NinjaGame.services.BasicGameModeService;
import com.group9.NinjaGame.services.GameService;
import com.group9.NinjaGame.services.MultiplayerGameService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    @InjectMocks
    private BasicGameModeService basicGameModeService;

    private GameInfo gameInfo;
    private GameContainer gameContainer =GameContainer.getInstance();
    private UUID uuid = UUID.randomUUID();
    private UUID playerUUID = UUID.randomUUID();
    private final String lobbyCode = "123456";


    private MultiplayerGameService multiplayerGameService;

    // This method checks if helper classes work properly and sets up a gameService & container objects
    @BeforeEach
    public void setUp() {
        assertNotNull(gameRepository);
        assertNotNull(cardSetRepository);
        assertNotNull(server);
        assertNotNull(basicGameModeService);

        gameInfo = new GameInfo(uuid,lobbyCode);
        gameContainer.initGame(gameInfo);
        lenient().doReturn(namespace).when(server).addNamespace("/game");
        lenient().doReturn(broadcastOperations).when(namespace).getRoomOperations(uuid.toString());
        lenient().doReturn(playerUUID).when(client).getSessionId();
        multiplayerGameService = new MultiplayerGameService(server,gameRepository,cardSetRepository,basicGameModeService);

    }

    @Test
    public void testLeaveGameGameInfoNotFound() {
        LeaveGameParam leaveGameParam = new LeaveGameParam();
        leaveGameParam.gameId = UUID.randomUUID();

        multiplayerGameService.onLeave(client, leaveGameParam, ackRequest);

        verify(client,times(1)).leaveRoom(leaveGameParam.gameId.toString());
        verify(ackRequest,times(1)).sendAckData(any(SocketIOMessage.class));
    }

    @Test
    public void testLeaveGameGameInfoFound() {
        LeaveGameParam leaveGameParam = new LeaveGameParam();
        leaveGameParam.gameId = uuid;
        doReturn(uuid).when(client).getSessionId();


        gameContainer.joinGame(uuid, new Player("John",uuid));

        multiplayerGameService.onLeave(client, leaveGameParam, ackRequest);

        verify(client,times(2)).leaveRoom(gameInfo.gameId.toString()); // todo - will change when refactoring
        verify(ackRequest,times(1)).sendAckData(any(SocketIOMessage.class));
    }

    @Test
    public void testOnJoinStartedAndMultiPlayer() {
        JoinGameParam joinGameParam = new JoinGameParam();
        joinGameParam.lobbyCode = lobbyCode;
        joinGameParam.userName = "user";

        multiplayerGameService.onJoin(client, joinGameParam, ackRequest);

        verify(ackRequest,times(1)).sendAckData(any(SocketIOMessage.class));
        verify(namespace, times (1)).getRoomOperations(gameInfo.gameId.toString());
        verify(broadcastOperations,times(1)).sendEvent("lobby-update",gameInfo);
        verify(client, times(1)).joinRoom(gameInfo.gameId.toString()); // 191
    }
    @Test
    public void testOnJoinNotStartedAndMultiplayer() {
        JoinGameParam joinGameParam = new JoinGameParam();
        joinGameParam.lobbyCode = UUID.randomUUID().toString();
        joinGameParam.userName = "user";

        multiplayerGameService.onJoin(client, joinGameParam, ackRequest);

        verify(ackRequest,times(1)).sendAckData(any(SocketIOMessage.class));
        verify(namespace, times (0)).getRoomOperations(gameInfo.gameId.toString());
        verify(broadcastOperations,times(0)).sendEvent("lobby-update",gameInfo);
        verify(client, times(0)).joinRoom(gameInfo.gameId.toString()); // 191
    }

    @Test
    public void testOnJoinUserAlreadyJoined() {
        JoinGameParam joinGameParam = new JoinGameParam();
        joinGameParam.lobbyCode = lobbyCode;
        joinGameParam.userName = "user";

        gameContainer.joinGame(gameInfo.gameId, new Player("Matej", playerUUID));

        multiplayerGameService.onJoin(client, joinGameParam, ackRequest);

        verify(ackRequest,times(1)).sendAckData(any(SocketIOMessage.class));
        verify(namespace, times (0)).getRoomOperations(gameInfo.gameId.toString());
        verify(broadcastOperations,times(0)).sendEvent("lobby-update",gameInfo);
        verify(client, times(1)).joinRoom(gameInfo.gameId.toString()); // 191
    }


}
