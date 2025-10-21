package service;

import dao.QuoteDAO;
import model.Quote;

import java.util.List;
import java.util.logging.Logger;

/**
 * QuoteService Class
 * Business logic layer for quote operations
 * 
 * @author This Day in History Team
 * @version 1.0
 */
public class QuoteService {
    private static final Logger LOGGER = Logger.getLogger(QuoteService.class.getName());
    private final QuoteDAO quoteDAO;
    private Quote cachedQuote;

    /**
     * Constructor
     */
    public QuoteService() {
        this.quoteDAO = new QuoteDAO();
    }

    /**
     * Get quote of the day (random quote, cached)
     * 
     * @return Quote object
     */
    public Quote getQuoteOfTheDay() {
        if (cachedQuote == null) {
            cachedQuote = quoteDAO.getRandomQuote();
            LOGGER.info("Loaded quote of the day");
        }
        return cachedQuote;
    }

    /**
     * Get a fresh random quote
     * 
     * @return Quote object
     */
    public Quote getRandomQuote() {
        Quote quote = quoteDAO.getRandomQuote();
        LOGGER.info("Retrieved random quote");
        return quote;
    }

    /**
     * Refresh the quote of the day
     * 
     * @return New quote
     */
    public Quote refreshQuoteOfTheDay() {
        cachedQuote = quoteDAO.getRandomQuote();
        LOGGER.info("Refreshed quote of the day");
        return cachedQuote;
    }

    /**
     * Get all quotes
     * 
     * @return List of all quotes
     */
    public List<Quote> getAllQuotes() {
        return quoteDAO.getAllQuotes();
    }

    /**
     * Get quotes by author
     * 
     * @param author Author name
     * @return List of quotes
     */
    public List<Quote> getQuotesByAuthor(String author) {
        LOGGER.info("Fetching quotes for author: " + author);
        return quoteDAO.getQuotesByAuthor(author);
    }

    /**
     * Search quotes
     * 
     * @param keyword Search term
     * @return List of matching quotes
     */
    public List<Quote> searchQuotes(String keyword) {
        LOGGER.info("Searching quotes with keyword: " + keyword);
        return quoteDAO.searchQuotes(keyword);
    }

    /**
     * Get quote by ID
     * 
     * @param id Quote ID
     * @return Quote object
     */
    public Quote getQuoteById(int id) {
        return quoteDAO.getQuoteById(id);
    }

    /**
     * Get all authors
     * 
     * @return List of author names
     */
    public List<String> getAllAuthors() {
        return quoteDAO.getAllAuthors();
    }

    /**
     * Get random quote by author
     * 
     * @param author Author name
     * @return Quote object
     */
    public Quote getRandomQuoteByAuthor(String author) {
        LOGGER.info("Fetching random quote for author: " + author);
        return quoteDAO.getRandomQuoteByAuthor(author);
    }

    /**
     * Add a new quote
     * 
     * @param quote Quote to add
     * @return Generated ID
     */
    public int addQuote(Quote quote) {
        LOGGER.info("Adding new quote by: " + quote.getAuthor());
        return quoteDAO.insertQuote(quote);
    }

    /**
     * Update a quote
     * 
     * @param quote Quote to update
     * @return true if successful
     */
    public boolean updateQuote(Quote quote) {
        LOGGER.info("Updating quote: " + quote.getId());
        return quoteDAO.updateQuote(quote);
    }

    /**
     * Delete a quote
     * 
     * @param id Quote ID
     * @return true if successful
     */
    public boolean deleteQuote(int id) {
        LOGGER.info("Deleting quote: " + id);
        return quoteDAO.deleteQuote(id);
    }

    /**
     * Get total quote count
     * 
     * @return Number of quotes
     */
    public int getQuoteCount() {
        return quoteDAO.getQuoteCount();
    }
}
