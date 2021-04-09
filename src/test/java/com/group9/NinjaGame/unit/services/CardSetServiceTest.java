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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CardSetServiceTest {

    private CardSet cardSet;
    private List<CardSet> list;
    CardSetService cardSetService;


    @Mock
    private CardSetRepository repository;

    //maybe should be BeforeAll but can't get it to work
    @BeforeEach
    public void setUp(){
        assertNotNull(repository);

        cardSetService = new CardSetService(repository);

        list = new ArrayList();

        cardSet = new CardSet();
        cardSet.setId(UUID.randomUUID());
        cardSet.setName("kokot");
    }


    @Test
    public void testGetEntityById() throws NotFoundException {
        Optional<CardSet> result = Optional.of(cardSet);

        doReturn(result).when(repository).findById(cardSet.getId());
        CardSet foundCard = cardSetService.getById(cardSet.getId().toString());

        assertThat(foundCard != null);
        assertEquals(foundCard,cardSet);
    }
    @Test
    public void testListAllCardSets(){
        CardSet cardSet1 = new CardSet();
        cardSet1.setId(UUID.randomUUID());
        cardSet1.setName("kokot1");

        list.add(cardSet);
        list.add(cardSet1);


        doReturn(list).when(repository).findAll();
        Iterable<CardSet> foundCardSets = cardSetService.findAll();

        assertThat(foundCardSets != null);
        assertThat(((List<CardSet>) foundCardSets).size() > 0);
        assertThat(((List<CardSet>) foundCardSets).contains(cardSet) && ((List<CardSet>) foundCardSets).contains(cardSet1));
    }

//    @Test
//    public void testAddCardSet(){
//
//    }
//    @Test
//    public void testDeleteCardSet(){
//
//    }
}
