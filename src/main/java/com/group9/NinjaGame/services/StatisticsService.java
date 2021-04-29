package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.entities.statisics.CardDiscard;
import com.group9.NinjaGame.entities.statisics.CardRedraw;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.params.FinishGameParam;
import com.group9.NinjaGame.repositories.CardDiscardRepository;
import com.group9.NinjaGame.repositories.CardRedrawRepository;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class StatisticsService implements IStatisticsService {

    private final CardRepository cardRepository;
    private final GameRepository gameRepository;
    private final GameContainer gameContainer;
    private final CardDiscardRepository cardDiscardRepository;
    private final CardRedrawRepository cardRedrawRepository;

    @Autowired
    public StatisticsService(CardRepository cardRepository, GameRepository gameRepository, CardDiscardRepository cardDiscardRepository, CardRedrawRepository cardRedrawRepository) {
        this.cardRepository = cardRepository;
        this.gameRepository = gameRepository;
        this.gameContainer = GameContainer.getInstance();
        this.cardDiscardRepository = cardDiscardRepository;
        this.cardRedrawRepository = cardRedrawRepository;
    }

    @Override
    public Game insertGameStatistics(FinishGameParam finishGameParam){
        Game game = null;
        GameInfo gameInfo = gameContainer.getPlayerGame(finishGameParam.playerId);
        //check if game exists in container
        if(gameInfo instanceof GameInfo){
            Optional<Game> gameOptional = gameRepository.findById(finishGameParam.gameId);
            if(gameOptional.isPresent()){
                //get game from DB
                game = gameOptional.get();
                //add optional fields from end of the game
                game.setPercentageOfDoneCards(finishGameParam.percentageOfDoneCards);
                game.setTimeInSeconds(finishGameParam.timeInSeconds);
                //override game in DB with the new found info (finished cards, time to complete, etc.)
                try {
                    gameRepository.save(game);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return game;
    }

    @Override
    public List<CardDiscard> getAllCardDiscards() {
        return cardDiscardRepository.findAll();
    }

    @Override
    public List<CardDiscard> createCardDiscards(List<CardDiscard> cardDiscards) {
        return cardDiscardRepository.saveAll(cardDiscards);
    }

    @Override
    public List<CardRedraw> getAllCardRedraws() {
        return cardRedrawRepository.findAll();
    }

    @Override
    public List<CardRedraw> createCardRedraws(List<CardRedraw> cardRedraws) {
        return cardRedrawRepository.saveAll(cardRedraws);
    }


}
