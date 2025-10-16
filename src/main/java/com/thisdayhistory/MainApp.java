package com.thisdayhistory;

import com.thisdayhistory.dao.HistoricalFactDAO;
import com.thisdayhistory.service.FactService;
import com.thisdayhistory.ui.MainFrame;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HistoricalFactDAO dao = new HistoricalFactDAO();
            FactService service = new FactService(dao);
            MainFrame frame = new MainFrame(service);
            frame.setVisible(true);
        });
    }
}
