package com.group9.NinjaGame.unit.services;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import com.group9.NinjaGame.services.BasicGameModeService;
import com.group9.NinjaGame.services.GameService;
import com.group9.NinjaGame.services.MultiplayerGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class MultiplayerGameServiceTest {
    @Mock
    private GameRepository gameRepository;
    @Mock
    private CardSetRepository cardSetRepository;
    @Mock
    private SocketIOServer server;
    @Mock
    private SocketIONamespace namespace;
    @InjectMocks
    private BasicGameModeService basicGameModeService;


    private MultiplayerGameService multiplayerGameService;

    // This method checks if helper classes work properly and sets up a gameService & container objects
    @BeforeEach
    public void setUp() {
        assertNotNull(gameRepository);
        assertNotNull(cardSetRepository);
        assertNotNull(server);
        assertNotNull(basicGameModeService);
        multiplayerGameService = new MultiplayerGameService(gameRepository, cardSetRepository, server, basicGameModeService, namespace);
    }


    @Test
    public void testInitGame() {
//        private ConnectListener onConnected() {
//            return client -> {
//                HandshakeData handshakeData = client.getHandshakeData();
//                System.out.println("Client[{}] - Connected to chat module through '{}'" + client.getSessionId().toString() + handshakeData.getUrl());
//            };
//        }
        ConnectListener connectListener;
        multiplayerGameService.onConnected();
    }
}
