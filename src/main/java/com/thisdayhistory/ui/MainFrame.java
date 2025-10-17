package com.thisdayhistory.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.table.TableRowSorter;

import com.thisdayhistory.model.HistoricalFact;
import com.thisdayhistory.service.FactService;
import com.thisdayhistory.util.DateUtil;

public class MainFrame extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(MainFrame.class.getName());
    private static final String WINDOW_TITLE = "This Day in History";
    private static final Dimension WINDOW_SIZE = new Dimension(1200, 800);

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;

    private final FactService factService;
    private final FactTableModel factTableModel;
    private final TableRowSorter<FactTableModel> rowSorter;

    // UI Components
    private JTable factTable;
    private JLabel statusBar;
    private JSpinner monthSpinner;
    private JSpinner daySpinner;
    private JTextField searchField;
    private JButton searchButton;
    private JButton todayButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton randomButton;
    private JButton refreshButton;
    private JButton favoriteButton;
    private JComboBox<String> categoryFilter;
    private JToggleButton favoritesToggle;

    // Details panel components
    private JTextArea detailsEventText;
    private JLabel detailsDateLabel;
    private JLabel detailsYearLabel;
    private JLabel detailsCategoryLabel;
    private JLabel detailsSourceLabel;
    private JPanel detailsPanel;

    public MainFrame(FactService factService) {
        if (factService == null) {
            throw new IllegalArgumentException("FactService cannot be null");
        }
        this.factService = factService;
        this.factTableModel = new FactTableModel();
        this.rowSorter = new TableRowSorter<>(factTableModel);
        initUI();

        // Load initial data for today
        LocalDate today = LocalDate.now();
        updateSpinners(today.getMonthValue(), today.getDayOfMonth());
        loadFactsForDate(today.getMonthValue(), today.getDayOfMonth());
    }

    private void initUI() {
        setTitle(WINDOW_TITLE);
        setSize(WINDOW_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize table with improved styling
        factTable = new JTable(factTableModel);
        factTable.setRowSorter(rowSorter);
        factTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        factTable.setRowHeight(28);
        factTable.setShowGrid(true);
        factTable.setGridColor(new Color(220, 220, 220));
        factTable.setIntercellSpacing(new Dimension(1, 1));
        factTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        factTable.getTableHeader().setBackground(PRIMARY_COLOR);
        factTable.getTableHeader().setForeground(Color.WHITE);
        factTable.setSelectionBackground(ACCENT_COLOR);
        factTable.setSelectionForeground(Color.WHITE);
        factTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
                updateDetailsPanel();
            }
        });

        createMenuBar();

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createToolBar(), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);

        statusBar = new JLabel(" Ready");
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        statusBar.setFont(new Font("SansSerif", Font.PLAIN, 11));
        statusBar.setBackground(CARD_COLOR);
        statusBar.setOpaque(true);
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Register keyboard shortcuts
        registerKeyboardShortcuts();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem importItem = new JMenuItem("Import CSV...", KeyEvent.VK_I);
        JMenuItem exportItem = new JMenuItem("Export CSV...", KeyEvent.VK_E);
        JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);

        importItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));
        exportItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));

        importItem.addActionListener(e -> importFacts());
        exportItem.addActionListener(e -> exportFacts());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(importItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        JMenuItem addItem = new JMenuItem("Add Fact...", KeyEvent.VK_A);
        JMenuItem editItem = new JMenuItem("Edit Fact...", KeyEvent.VK_D);
        JMenuItem deleteItem = new JMenuItem("Delete Fact", KeyEvent.VK_L);

        addItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

        addItem.addActionListener(e -> addFact());
        editItem.addActionListener(e -> editFact());
        deleteItem.addActionListener(e -> deleteFact());

        editMenu.add(addItem);
        editMenu.add(editItem);
        editMenu.add(deleteItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_A);
        aboutItem.addActionListener(e -> showAboutDialog());

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private JToolBar createToolBar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(CARD_COLOR);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        // Date selection panel
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setOpaque(false);

        monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));

        monthSpinner.setPreferredSize(new Dimension(60, 28));
        daySpinner.setPreferredSize(new Dimension(60, 28));

        monthSpinner.addChangeListener(e -> updateFactsForCurrentDate());
        daySpinner.addChangeListener(e -> updateFactsForCurrentDate());

        datePanel.add(new JLabel("Month:"));
        datePanel.add(monthSpinner);
        datePanel.add(Box.createHorizontalStrut(5));
        datePanel.add(new JLabel("Day:"));
        datePanel.add(daySpinner);

        todayButton = createStyledButton("Today", SUCCESS_COLOR);
        todayButton.addActionListener(e -> loadToday());
        datePanel.add(Box.createHorizontalStrut(10));
        datePanel.add(todayButton);

        toolbar.add(datePanel);
        toolbar.add(Box.createHorizontalStrut(15));
        toolbar.addSeparator();
        toolbar.add(Box.createHorizontalStrut(15));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.setOpaque(false);

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 28));
        searchField.addActionListener(e -> performSearch());
        searchButton = createStyledButton("Search", ACCENT_COLOR);
        searchButton.addActionListener(e -> performSearch());

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        toolbar.add(searchPanel);
        toolbar.add(Box.createHorizontalStrut(15));
        toolbar.addSeparator();
        toolbar.add(Box.createHorizontalStrut(15));

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filterPanel.setOpaque(false);

        categoryFilter = new JComboBox<>(new String[]{"All Categories", "Politics", "Science", "Arts", "Sports",
            "Births", "Crime", "Revolutions", "Assassinations", "Disasters", "Architecture", "Culture", "Military"});
        categoryFilter.setPreferredSize(new Dimension(150, 28));
        categoryFilter.addActionListener(e -> updateFactsForCurrentDate());

        favoritesToggle = new JToggleButton("★ Favorites");
        favoritesToggle.setPreferredSize(new Dimension(110, 28));
        favoritesToggle.setBackground(CARD_COLOR);
        favoritesToggle.addActionListener(e -> updateFactsForCurrentDate());

        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryFilter);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(favoritesToggle);

        toolbar.add(filterPanel);
        toolbar.add(Box.createHorizontalGlue());

        // Action buttons panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        actionsPanel.setOpaque(false);

        refreshButton = createStyledButton("↻ Refresh", PRIMARY_COLOR);
        refreshButton.addActionListener(e -> updateFactsForCurrentDate());

        randomButton = createStyledButton("🎲 Random", new Color(155, 89, 182));
        randomButton.addActionListener(e -> loadRandomFact());

        addButton = createStyledButton("+ Add", SUCCESS_COLOR);
        addButton.addActionListener(e -> addFact());

        editButton = createStyledButton("✎ Edit", ACCENT_COLOR);
        editButton.addActionListener(e -> editFact());

        deleteButton = createStyledButton("✕ Delete", DANGER_COLOR);
        deleteButton.addActionListener(e -> deleteFact());

        favoriteButton = createStyledButton("★", new Color(241, 196, 15));
        favoriteButton.addActionListener(e -> toggleFavorite());
        favoriteButton.setToolTipText("Toggle Favorite");

        actionsPanel.add(refreshButton);
        actionsPanel.add(randomButton);
        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        actionsPanel.add(favoriteButton);

        toolbar.add(actionsPanel);

        updateButtonStates();

        return toolbar;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(85, 28));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table scroll pane
        JScrollPane tableScrollPane = new JScrollPane(factTable);
        tableScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        "Historical Facts",
                        0,
                        0,
                        new Font("SansSerif", Font.BOLD, 13),
                        PRIMARY_COLOR
                ),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        tableScrollPane.setBackground(CARD_COLOR);

        // Create details panel
        detailsPanel = createDetailsPanel();

        // Create split pane
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                tableScrollPane,
                detailsPanel
        );
        splitPane.setResizeWeight(0.65);
        splitPane.setDividerSize(6);
        splitPane.setBorder(null);

        centerPanel.add(splitPane, BorderLayout.CENTER);
        return centerPanel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        "Fact Details",
                        0,
                        0,
                        new Font("SansSerif", Font.BOLD, 13),
                        PRIMARY_COLOR
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 10, 0);

        // Event text area
        detailsEventText = new JTextArea();
        detailsEventText.setLineWrap(true);
        detailsEventText.setWrapStyleWord(true);
        detailsEventText.setEditable(false);
        detailsEventText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        detailsEventText.setBackground(new Color(248, 249, 250));
        detailsEventText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(detailsEventText);
        scrollPane.setBorder(null);
        panel.add(scrollPane, gbc);

        // Date label
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(dateLabel, gbc);

        gbc.gridx = 1;
        detailsDateLabel = new JLabel();
        detailsDateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(detailsDateLabel, gbc);

        // Year label
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(yearLabel, gbc);

        gbc.gridx = 1;
        detailsYearLabel = new JLabel();
        detailsYearLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(detailsYearLabel, gbc);

        // Category label
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        detailsCategoryLabel = new JLabel();
        detailsCategoryLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(detailsCategoryLabel, gbc);

        // Source label
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel sourceLabel = new JLabel("Source:");
        sourceLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(sourceLabel, gbc);

        gbc.gridx = 1;
        detailsSourceLabel = new JLabel();
        detailsSourceLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(detailsSourceLabel, gbc);

        return panel;
    }

    private void updateDetailsPanel() {
        int selectedRow = factTable.getSelectedRow();
        if (selectedRow == -1) {
            clearDetailsPanel();
            return;
        }

        int modelRow = factTable.convertRowIndexToModel(selectedRow);
        HistoricalFact fact = factTableModel.getFactAt(modelRow);

        detailsEventText.setText(fact.getEvent());
        detailsDateLabel.setText(String.format("%s %d", getMonthName(fact.getMonth()), fact.getDay()));
        detailsYearLabel.setText(fact.getYear() != null ? String.valueOf(fact.getYear()) : "Unknown");
        detailsCategoryLabel.setText(fact.getCategory() != null ? fact.getCategory() : "None");
        detailsSourceLabel.setText(fact.getSource() != null ? fact.getSource() : "Unknown");

        favoriteButton.setText(fact.isFavorite() ? "★" : "☆");
    }

    private void clearDetailsPanel() {
        detailsEventText.setText("Select a fact to view details");
        detailsDateLabel.setText("");
        detailsYearLabel.setText("");
        detailsCategoryLabel.setText("");
        detailsSourceLabel.setText("");
    }

    private String getMonthName(int month) {
        return switch (month) {
            case 1 ->
                "January";
            case 2 ->
                "February";
            case 3 ->
                "March";
            case 4 ->
                "April";
            case 5 ->
                "May";
            case 6 ->
                "June";
            case 7 ->
                "July";
            case 8 ->
                "August";
            case 9 ->
                "September";
            case 10 ->
                "October";
            case 11 ->
                "November";
            case 12 ->
                "December";
            default ->
                "Unknown";
        };
    }

    private void updateButtonStates() {
        boolean hasSelection = factTable.getSelectedRow() != -1;
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
        favoriteButton.setEnabled(hasSelection);
    }

    private void loadToday() {
        LocalDate today = LocalDate.now();
        updateSpinners(today.getMonthValue(), today.getDayOfMonth());
        loadFactsForDate(today.getMonthValue(), today.getDayOfMonth());
        statusBar.setText(" Showing facts for today: " + getMonthName(today.getMonthValue()) + " " + today.getDayOfMonth());
    }

    private void toggleFavorite() {
        int selectedRow = factTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = factTable.convertRowIndexToModel(selectedRow);
        HistoricalFact fact = factTableModel.getFactAt(modelRow);

        // Toggle favorite status
        fact.setFavorite(!fact.isFavorite());
        factService.updateFact(fact);

        // Update UI
        factTableModel.fireTableRowsUpdated(modelRow, modelRow);
        updateDetailsPanel();
        statusBar.setText(fact.isFavorite()
                ? " Added to favorites" : " Removed from favorites");
    }

    private void registerKeyboardShortcuts() {
        // Add keyboard shortcuts
        KeyStroke ctrlN = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlE = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK);
        KeyStroke delete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);

        getRootPane().registerKeyboardAction(
                e -> addFact(),
                "Add Fact",
                ctrlN,
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        getRootPane().registerKeyboardAction(
                e -> editFact(),
                "Edit Fact",
                ctrlE,
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        getRootPane().registerKeyboardAction(
                e -> deleteFact(),
                "Delete Fact",
                delete,
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    private void updateSpinners(int month, int day) {
        monthSpinner.setValue(month);
        daySpinner.setValue(day);
    }

    private void loadFactsForDate(int month, int day) {
        SwingWorker<List<HistoricalFact>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<HistoricalFact> doInBackground() {
                statusBar.setText(" Loading facts...");
                String category = categoryFilter.getSelectedIndex() == 0 ? null
                        : (String) categoryFilter.getSelectedItem();
                boolean favoritesOnly = favoritesToggle.isSelected();
                return factService.getFactsForDate(month, day, category, favoritesOnly);
            }

            @Override
            protected void done() {
                try {
                    List<HistoricalFact> facts = get();
                    factTableModel.setFacts(facts);
                    statusBar.setText(String.format(" Loaded %d fact(s) for %s %d",
                            facts.size(), getMonthName(month), day));
                    clearDetailsPanel();
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.log(Level.SEVERE, "Error loading facts", e);
                    statusBar.setText(" Error loading facts");
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Error loading facts: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void updateFactsForCurrentDate() {
        int month = (Integer) monthSpinner.getValue();
        int day = (Integer) daySpinner.getValue();

        // Validate day based on month
        int maxDay = DateUtil.getMaxDayForMonth(month);
        if (day > maxDay) {
            daySpinner.setValue(maxDay);
            day = maxDay;
        }

        loadFactsForDate(month, day);
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            updateFactsForCurrentDate();
            return;
        }

        SwingWorker<List<HistoricalFact>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<HistoricalFact> doInBackground() {
                statusBar.setText(" Searching...");
                return factService.searchFacts(searchTerm);
            }

            @Override
            protected void done() {
                try {
                    List<HistoricalFact> facts = get();
                    factTableModel.setFacts(facts);
                    statusBar.setText(String.format(" Found %d matching fact(s)", facts.size()));
                    clearDetailsPanel();
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.log(Level.SEVERE, "Error searching facts", e);
                    statusBar.setText(" Error searching facts");
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Error searching facts: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void loadRandomFact() {
        SwingWorker<HistoricalFact, Void> worker = new SwingWorker<>() {
            @Override
            protected HistoricalFact doInBackground() {
                statusBar.setText(" Loading random fact...");
                int month = (Integer) monthSpinner.getValue();
                int day = (Integer) daySpinner.getValue();
                return factService.getRandomFact(month, day);
            }

            @Override
            protected void done() {
                try {
                    HistoricalFact fact = get();
                    if (fact != null) {
                        updateSpinners(fact.getMonth(), fact.getDay());
                        loadFactsForDate(fact.getMonth(), fact.getDay());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.log(Level.SEVERE, "Error loading random fact", e);
                    statusBar.setText("Error loading random fact");
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Error loading random fact: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void importFacts() {
        ImportExportUtil.importFacts(this, factService);
        updateFactsForCurrentDate();
    }

    private void exportFacts() {
        ImportExportUtil.exportFacts(this, factService);
    }

    private void addFact() {
        int month = (Integer) monthSpinner.getValue();
        int day = (Integer) daySpinner.getValue();

        AddEditFactDialog dialog = new AddEditFactDialog(this, "Add Historical Fact", month, day);
        dialog.setVisible(true);

        if (dialog.getResult() != null) {
            factService.addFact(dialog.getResult());
            updateFactsForCurrentDate();
        }
    }

    private void editFact() {
        int selectedRow = factTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a fact to edit.",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int modelRow = factTable.convertRowIndexToModel(selectedRow);
        HistoricalFact fact = factTableModel.getFactAt(modelRow);

        AddEditFactDialog dialog = new AddEditFactDialog(this, "Edit Historical Fact", fact);
        dialog.setVisible(true);

        if (dialog.getResult() != null) {
            factService.updateFact(dialog.getResult());
            updateFactsForCurrentDate();
        }
    }

    private void deleteFact() {
        int selectedRow = factTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a fact to delete.",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int modelRow = factTable.convertRowIndexToModel(selectedRow);
        HistoricalFact fact = factTableModel.getFactAt(modelRow);

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this historical fact?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            factService.deleteFact(fact.getId());
            updateFactsForCurrentDate();
        }
    }

    private void showAboutDialog() {
        AboutDialog dialog = new AboutDialog(this);
        dialog.setVisible(true);
    }
}
