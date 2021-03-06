package com.group9.NinjaGame.resources.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.helpers.exceptions.StartBossFightException;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.messages.MessageType;
import com.group9.NinjaGame.models.params.BossScoreParam;
import com.group9.NinjaGame.services.ConcurrentGameModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.group9.NinjaGame.helpers.SocketIOHelper.SendMessage;

@Component
public class ConcurrentGameModeResource {

    private SocketIOServer server;
    private ConcurrentGameModeService concurrentGameModeService;

    @Autowired
    public ConcurrentGameModeResource(ConcurrentGameModeService concurrentGameModeService) {
        this.concurrentGameModeService = concurrentGameModeService;
    }

    public void registerListeners(SocketIOServer server) {
        this.server = server;
        this.server.addEventListener("concurrent.draw", Object.class, this::onDraw);
        this.server.addEventListener("concurrent.complete", Object.class, this::onComplete);
        this.server.addEventListener("concurrent.boss-complete", BossScoreParam.class, this::onBossComplete);
    }

    public void onDraw(SocketIOClient client, Object data, AckRequest ackSender) {
        try {
            Card card = concurrentGameModeService.onDraw(client.getSessionId());
            SendMessage(ackSender, MessageType.SUCCESS, "card", card);
        } catch (Exception e) {
            SendMessage(ackSender, MessageType.ERROR, e.getMessage());
        }
    }

    public void onComplete(SocketIOClient client, Object data, AckRequest ackSender) {
        try {
            GameInfo gameInfo = concurrentGameModeService.onComplete(client.getSessionId());
            server.getRoomOperations(gameInfo.gameId.toString()).sendEvent("game-update", gameInfo);
        } catch (StartBossFightException exception) {
            GameInfo gameInfo = exception.getGameInfo();
            server.getRoomOperations(gameInfo.gameId.toString()).sendEvent("boss-start", gameInfo);
        } catch (Exception e) {
            SendMessage(ackSender, MessageType.ERROR, e.getMessage());
        }
    }

    public void onBossComplete(SocketIOClient client, BossScoreParam bossScore, AckRequest ackSender) {
        try {
            GameInfo gameInfo = concurrentGameModeService.onBossComplete(bossScore, client.getSessionId());
            server.getRoomOperations(gameInfo.gameId.toString()).sendEvent("boss-score-update", gameInfo);
            SendMessage(ackSender, MessageType.SUCCESS, "", gameInfo);
        } catch (Exception e) {
            SendMessage(ackSender, MessageType.ERROR, e.getMessage());
        }
    }
}
