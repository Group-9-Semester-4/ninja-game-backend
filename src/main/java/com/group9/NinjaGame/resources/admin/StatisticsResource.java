package com.group9.NinjaGame.resources.admin;

import com.group9.NinjaGame.services.IStatisticsService;
import com.group9.NinjaGame.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/statistics")
@Controller
public class StatisticsResource {

    IStatisticsService statisticsService;

    @Autowired
    public StatisticsResource(IStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("")
    public String showStatisticsPage(Model model) {
        model.addAttribute("discards", statisticsService.getAllCardDiscards());
        model.addAttribute("redraws", statisticsService.getAllCardRedraws());
        model.addAttribute("gametime", statisticsService.getAllTimePlayedPerGame());
        model.addAttribute("avggametime", statisticsService.getAverageGameTime());
        return "statistics";
    }
}
