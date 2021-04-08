package com.group9.NinjaGame.unit.services;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.services.ICardService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnit.class)
public class CardServiceTest {
    @Autowired
    private CardRepository repository;


    private Card card;

//    @BeforeAll
//    public void setUp(){
//
//    }

    @Test
    public void testGetEntityById(){
//        card = new Card();
//        card.setId(UUID.randomUUID());
//        card.setName("kokot");
//        repository.save(card);
//
//        Optional<Card> optionalCard = repository.findById(card.getId());
//        assertEquals(card.getId(), optionalCard.get().getId());
    }

    @Test
    public void testAddCard(){
//        Card card = new Card();
//        card.setName("kokot");

    }
    @Test
    public void testDeleteCard(){

    }
    @Test
    public void testListAll(){

    }
}
