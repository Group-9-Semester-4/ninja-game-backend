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
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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

        try {
            game = gameRepository.save(game);

            if (game.isMultiPlayer()) {
                multiplayerGameService.initGame(game.getId(), param.lobbyCode);
            }
            return game;
        }
        catch(Exception e){
            throw e;
        }

    }

    public Game joinGame(JoinGameParam param) throws Exception {

        try {
            GameInfo gameInfo = gameContainer.getGameInfo(param.lobbyCode);

            Optional<Game> gameEntityOptional = gameRepository.findById(gameInfo.gameId);

            if (gameEntityOptional.isPresent()) {
                Player player = new Player(param.userName);

                multiplayerGameService.joinGame(gameInfo.gameId, player);

                return gameEntityOptional.get();
            }
            else {
                throw new NotFoundException("Can't find Game with this ID");
            }
        }
        catch (Exception e){
            throw e;
        }
    }


    @Override
    public Game startGame(UUID gameId, UUID cardSetId) throws Exception {
        try {
            Optional<Game> gameEntityOptional = gameRepository.findById(gameId);

            if(gameEntityOptional.isPresent()){

                Optional<CardSet> cardSetEntityOptional = cardSetRepository.findById(cardSetId);
                Game game = gameEntityOptional.get();
                CardSet cardSet = cardSetEntityOptional.get();

                game.setSelectedCardSet(cardSet);

                List<Card> cards = new ArrayList<>(cardSet.getCards());

                game = gameRepository.save(game);

                multiplayerGameService.startGame(gameId, cards);

                return game;
            }
            else {
                throw new NotFoundException("Can't find Game with this ID");
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Game startGame(UUID gameId, List<UUID> unwantedCards) throws Exception {
        try{
            Optional<Game> gameEntityOptional = gameRepository.findById(gameId);
            if(gameEntityOptional.isPresent()){
                Game game = gameEntityOptional.get();

                game = gameRepository.save(game);


                List<Card> cards = (List<Card>) cardRepository.getCards(unwantedCards);

                multiplayerGameService.startGame(gameId, cards);

                return game;
            }
            else {
                throw new NotFoundException("Can't find Game with this ID");
            }

        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Card draw(UUID gameId) {
        try {
            List<Card> cards = gameContainer.getGameInfo(gameId).remainingCards;
            return cards.get(new Random().nextInt(cards.size()));
        }
        catch(Exception e){
            throw e;
        }
    }

    public boolean removeDoneCard(UUID gameId, UUID cardId) {
        try {
            GameInfo gameInfo = gameContainer.getGameInfo(gameId);
            return gameInfo.removeCardById(cardId);
        }
        catch(Exception e){
            throw e;
        }
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
