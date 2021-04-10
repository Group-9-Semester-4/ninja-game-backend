package com.group9.NinjaGame.resources.admin;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.services.ICardService;
import com.group9.NinjaGame.services.ICardSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/admin/card-set")
@Controller
public class CardSetResource {

    ICardSetService cardSetService;
    ICardService cardService;

    @Autowired
    public CardSetResource(ICardService cardService, ICardSetService cardSetService) {
        this.cardService = cardService;
        this.cardSetService = cardSetService;
    }


    @GetMapping("")
    public String listCardSets(Model model) {
        Iterable<CardSet> cardSets = cardSetService.findAll();
        model.addAttribute("cardSets", cardSets);
        return "manage-card-sets";
    }


    @GetMapping("/create")
    public String showCreateCardSetForm(Model model) {
        List<Card> cardList = cardService.listAll();
        model.addAttribute("cardSet", new CardSet());
        model.addAttribute("allCards", cardList);
        return "add-card-set";
    }

    @PostMapping("add")
    public String addCardSet(@Valid CardSet cardSet, BindingResult result) {
        if (result.hasErrors()) {
            return "add-card-set";
        }
        cardSetService.createCardSet(cardSet);
        return "redirect:/admin/card-set";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        CardSet cardSet = new CardSet();
        List<Card> cardList = new ArrayList<>();

        try {
            cardSet = cardSetService.getById(id);
            cardList = cardService.listAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid card set Id:" + id);
        }

        model.addAttribute("cardSet", cardSet);
        model.addAttribute("allCards", cardList);
        return "update-card-set";
    }

    @PostMapping("/update/{id}")
    public String updateCardSet(@PathVariable("id") String id, @Valid CardSet cardSet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            cardSet.setId(UUID.fromString(id));
            return "update-card-set";
        }
        cardSetService.createCardSet(cardSet);
        return "redirect:/admin/card-set";
    }


    @GetMapping("/delete/{id}")
    public String deleteCardSet(@PathVariable("id") String id, Model model) {
        CardSet cardSet = null;
        try {
            cardSet = cardSetService.getById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid card set Id:" + id);
        }

        cardSetService.deleteCardSet(cardSet);
        return "redirect:/admin/card-set";
    }

    @GetMapping("/manage-cards")
    //TODO: doesn't update automatically
    public String listCards(Model model) {
        List<Card> cardList = cardService.listAll();
        model.addAttribute("cards", cardList);
        return "manage-cards";
    }
}
