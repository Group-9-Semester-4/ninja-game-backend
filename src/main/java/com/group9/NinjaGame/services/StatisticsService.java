package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.entities.statisics.CardDiscard;
import com.group9.NinjaGame.entities.statisics.CardRedraw;
import com.group9.NinjaGame.entities.statisics.CardSetCompletion;
import com.group9.NinjaGame.entities.statisics.TimePlayedPerGame;
import com.group9.NinjaGame.models.params.FinishGameParam;
import com.group9.NinjaGame.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.ObjectUtils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.*;

@Component
public class StatisticsService implements IStatisticsService {

    private final CardSetRepository cardSetRepository;
    private final GameRepository gameRepository;
    private final CardDiscardRepository cardDiscardRepository;
    private final CardRedrawRepository cardRedrawRepository;
    private final CardSetCompletionRepository cardSetCompletionRepository;
    private final TimePlayedPerGameRepository timePlayedPerGameRepository;
    private final UserRepository userRepository;

    @Autowired
    public StatisticsService(CardSetRepository cardSetRepository, GameRepository gameRepository, CardDiscardRepository cardDiscardRepository, CardRedrawRepository cardRedrawRepository, CardSetCompletionRepository cardSetCompletionRepository, TimePlayedPerGameRepository timePlayedPerGameRepository, UserRepository userRepository) {
        this.cardSetRepository = cardSetRepository;
        this.gameRepository = gameRepository;
        this.cardDiscardRepository = cardDiscardRepository;
        this.cardRedrawRepository = cardRedrawRepository;
        this.userRepository = userRepository;

        // both saved in the insertGameStatistics method.
        this.cardSetCompletionRepository = cardSetCompletionRepository;
        this.timePlayedPerGameRepository = timePlayedPerGameRepository;
    }

    @Override
    public Game insertGameStatistics(FinishGameParam finishGameParam) {

        Optional<Game> gameOptional = gameRepository.findById(finishGameParam.gameId);
        Optional<CardSet> cardSetOptional = cardSetRepository.findById(finishGameParam.cardSetId);

        if (!gameOptional.isPresent() || !cardSetOptional.isPresent()) {
            throw new NullPointerException("Game or Card set does not exist");
        }

        Game game = gameOptional.get();
        CardSet cardSet = cardSetOptional.get();

        int amountOfCards = cardSet.getCards().size();

        int percentageOfDoneCards = (int) (((double) finishGameParam.cardsCompleted / (double) amountOfCards) * 100);

        //add optional fields from end of the game
        game.setPercentageOfDoneCards(percentageOfDoneCards);
        game.setTimeInSeconds(Math.min(finishGameParam.timeInSeconds, 32000)); // This is done so the SQL database
        //override game in DB with the new found info (finished cards, time to complete, etc.)
        insertRedrawnCards(finishGameParam.listOfRedrawnCards, finishGameParam.cardSetId, game.getUser().getId());
        insertCardDiscards(finishGameParam.unwantedCards, finishGameParam.cardSetId, game.getUser().getId());
        timePlayedPerGameRepository.save(new TimePlayedPerGame(game.getId(), finishGameParam.cardSetId, game.getTimeInSeconds(), Instant.now()));
        cardSetCompletionRepository.save(new CardSetCompletion(game.getId(), finishGameParam.cardSetId, game.getPercentageOfDoneCards(), Instant.now()));
        gameRepository.save(game);

        return game;
    }
    @Override
    public Map<String, Short> getAllTimePlayedPerGame() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); // helper to get nicely readable date time value
        Map<String,Short> mappedResult = new LinkedHashMap<>();
        List<Object[]> queryResult = timePlayedPerGameRepository.getAllPlayTimes();
        Collections.reverse(queryResult);
        for (Object[] obj : queryResult) {
            Timestamp sql_timestamp = (Timestamp) obj[0];
            String formatted_date = sdf.format(sql_timestamp);
            Short seconds_played = (Short) obj[1];
            mappedResult.put(formatted_date,seconds_played);
        }
        return mappedResult;
    }

    @Override
    public Long getAverageGameTime() {
        return timePlayedPerGameRepository.getAvgGameTimePlayingNinjaGame();
    }

    @Override
    public Map<String, Long> getAllCardDiscards() {
        // The reason for the Object[] -> Map conversion: https://stackoverflow.com/questions/51150748/nonuniqueresultexception-jparepository-spring-boot
        // MySQL returns BigInteger by default, I convert into BigDecimal because of: https://stackoverflow.com/questions/5553863/cast-bigint-to-long
        Map<String, Long> mappedResult = new LinkedHashMap<>();
        List<Object[]> queryResult = cardDiscardRepository.getAllDiscardCardsWithCount();
        for (Object[] obj : queryResult) {
            String ld = (String) ObjectUtils.nullSafe(obj[0], "ERROR - Missing name");
            BigInteger big = (BigInteger) obj[1];
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

    public List<CardRedraw> insertRedrawnCards(List<UUID> redrawnCards, UUID cardSetUUID, UUID playerUUID) {
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
    public Map<String, Long> getAllCardRedraws() {
        // The reason for the Object[] -> Map conversion: https://stackoverflow.com/questions/51150748/nonuniqueresultexception-jparepository-spring-boot
        // MySQL returns BigInteger by default, I convert into BigDecimal because of: https://stackoverflow.com/questions/5553863/cast-bigint-to-long
        Map<String, Long> mappedResult = new LinkedHashMap<>();
        List<Object[]> queryResult = cardRedrawRepository.getAllRedrawCardsWithCount();
        for (Object[] obj : queryResult) {
            String ld = (String) ObjectUtils.nullSafe(obj[0], "ERROR - Missing name");
            BigInteger big = (BigInteger) obj[1];
            Long count = big.longValue();
            mappedResult.put(ld, count);
        }
        return mappedResult;
    }

    @Override
    public List<Object[]> getAllUsers(int pageNo) {
        List<Object[]> list = userRepository.findAllPaginated(pageNo);
        for (Object[] u : list) {
            u[2] = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(u[2]);
            u[3] = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(u[3]);
        }
        return list;
    }

    @Override
    public Long countUsers() {
        return userRepository.count();
    }

}
