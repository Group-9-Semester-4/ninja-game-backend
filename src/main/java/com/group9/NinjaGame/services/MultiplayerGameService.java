package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.helpers.GameModeResolver;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;
import com.group9.NinjaGame.models.modes.ConcurrentGameMode;
import com.group9.NinjaGame.models.modes.GameMode;
import com.group9.NinjaGame.models.params.JoinGameParam;
import com.group9.NinjaGame.models.params.LeaveGameParam;
import com.group9.NinjaGame.models.params.StartGameParam;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MultiplayerGameService {

    private final GameRepository gameRepository;

    private final CardSetRepository cardSetRepository;

    private final GameContainer gameContainer;

    @Autowired
    public MultiplayerGameService(GameRepository gameRepository, CardSetRepository cardSetRepository) {
        this.gameRepository = gameRepository;
        this.cardSetRepository = cardSetRepository;

        gameContainer = GameContainer.getInstance();
    }

    public GameInfo getGameInfoByPlayerId(UUID playerId) {
        try {
            return gameContainer.getPlayerGame(playerId);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean removePlayerFromLobby(UUID playerId, GameInfo gameInfo) {
        gameContainer.removePlayerFromLobby(playerId, gameInfo);
        return gameInfo.lobby.players.isEmpty();
    }

    public void destroyGame(UUID gameId) {
        try {
            gameContainer.removeGame(gameId);

            gameRepository.deleteById(gameId);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean onLeave(LeaveGameParam param) {
        GameInfo gameInfo = gameContainer.getGameInfo(param.gameId);

        if (gameInfo != null) {
            return true;
        }
        return false;
    }

    public GameInfo onJoin(JoinGameParam param, UUID playerId) {
        GameInfo gameInfo = gameContainer.getGameInfoByLobbyCode(param.lobbyCode);

        if (gameInfo != null && !gameInfo.started && gameInfo.multiPlayer) {
            UUID gameId = gameInfo.gameId;

            Player player = new Player(param.userName, playerId);

            boolean connected = gameContainer.joinGame(gameId, player);

            if (connected) {
                return gameInfo;
            }
        }

        return null;
    }

    public Pair<Game, GameInfo> onStart(StartGameParam param, UUID playerId) throws Exception {

        GameInfo gameInfo = gameContainer.getPlayerGame(playerId);

        if (!gameInfo.lobby.lobbyOwnerId.equals(playerId)) {
            throw new Exception("Only lobby owner can start a game");
        }

        Optional<Game> gameEntityOptional = gameRepository.findById(gameInfo.gameId);
        Optional<CardSet> cardSetEntityOptional = cardSetRepository.findById(param.cardSetId);

        if (gameEntityOptional.isPresent() && cardSetEntityOptional.isPresent()) {

            String gameModeName = param.gameMode;

            GameMode gameMode = GameModeResolver.getFromString(gameModeName);

            if (gameMode == null) {
                throw new Exception("Game mode does not exist");
            }

            Game game = gameEntityOptional.get();
            CardSet cardSet = cardSetEntityOptional.get();

            List<Card> cards = new ArrayList<>();

            for (Card card : cardSet.getCards()) {
                if (!param.unwantedCards.contains(card.getId())) {
                    cards.add(card);
                }
            }

            game.setSelectedCardSet(cardSet);
            game = gameRepository.save(game);

            gameInfo.started = true;
            gameMode.setCards(cards);
            gameMode.init(gameInfo, param.timeLimit);

            //Todo - these next two lines should be above the init on previous line???
            gameInfo.gameModeData = gameMode;
            gameInfo.gameModeId = gameModeName;

            return Pair.of(game, gameInfo);
        }
        throw new Exception("Can not start a game");
    }
}
