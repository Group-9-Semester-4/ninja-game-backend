package com.group9.NinjaGame.resources.admin;

import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.services.CardService;
import com.group9.NinjaGame.services.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
