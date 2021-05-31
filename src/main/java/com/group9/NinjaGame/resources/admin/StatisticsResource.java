package com.group9.NinjaGame.resources.admin;

import com.group9.NinjaGame.services.IStatisticsService;
import com.group9.NinjaGame.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;

@RequestMapping("/admin/statistics")
@Controller
public class StatisticsResource {

    IStatisticsService statisticsService;

    @Autowired
    public StatisticsResource(IStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("")
    public String showStatisticsDefault(Model model) {
        return "redirect:/admin/statistics/1";
    }
    @GetMapping(value = "{pageNo}")
    public String showStatisticsPage(Model model, @PathVariable Integer pageNo) {
        pageNo -= 1;
        model.addAttribute("discards", statisticsService.getAllCardDiscards());
        model.addAttribute("redraws", statisticsService.getAllCardRedraws());
        model.addAttribute("gametime", statisticsService.getAllTimePlayedPerGame());
        model.addAttribute("avggametime", statisticsService.getAverageGameTime());
        model.addAttribute("countUsers", statisticsService.countUsers());
        if (pageNo == null) {
            model.addAttribute("users", statisticsService.getAllUsers(0));
        } else {
            model.addAttribute("users", statisticsService.getAllUsers(pageNo*25));
        }
        return "statistics";
    }
}
