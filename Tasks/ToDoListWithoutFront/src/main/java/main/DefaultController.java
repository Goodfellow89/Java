package main;

import main.model.Deal;
import main.model.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DefaultController {

    @Autowired
    private DealRepository repository;

    @RequestMapping("/")
    public String index(Model model) {

        Iterable deals = repository.findAll();
        List<Deal> list = new ArrayList<>();
        deals.forEach(d -> list.add((Deal) d));

        model.addAttribute("toDoList", list);
        return "index";
    }
}
