package com.thisdayhistory.ui;

import com.thisdayhistory.model.HistoricalFact;
import com.thisdayhistory.service.FactService;
import com.thisdayhistory.util.CsvUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportExportUtil {
    private static final Logger LOGGER = Logger.getLogger(ImportExportUtil.class.getName());
    private static final String DEFAULT_EXTENSION = "csv";
    private static final String CSV_DESCRIPTION = "CSV Files (*.csv)";

    public static void importFacts(JFrame parent, FactService factService) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(CSV_DESCRIPTION, DEFAULT_EXTENSION));

        int option = fileChooser.showOpenDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileReader fr = new FileReader(file)) {
                List<HistoricalFact> importedFacts = CsvUtil.importFacts(fr);
                int imported = 0;
                List<String> errors = new ArrayList<>();

                // Import each fact
                for (HistoricalFact fact : importedFacts) {
                    try {
                        factService.addFact(fact);
                        imported++;
                    } catch (Exception e) {
                        errors.add(String.format("Error importing fact for %d/%d/%d: %s", 
                            fact.getMonth(), fact.getDay(), fact.getYear(), e.getMessage()));
                    }
                }

                // Show results
                if (errors.isEmpty()) {
                    JOptionPane.showMessageDialog(parent,
                        String.format("Successfully imported %d facts from %s", imported, file.getName()),
                        "Import Complete",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Imported %d facts with %d errors:\n\n", 
                        imported, errors.size()));
                    for (String error : errors) {
                        message.append(error).append("\n");
                    }

                    JTextArea textArea = new JTextArea(message.toString());
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 300));

                    JOptionPane.showMessageDialog(parent,
                        scrollPane,
                        "Import Results",
                        JOptionPane.WARNING_MESSAGE);
                }

            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error importing facts", e);
                JOptionPane.showMessageDialog(parent,
                    "Error importing facts: " + e.getMessage(),
                    "Import Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void exportFacts(JFrame parent, FactService factService) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(CSV_DESCRIPTION, DEFAULT_EXTENSION));

        int option = fileChooser.showSaveDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Ensure file has .csv extension
            if (!file.getName().toLowerCase().endsWith("." + DEFAULT_EXTENSION)) {
                file = new File(file.getPath() + "." + DEFAULT_EXTENSION);
            }

            try (FileWriter fw = new FileWriter(file)) {
                List<HistoricalFact> facts = factService.getAllFacts();
                CsvUtil.exportFacts(fw, facts);
                JOptionPane.showMessageDialog(parent,
                    String.format("Successfully exported %d facts to %s", facts.size(), file.getName()),
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error exporting facts", e);
                JOptionPane.showMessageDialog(parent,
                    "Error exporting facts: " + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
