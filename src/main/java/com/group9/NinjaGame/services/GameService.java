package com.group9.NinjaGame.services;

import com.group9.NinjaGame.container.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.GameEntity;
import com.group9.NinjaGame.models.Game;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import org.springframework.beans.BeanUtils;
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
    public GameEntity initGame(int timeLimit, boolean singlePlayer, boolean playingAlone) {
        GameEntity gameEntity = new GameEntity(timeLimit, singlePlayer, playingAlone);

        gameRepository.save(gameEntity);

        return gameEntity;
    }

    @Override
    public GameEntity startGame(UUID gameId, UUID cardSetId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        Optional<CardSet> cardSetEntityOptional = cardSetRepository.findById(cardSetId);

        GameEntity gameEntity = null;

        if (gameEntityOptional.isPresent() && cardSetEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();
            CardSet cardSet = cardSetEntityOptional.get();

            gameEntity.setSelectedCardSet(cardSet);

            List<UUID> cardIds = new ArrayList<>();

            for (Card card : cardSet.getCards()) {
                cardIds.add(card.getId());
            }

            GameContainer.getInstance().setGameCards(gameId, cardIds);

            gameEntity = gameRepository.save(gameEntity);
        }

        return gameEntity;
    }

    @Override
    public GameEntity startGame(UUID gameId, List<UUID> unwantedCards) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        GameEntity gameEntity = null;

        if (gameEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();

            List<UUID> cards = (List<UUID>) cardRepository.getCardIds(unwantedCards);

            GameContainer.getInstance().setGameCards(gameId, cards);

            gameEntity = gameRepository.save(gameEntity);
        }

        return gameEntity;
    }

    @Override
    public Card draw(UUID gameId) {

        List<UUID> cardIds = GameContainer.getInstance().getGameCards(gameId);

        UUID drawnCardId = cardIds.get(new Random().nextInt(cardIds.size()));

        Optional<Card> cardOptional = cardRepository.findById(drawnCardId);

        return cardOptional.orElse(null);

    }

    public boolean removeDoneCard(UUID gameId, UUID cardId) {
        List<UUID> cardIds = GameContainer.getInstance().getGameCards(gameId);

        return cardIds.remove(cardId);
    }

    public Game finishGame(UUID gameId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        GameEntity gameEntity = null;
        Game g = new Game();
        if (gameEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();
            BeanUtils.copyProperties(gameEntity, g);

            if (gameEntity.getSelectedCardSet().isTemporary()){
                cardSetRepository.delete(gameEntity.getSelectedCardSet());
            }

            gameRepository.delete(gameEntity);
        }
        return g;
    }

    public Iterable<GameEntity> findAll() {
        return gameRepository.findAll();
    }
}
