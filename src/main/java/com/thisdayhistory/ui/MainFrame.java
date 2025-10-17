package com.thisdayhistory.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
    private static final Dimension WINDOW_SIZE = new Dimension(1024, 768);
    
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
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton randomButton;
    private JComboBox<String> categoryFilter;
    private JToggleButton favoritesToggle;
    
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

        // Initialize table
        factTable = new JTable(factTableModel);
        factTable.setRowSorter(rowSorter);
        factTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        factTable.getSelectionModel().addListSelectionListener(e -> updateButtonStates());

        createMenuBar();
        
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        mainPanel.add(createToolBar(), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        
        statusBar = new JLabel("Ready");
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
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

        // Date selection components
        monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));

        monthSpinner.addChangeListener(e -> updateFactsForCurrentDate());
        daySpinner.addChangeListener(e -> updateFactsForCurrentDate());

        toolbar.add(new JLabel("Month: "));
        toolbar.add(monthSpinner);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(new JLabel("Day: "));
        toolbar.add(daySpinner);
        toolbar.add(Box.createHorizontalStrut(20));

        // Search components
        searchField = new JTextField(20);
        searchField.addActionListener(e -> performSearch());
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());

        toolbar.add(new JLabel("Search: "));
        toolbar.add(searchField);
        toolbar.add(searchButton);
        toolbar.add(Box.createHorizontalStrut(20));

        // Category filter
        categoryFilter = new JComboBox<>(new String[]{"All Categories", "Politics", "Science", "Arts", "Sports"});
        categoryFilter.addActionListener(e -> updateFactsForCurrentDate());
        
        toolbar.add(new JLabel("Category: "));
        toolbar.add(categoryFilter);
        toolbar.add(Box.createHorizontalStrut(20));

        // Favorites toggle
        favoritesToggle = new JToggleButton("Favorites");
        favoritesToggle.addActionListener(e -> updateFactsForCurrentDate());
        toolbar.add(favoritesToggle);
        toolbar.add(Box.createHorizontalStrut(20));

        // CRUD operation buttons
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        randomButton = new JButton("Random");

        addButton.addActionListener(e -> addFact());
        editButton.addActionListener(e -> editFact());
        deleteButton.addActionListener(e -> deleteFact());
        randomButton.addActionListener(e -> loadRandomFact());

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(randomButton);

        updateButtonStates();

        return toolbar;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Create table scroll pane
        JScrollPane tableScrollPane = new JScrollPane(factTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Historical Facts"));
        
        // Create details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Details"));
        
        // Create split pane
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            tableScrollPane,
            detailsPanel
        );
        splitPane.setResizeWeight(0.7);
        
        centerPanel.add(splitPane, BorderLayout.CENTER);
        return centerPanel;
    }

    private void updateButtonStates() {
        boolean hasSelection = factTable.getSelectedRow() != -1;
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
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
                statusBar.setText("Loading facts...");
                String category = categoryFilter.getSelectedIndex() == 0 ? null : 
                    (String) categoryFilter.getSelectedItem();
                boolean favoritesOnly = favoritesToggle.isSelected();
                return factService.getFactsForDate(month, day, category, favoritesOnly);
            }

            @Override
            protected void done() {
                try {
                    List<HistoricalFact> facts = get();
                    factTableModel.setFacts(facts);
                    statusBar.setText(String.format("Loaded %d facts for %d/%d", facts.size(), month, day));
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.log(Level.SEVERE, "Error loading facts", e);
                    statusBar.setText("Error loading facts");
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
                statusBar.setText("Searching...");
                return factService.searchFacts(searchTerm);
            }

            @Override
            protected void done() {
                try {
                    List<HistoricalFact> facts = get();
                    factTableModel.setFacts(facts);
                    statusBar.setText(String.format("Found %d matching facts", facts.size()));
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.log(Level.SEVERE, "Error searching facts", e);
                    statusBar.setText("Error searching facts");
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
                statusBar.setText("Loading random fact...");
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
