package com.group9.NinjaGame.services;

import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.group9.NinjaGame.container.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.models.params.JoinGameParam;
import com.group9.NinjaGame.models.params.StartGameParam;
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

    public Game startGame(StartGameParam param) {
        Optional<Game> gameEntityOptional = gameRepository.findById(param.gameId);
        Optional<CardSet> cardSetEntityOptional = cardSetRepository.findById(param.cardSetId);

        if (gameEntityOptional.isPresent() && cardSetEntityOptional.isPresent()) {
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

            multiplayerGameService.startGame(param.gameId, cards);

            return game;
        }

        return null;
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
