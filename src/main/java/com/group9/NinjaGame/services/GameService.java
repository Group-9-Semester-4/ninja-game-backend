package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.modes.GameMode;
import com.group9.NinjaGame.models.modes.SinglePlayerGameMode;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.models.params.StartGameParam;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameService implements IGameService {

    private CardSetRepository cardSetRepository;
    private GameRepository gameRepository;
    private GameContainer gameContainer;

    @Autowired
    public GameService(CardSetRepository cardSetRepository, GameRepository gameRepository) {
        this.cardSetRepository = cardSetRepository;
        this.gameRepository = gameRepository;
        gameContainer = GameContainer.getInstance();
    }

    @Override
    public Game initGame(InitGameParam param) {
        Game game = new Game(param.timeLimit, param.multiPlayer, param.playingAlone);

        game = gameRepository.save(game);

        UUID gameId = game.getId();
        GameInfo gameInfo;

        if (param.multiPlayer) {
            gameInfo = new GameInfo(gameId, param.lobbyCode);
        } else {
            gameInfo = new GameInfo(gameId);
        }

        gameContainer.initGame(gameInfo);

        return game;
    }

    public Game startGame(StartGameParam param) throws NotFoundException {
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

            GameInfo gameInfo = gameContainer.getGameInfo(game.getId());

            gameInfo.started = true;

            gameInfo.gameModeData = new SinglePlayerGameMode();
            gameInfo.gameModeData.setCards(cards);

            return game;
        }

        throw new NotFoundException("Game of Card set not found");
    }

    @Override
    public Card draw(UUID gameId) {

        GameMode mode = gameContainer.getGameInfo(gameId).gameModeData;

        if (mode instanceof SinglePlayerGameMode) {

            SinglePlayerGameMode gameMode = (SinglePlayerGameMode) mode;

            List<Card> cards = gameMode.remainingCards;

            if (cards != null) {

                return cards.get(new Random().nextInt(cards.size()));
            }
        }


        return null;
    }

    public boolean removeDoneCard(UUID gameId, UUID cardId) {

        GameMode mode = gameContainer.getGameInfo(gameId).gameModeData;

        if (mode instanceof SinglePlayerGameMode) {
            SinglePlayerGameMode gameMode = (SinglePlayerGameMode) mode;

            return gameMode.removeCardById(cardId);
        }

        return false;
    }

    public Game finishGame(UUID gameId) throws Exception {
        try {
            Optional<Game> gameEntityOptional = gameRepository.findById(gameId);
            gameContainer.removeGame(gameId);

            if (gameEntityOptional.isPresent()) {
                Game game = gameEntityOptional.get();

                gameRepository.delete(game);

                return game;
            }
            else {
                throw new NotFoundException("Can't find Game with this ID");
            }
        }
        catch(Exception e){
            //can't delete game
            throw e;
        }
    }

    public Iterable<Game> findAll() {
        try {
            return gameRepository.findAll();
        }
        catch(Exception e){
            throw e;
        }

    }
}
