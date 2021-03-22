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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/admin/card")
@Controller
public class AdminCardResource {

    @Autowired
    ICardService cardService;

    @RequestMapping("list")
    public String listCards(Model model) {
        List<Card> cardList = cardService.getAll();
        model.addAttribute("cards", cardList);
        model.addAttribute("hah", "mikijesupersnorchler");
        return "cardList.html";
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
        return "redirect:/admin/card/list";
    }
}
