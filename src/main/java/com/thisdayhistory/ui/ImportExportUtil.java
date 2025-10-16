package com.thisdayhistory.ui;

import com.thisdayhistory.model.HistoricalFact;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class ImportExportUtil {

    public static void importCsv(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // ... (implement CSV import logic)
            JOptionPane.showMessageDialog(parent, "Imported from " + file.getName());
        }
    }

    public static void exportCsv(JFrame parent, List<HistoricalFact> facts) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // ... (implement CSV export logic)
            JOptionPane.showMessageDialog(parent, "Exported to " + file.getName());
        }
    }
}
