package com.group9.NinjaGame.unit.services;

import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.services.CardSetService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardSetServiceTest {

    private CardSet cardSet;
    private List<CardSet> list;
    CardSetService cardSetService;


    @Mock
    private CardSetRepository cardSetRepository;

    @BeforeEach
    public void setUp() {
        assertNotNull(cardSetRepository);

        cardSetService = new CardSetService(cardSetRepository);

        list = new ArrayList<>();

        cardSet = new CardSet();
        cardSet.setId(UUID.randomUUID());
        cardSet.setName("cardSetName100");
    }


    @Test
    public void testGetEntityById() throws NotFoundException {
        Optional<CardSet> result = Optional.of(cardSet);
        doReturn(result).when(cardSetRepository).findById(cardSet.getId());

        CardSet foundCard = cardSetService.getById(cardSet.getId().toString());

        assertEquals(foundCard, cardSet);
        assertNotNull(foundCard);
        verify(cardSetRepository, times(1)).findById(cardSet.getId());
    }

    @Test
    public void testListAllCardSets() {
        CardSet cardSet2 = new CardSet();
        cardSet2.setId(UUID.randomUUID());
        cardSet2.setName("cardSetName101");

        list.add(cardSet);
        list.add(cardSet2);

        doReturn(list).when(cardSetRepository).findAll();
        List<CardSet> res = cardSetService.findAll();

        assertNotNull(res);
        assertTrue(res.size() > 0);
        assertTrue(res.contains(cardSet) && res.contains(cardSet2));
    }

    @Test
    public void testAddCardSet() {
        doReturn(cardSet).when(cardSetRepository).save(cardSet);

        CardSet res = cardSetService.createCardSet(cardSet);

        assertNotNull(res);
        assertTrue(res.getName().equalsIgnoreCase("cardSetName100"));
        verify(cardSetRepository, times(1)).save(cardSet);
    }

    @Test
    public void testDeleteCardSet() {
        cardSetService.deleteCardSet(cardSet);

        verify(cardSetRepository, times(1)).delete(cardSet);
    }

    @Test
    public void testDeletedCardSetThrowsNotFoundException() throws NotFoundException {
        doReturn(Optional.empty()).when(cardSetRepository).findById(cardSet.getId());

        cardSetService.deleteCardSet(cardSet);

        try {
            cardSetService.getById(cardSet.getId().toString());
            fail();
        } catch (NotFoundException e) {
            assertEquals(e.getMessage(), "Can't find Card set with this ID");
        }
        verify(cardSetRepository, times(1)).delete(cardSet);
        verify(cardSetRepository, times(1)).findById(cardSet.getId());
    }
}
