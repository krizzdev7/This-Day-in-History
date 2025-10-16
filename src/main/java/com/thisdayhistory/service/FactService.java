package com.thisdayhistory.service;

import com.thisdayhistory.dao.HistoricalFactDAO;
import com.thisdayhistory.model.HistoricalFact;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class FactService {

    private final HistoricalFactDAO factDAO;
    private final Set<Integer> shownFactIds = new HashSet<>();

    public FactService(HistoricalFactDAO factDAO) {
        this.factDAO = factDAO;
    }

    public HistoricalFact getRandomFact(int month, int day) {
        List<HistoricalFact> facts = factDAO.findByDate(month, day);
        if (facts.isEmpty()) {
            return null;
        }

        List<HistoricalFact> unshownFacts = new java.util.ArrayList<>(facts.stream()
                .filter(f -> !shownFactIds.contains(f.getId()))
                .toList());

        if (unshownFacts.isEmpty()) {
            shownFactIds.clear();
            unshownFacts.addAll(facts);
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(unshownFacts.size());
        HistoricalFact randomFact = unshownFacts.get(randomIndex);
        shownFactIds.add(randomFact.getId());
        return randomFact;
    }

    public void markFavorite(int id, boolean favorite) {
        HistoricalFact fact = factDAO.findById(id);
        if (fact != null) {
            fact.setFavorite(favorite);
            factDAO.update(fact);
        }
    }
    
    public List<HistoricalFact> getFactsByDate(int month, int day) {
        return factDAO.findByDate(month, day);
    }

    public List<HistoricalFact> searchFacts(String query) {
        return factDAO.search(query);
    }

    public int addFact(HistoricalFact fact) {
        return factDAO.create(fact);
    }

    public boolean updateFact(HistoricalFact fact) {
        return factDAO.update(fact);
    }

    public boolean deleteFact(int id) {
        return factDAO.delete(id);
    }

    public List<HistoricalFact> getFavorites() {
        return factDAO.findFavorites();
    }
}
