package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.entities.statisics.CardDiscard;
import com.group9.NinjaGame.entities.statisics.CardRedraw;
import com.group9.NinjaGame.entities.statisics.CardSetCompletion;
import com.group9.NinjaGame.entities.statisics.TimePlayedPerGame;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.params.FinishGameParam;
import com.group9.NinjaGame.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.ObjectUtils;

import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@Component
public class StatisticsService implements IStatisticsService {

    private final CardRepository cardRepository;
    private final GameRepository gameRepository;
    private final GameContainer gameContainer;
    private final CardDiscardRepository cardDiscardRepository;
    private final CardRedrawRepository cardRedrawRepository;
    private final CardSetCompletionRepository cardSetCompletionRepository;
    private final TimePlayedPerGameRepository timePlayedPerGameRepository;

    @Autowired
    public StatisticsService(CardRepository cardRepository, GameRepository gameRepository, CardDiscardRepository cardDiscardRepository, CardRedrawRepository cardRedrawRepository, CardSetCompletionRepository cardSetCompletionRepository, TimePlayedPerGameRepository timePlayedPerGameRepository) {
        this.cardRepository = cardRepository;
        this.gameRepository = gameRepository;
        this.gameContainer = GameContainer.getInstance();
        this.cardDiscardRepository = cardDiscardRepository;
        this.cardRedrawRepository = cardRedrawRepository;

        // both saved in the insertGameStatistics method.
        this.cardSetCompletionRepository = cardSetCompletionRepository;
        this.timePlayedPerGameRepository = timePlayedPerGameRepository;
    }

    @Override
    public Game insertGameStatistics(FinishGameParam finishGameParam) {
        Game game = null;
        GameInfo gameInfo = gameContainer.getPlayerGame(finishGameParam.playerId);
        //check if game exists in container
        if (gameInfo instanceof GameInfo) {
            Optional<Game> gameOptional = gameRepository.findById(finishGameParam.gameId);
            if (gameOptional.isPresent()) {
                //get game from DB
                game = gameOptional.get();
                //add optional fields from end of the game
                game.setPercentageOfDoneCards(finishGameParam.percentageOfDoneCards);
                game.setTimeInSeconds(Math.min(finishGameParam.timeInSeconds, 32000)); // This is done so the SQL database
                //override game in DB with the new found info (finished cards, time to complete, etc.)
                try {
                    insertRedrawnCards(finishGameParam.listOfRedrawnCards, finishGameParam.cardSetId, finishGameParam.playerId);
                    timePlayedPerGameRepository.save(new TimePlayedPerGame(game.getId(), game.getSelectedCardSet().getId(), game.getTimeInSeconds(), Instant.now()));
                    cardSetCompletionRepository.save(new CardSetCompletion(game.getId(), game.getSelectedCardSet().getId(), game.getPercentageOfDoneCards(), Instant.now()));
                    gameRepository.save(game);
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        return game;
    }

    @Override
    public Map<String, Long> getAllCardDiscards() {
        // The reason for the Object[] -> Map conversion: https://stackoverflow.com/questions/51150748/nonuniqueresultexception-jparepository-spring-boot
        // MySQL returns BigInteger by default, I convert into BigDecimal because of: https://stackoverflow.com/questions/5553863/cast-bigint-to-long
        Map<String, Long> mappedResult = new HashMap<>();
        List<Object[]> queryResult = cardDiscardRepository.getAllDiscardCardsWithCount();
        for (Object[] obj : queryResult ) {
            String ld = (String) ObjectUtils.nullSafe(obj[0], "ERROR - Missing name");
            BigInteger big =(BigInteger) obj[1];
            Long count = big.longValue();
            mappedResult.put(ld, count);
        }
        return mappedResult;
    }

    @Override
    public List<CardDiscard> insertCardDiscards(List<UUID> discardedCards, UUID cardSetUUID, UUID playerUUID) {

        List<CardDiscard> cardDiscards = new ArrayList<>();
        if (discardedCards.size() > 0) {
            for (UUID cardUUID : discardedCards) {
                Instant time = Instant.now();
                cardDiscards.add(new CardDiscard(cardUUID, cardSetUUID, playerUUID, time));
            }
            return cardDiscardRepository.saveAll(cardDiscards);
        }
        return Collections.emptyList();
    }

    private List<CardRedraw> insertRedrawnCards(List<UUID> redrawnCards, UUID cardSetUUID, UUID playerUUID) {
        List<CardRedraw> cardRedraws = new ArrayList<>();
        if (redrawnCards.size() > 0) {
            for (UUID cardUUID : redrawnCards) {
                Instant time = Instant.now();
                cardRedraws.add(new CardRedraw(cardUUID, cardSetUUID, playerUUID, time));
            }
            return cardRedrawRepository.saveAll(cardRedraws);
        }
        return Collections.emptyList();

    }

    @Override
    public Map<String,Long> getAllCardRedraws() {
        // The reason for the Object[] -> Map conversion: https://stackoverflow.com/questions/51150748/nonuniqueresultexception-jparepository-spring-boot
        // MySQL returns BigInteger by default, I convert into BigDecimal because of: https://stackoverflow.com/questions/5553863/cast-bigint-to-long
        Map<String, Long> mappedResult = new HashMap<>();
        List<Object[]> queryResult = cardRedrawRepository.getAllRedrawCardsWithCount();
        for (Object[] obj : queryResult ) {
            String ld = (String) ObjectUtils.nullSafe(obj[0], "ERROR - Missing name");
            BigInteger big =(BigInteger) obj[1];
            Long count = big.longValue();
            mappedResult.put(ld, count);
        }
        return mappedResult;
    }

    @Override
    public List<CardRedraw> createCardRedraws(List<CardRedraw> cardRedraws) {
        return cardRedrawRepository.saveAll(cardRedraws);
    }


}
