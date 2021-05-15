package com.group9.NinjaGame.resources.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.messages.MessageType;
import com.group9.NinjaGame.models.params.BossScoreParam;
import com.group9.NinjaGame.services.BasicGameModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.group9.NinjaGame.helpers.SocketIOHelper.SendMessage;

@Component
public class BasicGameModeResource {

    private SocketIOServer server;
    private BasicGameModeService basicGameModeService;

    @Autowired
    public BasicGameModeResource(BasicGameModeService basicGameModeService) {
        this.basicGameModeService = basicGameModeService;
    }

    public void registerListeners(SocketIOServer server) {
        this.server = server;
        this.server.addEventListener("basic.draw", Object.class, this::onDraw);
        this.server.addEventListener("basic.complete", Object.class, this::onComplete);
        this.server.addEventListener("basic.boss-complete", BossScoreParam.class, this::onBossComplete);
    }

    public void onDraw(SocketIOClient client, Object data, AckRequest ackSender) {
        try {
            GameInfo gameInfo = basicGameModeService.onDraw(client.getSessionId());
            server.getRoomOperations(gameInfo.gameId.toString()).sendEvent("game-update", gameInfo);
        } catch (Exception e) {
            SendMessage(ackSender, MessageType.ERROR, e.getMessage());
        }
    }

    public void onComplete(SocketIOClient client, Object data, AckRequest ackSender) {
        try {
            GameInfo gameInfo = basicGameModeService.onComplete(client.getSessionId());
            server.getRoomOperations(gameInfo.gameId.toString()).sendEvent("game-update", gameInfo);
        } catch (Exception e) {
            SendMessage(ackSender, MessageType.ERROR, e.getMessage());
        }
    }

    public void onBossComplete(SocketIOClient client, BossScoreParam bossScore, AckRequest ackSender) {
        try {
            GameInfo gameInfo = basicGameModeService.onBossComplete(bossScore, client.getSessionId());
            server.getRoomOperations(gameInfo.gameId.toString()).sendEvent("boss-score-update", gameInfo);
            SendMessage(ackSender, MessageType.SUCCESS, "", gameInfo);
        } catch (Exception e) {
            SendMessage(ackSender, MessageType.ERROR, e.getMessage());
        }
    }
}
