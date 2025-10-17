package com.thisdayhistory.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.thisdayhistory.dao.HistoricalFactDAO;
import com.thisdayhistory.model.HistoricalFact;

public class FactService {
    private static final Logger LOGGER = Logger.getLogger(FactService.class.getName());
    private final HistoricalFactDAO factDAO;
    private final Set<Integer> shownFactIds = new HashSet<>();

    public FactService(HistoricalFactDAO factDAO) {
        if (factDAO == null) {
            throw new IllegalArgumentException("FactDAO cannot be null");
        }
        this.factDAO = factDAO;
        LOGGER.info("FactService initialized");
    }

    public HistoricalFact getRandomFact(int month, int day) {
        validateDate(month, day);
        
        LOGGER.info(String.format("Getting random fact for date %d-%d", month, day));
        List<HistoricalFact> facts = factDAO.findByDate(month, day);
        
        if (facts.isEmpty()) {
            LOGGER.info("No facts found for the specified date");
            return null;
        }

        List<HistoricalFact> unshownFacts = facts.stream()
                .filter(f -> !shownFactIds.contains(f.getId()))
                .toList();

        if (unshownFacts.isEmpty()) {
            LOGGER.info("All facts have been shown, resetting shown facts list");
            shownFactIds.clear();
            unshownFacts = facts;
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(unshownFacts.size());
        HistoricalFact randomFact = unshownFacts.get(randomIndex);
        shownFactIds.add(randomFact.getId());
        
        LOGGER.info("Retrieved random fact: " + randomFact.getId());
        return randomFact;
    }

    public void markFavorite(int id, boolean favorite) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid fact ID: " + id);
        }

        LOGGER.info(String.format("Marking fact %d as %sfavorite", id, favorite ? "" : "not "));
        HistoricalFact fact = factDAO.findById(id);
        
        if (fact == null) {
            throw new IllegalArgumentException("Fact not found with ID: " + id);
        }

        fact.setFavorite(favorite);
        factDAO.update(fact);
    }
    
    public List<HistoricalFact> getFactsByDate(int month, int day) {
        validateDate(month, day);
        LOGGER.info(String.format("Getting facts for date %d-%d", month, day));
        return factDAO.findByDate(month, day);
    }

    public List<HistoricalFact> searchFacts(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        
        LOGGER.info("Searching facts with query: " + query);
        return factDAO.search(query.trim());
    }

    public int addFact(HistoricalFact fact) {
        validateFact(fact);
        LOGGER.info("Adding new historical fact: " + fact);
        return factDAO.create(fact);
    }

    public boolean updateFact(HistoricalFact fact) {
        if (fact == null) {
            throw new IllegalArgumentException("Fact cannot be null");
        }
        if (fact.getId() == null || fact.getId() <= 0) {
            throw new IllegalArgumentException("Invalid fact ID for update");
        }
        
        validateFact(fact);
        LOGGER.info("Updating historical fact: " + fact.getId());
        return factDAO.update(fact);
    }

    public boolean deleteFact(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid fact ID: " + id);
        }

        LOGGER.info("Deleting historical fact: " + id);
        return factDAO.delete(id);
    }

    public List<HistoricalFact> getFavorites() {
        LOGGER.info("Retrieving favorite facts");
        return factDAO.findFavorites();
    }

    public List<String> getCategories() {
        LOGGER.info("Retrieving all categories");
        return factDAO.getAllCategories();
    }

    private void validateDate(int month, int day) {
        if (!DateUtil.isValidDate(month, day, null)) {
            int maxDays = DateUtil.getDaysInMonth(month, null);
            throw new IllegalArgumentException(
                String.format("Date %d-%d is invalid. Day must be between 1 and %d for month %d",
                    month, day, maxDays, month));
        }
    }

    private void validateFact(HistoricalFact fact) {
        if (fact == null) {
            throw new IllegalArgumentException("Fact cannot be null");
        }

        validateDate(fact.getMonth(), fact.getDay());

        if (fact.getEvent() == null || fact.getEvent().trim().isEmpty()) {
            throw new IllegalArgumentException("Event description cannot be empty");
        }
        if (fact.getCategory() == null || fact.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        if (fact.getSource() == null || fact.getSource().trim().isEmpty()) {
            throw new IllegalArgumentException("Source cannot be empty");
        }
    }
}
