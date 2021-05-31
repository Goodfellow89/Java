package main;

import main.model.Deal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ToDoList {

    private static ConcurrentHashMap<Integer, Deal> deals = new ConcurrentHashMap<>();
    private static int currentId = 1;

    public static int add(Deal deal) {
        int id = currentId++;
        deal.setId(id);
        deals.put(id, deal);
        return id;
    }

    public static Deal get(int id) {
        if (deals.containsKey(id)) {
            return deals.get(id);
        }
        return null;
    }

    public static List<Deal> getAll() {
        return new ArrayList<>(deals.values());
    }

    public static String update(int id, Deal deal) {
        if (deals.containsKey(id)) {
            if (deal.getName() != null) {
                deals.get(id).setName(deal.getName());
            }
            if (deal.getDescription() != null) {
                deals.get(id).setDescription(deal.getDescription());
            }
            if (deal.getStatus() != null) {
                deals.get(id).setStatus(deal.getStatus());
            }
            return "OK";
        }
        return null;
    }

    public static void updateAll(Deal deal) {
        if (!deals.isEmpty()) {
            deals.keySet().forEach(id -> update(id, deal));
        }
    }

    public static void delete(int id) {
        deals.remove(id);
    }

    public static void deleteAll() {
        deals.clear();
    }
}