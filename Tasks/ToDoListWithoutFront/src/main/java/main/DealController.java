package main;

import main.model.Deal;
import main.model.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/deals/")
public class DealController {

    @Autowired
    private DealRepository toDoList;

    @PostMapping
    public int add(Deal deal) {
        Deal currentDeal = (Deal) toDoList.save(deal);
        return currentDeal.getId();
    }

    @PostMapping("/{id}")
    public ResponseEntity addingError() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable int id) {
        Optional deal = toDoList.findById(id);
        if (!deal.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(deal.get(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAll() {
        Iterable deals = toDoList.findAll();
        List<Deal> list = new ArrayList<>();
        deals.forEach(d -> list.add((Deal) d));
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List is still empty...");
        }
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable int id, Deal deal) {
        if (toDoList.findById(id).isPresent()) {
            Deal updatedDeal = toDoList.findById(id).get();
            if (deal.getName() != null) {
                updatedDeal.setName(deal.getName());
            }
            if (deal.getDescription() != null) {
                updatedDeal.setDescription(deal.getDescription());
            }
            if (deal.getStatus() != null) {
                updatedDeal.setStatus(deal.getStatus());
            }
            toDoList.save(updatedDeal);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PutMapping
    public void updateAll(Deal deal) {
        toDoList.findAll().forEach(d -> update(d.getId(), deal));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        toDoList.deleteById(id);
    }

    @DeleteMapping
    public void deleteAll() {
        toDoList.deleteAll();
    }
}