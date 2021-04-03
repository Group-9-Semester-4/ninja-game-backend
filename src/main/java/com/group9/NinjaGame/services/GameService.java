package com.group9.NinjaGame.services;

import com.group9.NinjaGame.container.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
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

    @Autowired
    public GameService(ICardService cardService, CardSetRepository cardSetRepository, CardRepository cardRepository, GameRepository gameRepository) {
        this.cardService = cardService;
        this.cardSetRepository = cardSetRepository;
        this.cardRepository = cardRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public Game initGame(int timeLimit, boolean multiPlayer, boolean playingAlone) {
        Game game = new Game(timeLimit, multiPlayer, playingAlone);

        gameRepository.save(game);

        return game;
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

            List<UUID> cardIds = new ArrayList<>();

            for (Card card : cardSet.getCards()) {
                cardIds.add(card.getId());
            }

            GameContainer.getInstance().setGameCards(gameId, cardIds);

            game = gameRepository.save(game);
        }

        return game;
    }

    @Override
    public Game startGame(UUID gameId, List<UUID> unwantedCards) {
        Optional<Game> gameEntityOptional = gameRepository.findById(gameId);
        Game game = null;

        if (gameEntityOptional.isPresent()) {
            game = gameEntityOptional.get();

            List<UUID> cards = (List<UUID>) cardRepository.getCardIds(unwantedCards);

            GameContainer.getInstance().setGameCards(gameId, cards);

            game = gameRepository.save(game);
        }

        return game;
    }

    @Override
    public Card draw(UUID gameId) {

        List<UUID> cardIds = GameContainer.getInstance().getGameCards(gameId);

        if (cardIds != null) {
            UUID drawnCardId = cardIds.get(new Random().nextInt(cardIds.size()));

            Optional<Card> cardOptional = cardRepository.findById(drawnCardId);

            return cardOptional.orElse(null);
        }

        return null;
    }

    public boolean removeDoneCard(UUID gameId, UUID cardId) {
        List<UUID> cardIds = GameContainer.getInstance().getGameCards(gameId);

        return cardIds.remove(cardId);
    }

    public Game finishGame(UUID gameId) {
        Optional<Game> gameEntityOptional = gameRepository.findById(gameId);
        Game game;

        GameContainer.getInstance().removeGame(gameId);

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
