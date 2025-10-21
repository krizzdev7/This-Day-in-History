package service;

import dao.EventDAO;
import dao.PersonDAO;
import model.Event;
import model.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * HistoryService Class
 * Business logic layer for historical data
 * Coordinates between DAOs and UI layer
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class HistoryService {
    private static final Logger LOGGER = Logger.getLogger(HistoryService.class.getName());
    private final EventDAO eventDAO;
    private final PersonDAO personDAO;

    /**
     * Constructor
     */
    public HistoryService() {
        this.eventDAO = new EventDAO();
        this.personDAO = new PersonDAO();
    }

    /**
     * Get all events for a specific date
     * 
     * @param date LocalDate to query
     * @return List of events
     */
    public List<Event> getEventsForDate(LocalDate date) {
        LOGGER.info("Fetching events for date: " + date);
        return eventDAO.getEventsByDate(date.getMonthValue(), date.getDayOfMonth());
    }

    /**
     * Get people born on a specific date
     * 
     * @param date LocalDate to query
     * @return List of people
     */
    public List<Person> getBirthsForDate(LocalDate date) {
        LOGGER.info("Fetching births for date: " + date);
        return personDAO.getPeopleBornOnDate(date.getMonthValue(), date.getDayOfMonth());
    }

    /**
     * Get people who died on a specific date
     * 
     * @param date LocalDate to query
     * @return List of people
     */
    public List<Person> getDeathsForDate(LocalDate date) {
        LOGGER.info("Fetching deaths for date: " + date);
        return personDAO.getPeopleDiedOnDate(date.getMonthValue(), date.getDayOfMonth());
    }

    /**
     * Get today's events
     * 
     * @return List of events for today
     */
    public List<Event> getTodaysEvents() {
        LocalDate today = LocalDate.now();
        return getEventsForDate(today);
    }

    /**
     * Get today's births
     * 
     * @return List of people born today
     */
    public List<Person> getTodaysBirths() {
        LocalDate today = LocalDate.now();
        return getBirthsForDate(today);
    }

    /**
     * Get today's deaths
     * 
     * @return List of people who died today
     */
    public List<Person> getTodaysDeaths() {
        LocalDate today = LocalDate.now();
        return getDeathsForDate(today);
    }

    /**
     * Search events by keyword
     * 
     * @param keyword Search term
     * @return List of matching events
     */
    public List<Event> searchEvents(String keyword) {
        LOGGER.info("Searching events with keyword: " + keyword);
        return eventDAO.searchEvents(keyword);
    }

    /**
     * Search people by keyword
     * 
     * @param keyword Search term
     * @return List of matching people
     */
    public List<Person> searchPeople(String keyword) {
        LOGGER.info("Searching people with keyword: " + keyword);
        return personDAO.searchPeople(keyword);
    }

    /**
     * Get events by category
     * 
     * @param category Category name
     * @return List of events in that category
     */
    public List<Event> getEventsByCategory(String category) {
        LOGGER.info("Fetching events for category: " + category);
        return eventDAO.getEventsByCategory(category);
    }

    /**
     * Get people by category
     * 
     * @param category Category name
     * @return List of people in that category
     */
    public List<Person> getPeopleByCategory(String category) {
        LOGGER.info("Fetching people for category: " + category);
        return personDAO.getPeopleByCategory(category);
    }

    /**
     * Get all event categories
     * 
     * @return List of category names
     */
    public List<String> getEventCategories() {
        return eventDAO.getAllCategories();
    }

    /**
     * Get all person categories
     * 
     * @return List of category names
     */
    public List<String> getPersonCategories() {
        return personDAO.getAllCategories();
    }

    /**
     * Get all events
     * 
     * @return List of all events
     */
    public List<Event> getAllEvents() {
        return eventDAO.getAllEvents();
    }

    /**
     * Get all people
     * 
     * @return List of all people
     */
    public List<Person> getAllPeople() {
        return personDAO.getAllPeople();
    }

    /**
     * Get event by ID
     * 
     * @param id Event ID
     * @return Event object
     */
    public Event getEventById(int id) {
        return eventDAO.getEventById(id);
    }

    /**
     * Get person by ID
     * 
     * @param id Person ID
     * @return Person object
     */
    public Person getPersonById(int id) {
        return personDAO.getPersonById(id);
    }

    /**
     * Get events for a date range
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return List of events in range
     */
    public List<Event> getEventsInDateRange(LocalDate startDate, LocalDate endDate) {
        LOGGER.info(String.format("Fetching events from %s to %s", startDate, endDate));
        return eventDAO.getEventsByDateRange(
            startDate.getMonthValue(), startDate.getDayOfMonth(),
            endDate.getMonthValue(), endDate.getDayOfMonth()
        );
    }

    /**
     * Get statistics about the database
     * 
     * @return String with statistics
     */
    public String getStatistics() {
        int eventCount = eventDAO.getEventCount();
        int personCount = personDAO.getPersonCount();
        int categoryCount = eventDAO.getAllCategories().size();

        return String.format("Database Statistics:\n" +
                           "Total Events: %d\n" +
                           "Total People: %d\n" +
                           "Event Categories: %d",
                           eventCount, personCount, categoryCount);
    }

    /**
     * Get a random historical fact (event)
     * 
     * @return Random event
     */
    public Event getRandomFact() {
        List<Event> allEvents = eventDAO.getAllEvents();
        if (!allEvents.isEmpty()) {
            int randomIndex = (int) (Math.random() * allEvents.size());
            return allEvents.get(randomIndex);
        }
        return null;
    }

    /**
     * Add a new event
     * 
     * @param event Event to add
     * @return Generated ID
     */
    public int addEvent(Event event) {
        LOGGER.info("Adding new event: " + event.getTitle());
        return eventDAO.insertEvent(event);
    }

    /**
     * Add a new person
     * 
     * @param person Person to add
     * @return Generated ID
     */
    public int addPerson(Person person) {
        LOGGER.info("Adding new person: " + person.getName());
        return personDAO.insertPerson(person);
    }

    /**
     * Update an event
     * 
     * @param event Event to update
     * @return true if successful
     */
    public boolean updateEvent(Event event) {
        LOGGER.info("Updating event: " + event.getId());
        return eventDAO.updateEvent(event);
    }

    /**
     * Update a person
     * 
     * @param person Person to update
     * @return true if successful
     */
    public boolean updatePerson(Person person) {
        LOGGER.info("Updating person: " + person.getId());
        return personDAO.updatePerson(person);
    }

    /**
     * Delete an event
     * 
     * @param id Event ID
     * @return true if successful
     */
    public boolean deleteEvent(int id) {
        LOGGER.info("Deleting event: " + id);
        return eventDAO.deleteEvent(id);
    }

    /**
     * Delete a person
     * 
     * @param id Person ID
     * @return true if successful
     */
    public boolean deletePerson(int id) {
        LOGGER.info("Deleting person: " + id);
        return personDAO.deletePerson(id);
    }
}
