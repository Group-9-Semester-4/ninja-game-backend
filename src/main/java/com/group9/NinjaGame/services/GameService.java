package com.group9.NinjaGame.services;

import com.group9.NinjaGame.container.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.models.params.JoinGameParam;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameService implements IGameService {

    private ICardService cardService;
    private CardRepository cardRepository;
    private CardSetRepository cardSetRepository;
    private GameRepository gameRepository;
    private IMultiplayerGameService multiplayerGameService;
    private GameContainer gameContainer;

    @Autowired
    public GameService(ICardService cardService, CardSetRepository cardSetRepository, CardRepository cardRepository, GameRepository gameRepository, IMultiplayerGameService multiplayerGameService) {
        this.cardService = cardService;
        this.cardSetRepository = cardSetRepository;
        this.cardRepository = cardRepository;
        this.gameRepository = gameRepository;
        this.multiplayerGameService = multiplayerGameService;
        gameContainer = GameContainer.getInstance();
    }

    @Override
    public Game initGame(InitGameParam param) {
        Game game = new Game(param.timeLimit, param.multiPlayer, param.playingAlone);

        game = gameRepository.save(game);
        
        if (game.isMultiPlayer()) {
            multiplayerGameService.initGame(game.getId(), param.lobbyCode);
        }

        return game;
    }

    public Game joinGame(JoinGameParam param) {

        GameInfo gameInfo = gameContainer.getGameInfo(param.lobbyCode);

        if (gameInfo != null) {
            Optional<Game> gameEntityOptional = gameRepository.findById(gameInfo.gameId);

            if (gameEntityOptional.isPresent()) {
                Player player = new Player(param.userName);

                multiplayerGameService.joinGame(gameInfo.gameId, player);

                return gameEntityOptional.get();
            }

        }

        return null;
    }


    @Override
    public Game startGame(UUID gameId, UUID cardSetId) {
        Optional<Game> gameEntityOptional = gameRepository.findById(gameId);
        Optional<CardSet> cardSetEntityOptional = cardSetRepository.findById(cardSetId);

        Game game = null;

        if (gameEntityOptional.isPresent() && cardSetEntityOptional.isPresent()) {
            game = gameEntityOptional.get();
            CardSet cardSet = cardSetEntityOptional.get();

            game.setSelectedCardSet(cardSet);

            List<Card> cards = new ArrayList<>(cardSet.getCards());

            game = gameRepository.save(game);

            multiplayerGameService.startGame(gameId, cards);
        }

        return game;
    }

    @Override
    public Game startGame(UUID gameId, List<UUID> unwantedCards) {
        Optional<Game> gameEntityOptional = gameRepository.findById(gameId);
        Game game = null;

        if (gameEntityOptional.isPresent()) {
            game = gameEntityOptional.get();

            game = gameRepository.save(game);

            List<Card> cards = (List<Card>) cardRepository.getCards(unwantedCards);

            multiplayerGameService.startGame(gameId, cards);
        }

        return game;
    }

    @Override
    public Card draw(UUID gameId) {

        List<Card> cards = gameContainer.getGameInfo(gameId).remainingCards;

        if (cards != null) {

            return cards.get(new Random().nextInt(cards.size()));
        }

        return null;
    }

    public boolean removeDoneCard(UUID gameId, UUID cardId) {
        GameInfo gameInfo = gameContainer.getGameInfo(gameId);

        return gameInfo.removeCardById(cardId);
    }

    public Game finishGame(UUID gameId) {
        Optional<Game> gameEntityOptional = gameRepository.findById(gameId);
        Game game;

        gameContainer.removeGame(gameId);

        if (gameEntityOptional.isPresent()) {
            game = gameEntityOptional.get();

            gameRepository.delete(game);

            return game;
        }

        return null;
    }

    public Iterable<Game> findAll() {
        return gameRepository.findAll();
    }
}
