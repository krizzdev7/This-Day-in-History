package com.thisdayhistory.ui;

import com.thisdayhistory.model.HistoricalFact;

import javax.swing.*;
import java.awt.*;

public class AddEditFactDialog extends JDialog {

    private final HistoricalFact fact;
    private boolean saved = false;

    public AddEditFactDialog(Frame owner, HistoricalFact fact) {
        super(owner, true);
        this.fact = fact;
        initUI();
    }

    private void initUI() {
        setTitle(fact.getId() == 0 ? "Add Fact" : "Edit Fact");
        setSize(400, 300);
        setLocationRelativeTo(getOwner());

        // Form fields
        // ... (add form fields for month, day, year, event, category, source)

        // Save and Cancel buttons
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // ... (validate and save the fact)
            saved = true;
            dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isSaved() {
        return saved;
    }

    public HistoricalFact getFact() {
        return fact;
    }
}
