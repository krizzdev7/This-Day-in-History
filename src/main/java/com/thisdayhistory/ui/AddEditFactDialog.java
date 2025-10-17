package com.thisdayhistory.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.Year;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.thisdayhistory.model.HistoricalFact;
import com.thisdayhistory.util.DateUtil;

public class AddEditFactDialog extends JDialog {

    private static final String[] CATEGORIES = {"Politics", "Science", "Arts", "Sports", "Other"};
    private static final int MIN_YEAR = -3000; // Allow ancient history

    private HistoricalFact result;
    private final JSpinner monthSpinner;
    private final JSpinner daySpinner;
    private final JSpinner yearSpinner;
    private final JTextArea eventArea;
    private final JComboBox<String> categoryCombo;
    private final JCheckBox favoriteCheck;
    private final JTextField sourceField;

    public AddEditFactDialog(Frame owner, String title, int month, int day) {
        super(owner, title, true);
        this.monthSpinner = new JSpinner(new SpinnerNumberModel(Integer.valueOf(month), Integer.valueOf(1), Integer.valueOf(12), Integer.valueOf(1)));
        this.daySpinner = new JSpinner(new SpinnerNumberModel(Integer.valueOf(day), Integer.valueOf(1), Integer.valueOf(31), Integer.valueOf(1)));
        this.yearSpinner = new JSpinner(new SpinnerNumberModel(Integer.valueOf(2000), Integer.valueOf(MIN_YEAR), Integer.valueOf(Year.now().getValue()), Integer.valueOf(1)));
        this.eventArea = new JTextArea(5, 40);
        this.categoryCombo = new JComboBox<>(CATEGORIES);
        this.favoriteCheck = new JCheckBox("Favorite");
        this.sourceField = new JTextField(40);
        initUI();
    }

    public AddEditFactDialog(Frame owner, String title, HistoricalFact fact) {
        super(owner, true);
        setTitle(title);
        this.monthSpinner = new JSpinner(new SpinnerNumberModel(Integer.valueOf(fact.getMonth()), Integer.valueOf(1), Integer.valueOf(12), Integer.valueOf(1)));
        this.daySpinner = new JSpinner(new SpinnerNumberModel(Integer.valueOf(fact.getDay()), Integer.valueOf(1), Integer.valueOf(31), Integer.valueOf(1)));
        Integer yearValue = fact.getYear() != null ? fact.getYear() : Integer.valueOf(2000);
        this.yearSpinner = new JSpinner(new SpinnerNumberModel(yearValue, Integer.valueOf(MIN_YEAR), Integer.valueOf(Year.now().getValue()), Integer.valueOf(1)));
        this.eventArea = new JTextArea(5, 40);
        this.categoryCombo = new JComboBox<>(CATEGORIES);
        this.favoriteCheck = new JCheckBox("Favorite");
        this.sourceField = new JTextField(40);

        // Set existing values
        eventArea.setText(fact.getEvent());
        categoryCombo.setSelectedItem(fact.getCategory());
        favoriteCheck.setSelected(fact.isFavorite());
        sourceField.setText(fact.getSource());

        initUI();
    }

    private void initUI() {
        setSize(400, 300);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(5, 5));

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Date panel
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("Month:"));
        datePanel.add(monthSpinner);
        datePanel.add(Box.createHorizontalStrut(10));
        datePanel.add(new JLabel("Day:"));
        datePanel.add(daySpinner);
        datePanel.add(Box.createHorizontalStrut(10));
        datePanel.add(new JLabel("Year:"));
        datePanel.add(yearSpinner);

        gbc.gridwidth = 2;
        mainPanel.add(datePanel, gbc);

        // Event description
        gbc.gridy++;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Event:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        eventArea.setLineWrap(true);
        eventArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(eventArea);
        mainPanel.add(scrollPane, gbc);

        // Category
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(categoryCombo, gbc);

        // Source
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Source:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(sourceField, gbc);

        // Favorite checkbox
        gbc.gridy++;
        favoriteCheck.setHorizontalAlignment(SwingConstants.LEFT);
        mainPanel.add(favoriteCheck, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Add validation and input control
        monthSpinner.addChangeListener(e -> validateDate());
        daySpinner.addChangeListener(e -> validateDate());

        // Limit event description length
        ((AbstractDocument) eventArea.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (newText.length() <= 500) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Save and Cancel buttons
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                saveAndClose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Set default button
        getRootPane().setDefaultButton(saveButton);
    }

    private void validateDate() {
        int month = (Integer) monthSpinner.getValue();
        int day = (Integer) daySpinner.getValue();
        int maxDay = DateUtil.getMaxDayForMonth(month);

        if (day > maxDay) {
            daySpinner.setValue(maxDay);
        }
    }

    private boolean validateInput() {
        String event = eventArea.getText().trim();
        if (event.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an event description.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            eventArea.requestFocus();
            return false;
        }

        if (event.length() < 10) {
            JOptionPane.showMessageDialog(this,
                    "Event description must be at least 10 characters long.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            eventArea.requestFocus();
            return false;
        }

        String source = sourceField.getText().trim();
        if (source.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a source for the historical fact.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            sourceField.requestFocus();
            return false;
        }

        return true;
    }

    private void saveAndClose() {
        int month = (Integer) monthSpinner.getValue();
        int day = (Integer) daySpinner.getValue();
        int year = (Integer) yearSpinner.getValue();
        String event = eventArea.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        boolean favorite = favoriteCheck.isSelected();
        String source = sourceField.getText().trim();

        result = new HistoricalFact(0, month, day, year, event, category, favorite, source);
        dispose();
    }

    public HistoricalFact getResult() {
        return result;
    }
}
