package com.group9.NinjaGame.resources.admin;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.services.CardService;
import com.group9.NinjaGame.services.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/admin/card")
@Controller
public class AdminCardResource {

    @Autowired
    ICardService cardService;

    @GetMapping("/index")
    //TODO: doesn't update automatically
    public String listCards(Model model) {
        List<Card> cardList = cardService.getAll();
        model.addAttribute("cards", cardList);
        model.addAttribute("hah", "mikijesupersnorchler");
        return "index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(CardEntity cardEntity) {
        return "add-card.html";
    }

    @PostMapping("add")
    public String addCard(@Valid CardEntity cardEntity, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-card.html";
        }

        cardService.addCard(cardEntity);
        return "redirect:/admin/card/index";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        CardEntity cardEntity = null;
        try {
            cardEntity = cardService.getEntityById(id);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid user Id:" + id);
        }

        model.addAttribute("cardEntity", cardEntity);
        return "update-card";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") String id, @Valid CardEntity cardEntity,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            cardEntity.setId(UUID.fromString(id));
            return "update-card";
        }

        cardService.addCard(cardEntity);
        return "redirect:/admin/card/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") String id, Model model) {
        CardEntity cardEntity = null;
        try {
            cardEntity = cardService.getEntityById(id);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid user Id:" + id);
        }

        cardService.deleteCard(cardEntity);
        return "redirect:/admin/card/index";
    }


}
