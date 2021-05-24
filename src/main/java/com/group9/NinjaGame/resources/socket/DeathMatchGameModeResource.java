package com.group9.NinjaGame.resources.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.messages.MessageType;
import com.group9.NinjaGame.models.modes.DeathMatchGameMode;
import com.group9.NinjaGame.models.params.CardCompleteParam;
import com.group9.NinjaGame.models.params.LockCardParam;
import com.group9.NinjaGame.models.structural.CardLockInfo;
import com.group9.NinjaGame.services.DeathMatchGameModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.group9.NinjaGame.helpers.SocketIOHelper.SendMessage;

@Component
public class DeathMatchGameModeResource {
    private SocketIOServer server;
    private DeathMatchGameModeService deathMatchGameModeService;

    @Autowired
    public DeathMatchGameModeResource(DeathMatchGameModeService deathMatchGameModeService) {
        this.deathMatchGameModeService = deathMatchGameModeService;
    }

    public void registerListeners(SocketIOServer server) {
        this.server = server;
        this.server.addEventListener("deathmatch.complete", CardCompleteParam.class, this::onComplete);
        this.server.addEventListener("deathmatch.ready", Object.class, this::onReady);
        this.server.addEventListener("deathmatch.lock-card", LockCardParam.class, this::onLockCard);
        this.server.addEventListener("deathmatch.unlock-card", LockCardParam.class, this::onLockCard);
    }

    public void onReady(SocketIOClient client, Object data, AckRequest ackSender) {
        try {
            GameInfo gameInfo = deathMatchGameModeService.onReady(client.getSessionId());
            if(deathMatchGameModeService.allPlayersReady(client.getSessionId())) {
                server.getRoomOperations(gameInfo.gameId.toString()).sendEvent("start", gameInfo);
            } else {
                server.getRoomOperations(gameInfo.gameId.toString()).sendEvent("ready-update", gameInfo);
            }
        } catch (Exception e) {
            SendMessage(ackSender, MessageType.ERROR, e.getMessage());
        }
    }

    public void onLockCard(SocketIOClient client, LockCardParam param, AckRequest ackSender) {
        try {
            GameInfo gameInfo = deathMatchGameModeService.onLock(param);

            SendMessage(ackSender, MessageType.SUCCESS, "Card locked");
            server.getRoomOperations(gameInfo.gameId.toString()).sendEvent("game-update", gameInfo);
        } catch (Exception e) {
            SendMessage(ackSender, MessageType.ERROR, e.getMessage());
        }
    }

    public void onUnlockCard(SocketIOClient client, LockCardParam param, AckRequest ackSender) {
        try {
            GameInfo gameInfo = deathMatchGameModeService.onUnlock(param);

            SendMessage(ackSender, MessageType.SUCCESS, "Card unlocked");
            server.getRoomOperations(gameInfo.gameId.toString()).sendEvent("game-update", gameInfo);
        } catch (Exception e) {
            SendMessage(ackSender, MessageType.ERROR, e.getMessage());
        }
    }


    public void onComplete(SocketIOClient client, CardCompleteParam param, AckRequest ackSender) {
        try {
            GameInfo gameInfo = deathMatchGameModeService.onComplete(param);

            DeathMatchGameMode gameMode = (DeathMatchGameMode) gameInfo.gameModeData;

            if(gameMode.remainingCards.isEmpty()){
                server.getRoomOperations(gameInfo.gameId.toString()).sendEvent("game-finish", gameInfo);
                return;
            }

            server.getRoomOperations(gameInfo.gameId.toString()).sendEvent("game-update", gameInfo);
        } catch (Exception e) {
            SendMessage(ackSender, MessageType.ERROR, e.getMessage());
        }
    }

}
