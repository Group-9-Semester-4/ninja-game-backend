package com.group9.NinjaGame.unit.services;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.statisics.CardDiscard;
import com.group9.NinjaGame.entities.statisics.CardRedraw;
import com.group9.NinjaGame.repositories.*;
import com.group9.NinjaGame.services.CardService;
import com.group9.NinjaGame.services.StatisticsService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {
    UUID playerId = UUID.randomUUID();
    UUID cardSetId = UUID.randomUUID();
    StatisticsService statisticsService;


    @Mock
    private CardSetRepository cardSetRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private CardDiscardRepository cardDiscardRepository;
    @Mock
    private CardRedrawRepository cardRedrawRepository;
    @Mock
    private CardSetCompletionRepository cardSetCompletionRepository;
    @Mock
    private TimePlayedPerGameRepository timePlayedPerGameRepository;


    @BeforeEach
    public void setUp() {
        assertNotNull(cardSetRepository);
        assertNotNull(gameRepository);
        assertNotNull(cardDiscardRepository);
        assertNotNull(cardRedrawRepository);
        assertNotNull(cardSetCompletionRepository);
        assertNotNull(timePlayedPerGameRepository);

        statisticsService = new StatisticsService(cardSetRepository, gameRepository,cardDiscardRepository,
                cardRedrawRepository, cardSetCompletionRepository, timePlayedPerGameRepository);
    }

    @Test
    public void testInsertCardDiscards() {
        List<UUID> discardedCards = new ArrayList<>();
        discardedCards.add(UUID.randomUUID());
        discardedCards.add(UUID.randomUUID());
        CardDiscard cardDiscard = new CardDiscard(discardedCards.get(0), cardSetId, playerId, Instant.now());
        CardDiscard cardDiscard1 = new CardDiscard(discardedCards.get(1), cardSetId, playerId, Instant.now());
        List<CardDiscard> cardDiscards = new ArrayList<>();
        cardDiscards.add(cardDiscard);
        cardDiscards.add(cardDiscard1);
        doReturn(cardDiscards).when(cardDiscardRepository).saveAll(any());

        List<CardDiscard> discardsResult = statisticsService.insertCardDiscards(discardedCards, cardSetId, playerId);

        assertNotNull(discardsResult);
        assertTrue(discardsResult.size()==discardedCards.size());
        assertEquals(discardsResult.get(0).getCardId(), cardDiscards.get(0).getCardId());
    }
    @Test
    public void testInsertCardRedraws() {
        List<UUID> redrawnCards = new ArrayList<>();
        redrawnCards.add(UUID.randomUUID());
        redrawnCards.add(UUID.randomUUID());
        CardRedraw cardRedraw= new CardRedraw(redrawnCards.get(0), cardSetId, playerId, Instant.now());
        CardRedraw cardRedraw1 = new CardRedraw(redrawnCards.get(1), cardSetId, playerId, Instant.now());
        List<CardRedraw> cardRedraws = new ArrayList<>();
        cardRedraws.add(cardRedraw);
        cardRedraws.add(cardRedraw1);

        doReturn(cardRedraws).when(cardRedrawRepository).saveAll(any());

        List<CardRedraw> discardsResult = statisticsService.insertRedrawnCards(redrawnCards, cardSetId, playerId);

        assertNotNull(discardsResult);
        assertTrue(discardsResult.size()==redrawnCards.size());
        assertEquals(discardsResult.get(0).getCardId(), cardRedraws.get(0).getCardId());
    }
    @Test
    public void testGetAllCardDiscards() throws NotFoundException {
        List<Object[]> list = new ArrayList<>();
        Object[] item = {
                "Random card",
                BigInteger.valueOf(1l)
        };
        list.add(item);
        doReturn(list).when(cardDiscardRepository).getAllDiscardCardsWithCount();

        Map<String, Long> result = statisticsService.getAllCardDiscards();

        verify(cardDiscardRepository,times(1)).getAllDiscardCardsWithCount();
        assertNotNull(result);
        assertEquals(list.size(), result.size());
    }
    @Test
    public void testGetAllCardRedraws() throws NotFoundException {
        List<Object[]> list = new ArrayList<>();
        Object[] item = {
                "Random card name",
                BigInteger.valueOf(1l)
        };
        list.add(item);
        doReturn(list).when(cardRedrawRepository).getAllRedrawCardsWithCount();

        Map<String, Long> result = statisticsService.getAllCardRedraws();

        verify(cardRedrawRepository,times(1)).getAllRedrawCardsWithCount();
        assertNotNull(result);
        assertEquals(list.size(), result.size());
    }

}
