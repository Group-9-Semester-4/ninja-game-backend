package com.group9.NinjaGame.resources.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.helpers.exceptions.StartBossFightException;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.messages.MessageType;
import com.group9.NinjaGame.models.params.BossScoreParam;
import com.group9.NinjaGame.models.params.CardCompleteParam;
import com.group9.NinjaGame.services.ConcurrentGameModeService;
import com.group9.NinjaGame.services.DeathMatchGameModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.group9.NinjaGame.helpers.SocketIOHelper.SendMessage;

@Component
public class DeathMatchGameModeResource {
    private SocketIONamespace namespace;
    private DeathMatchGameModeService deathMatchGameModeService;

    @Autowired
    public DeathMatchGameModeResource(DeathMatchGameModeService deathMatchGameModeService) {
        this.deathMatchGameModeService = deathMatchGameModeService;
    }

    public void registerListeners(SocketIONamespace namespace) {
        this.namespace = namespace;
        this.namespace.addEventListener("deathmatch.complete", CardCompleteParam.class, this::onComplete);
        this.namespace.addEventListener("concurrent.ready", Object.class, this::onReady);
    }

    public void onReady(SocketIOClient client, Object data, AckRequest ackSender) {
        try {
            GameInfo gameInfo = deathMatchGameModeService.onReady(client.getSessionId());
            if(deathMatchGameModeService.allPlayersReady(client.getSessionId())) {
                SendMessage(ackSender, MessageType.SUCCESS, "ready", gameInfo);
            }
            else SendMessage(ackSender, MessageType.SUCCESS, "not ready", gameInfo);
        } catch (Exception e) {
            SendMessage(ackSender, MessageType.ERROR, e.getMessage());
        }
    }


    public void onComplete(SocketIOClient client, CardCompleteParam param, AckRequest ackSender) {
        try {
            GameInfo gameInfo = deathMatchGameModeService.onComplete(param);
            namespace.getRoomOperations(gameInfo.gameId.toString()).sendEvent("game-update", gameInfo);
        } catch (Exception e) {
            SendMessage(ackSender, MessageType.ERROR, e.getMessage());
        }
    }

}
