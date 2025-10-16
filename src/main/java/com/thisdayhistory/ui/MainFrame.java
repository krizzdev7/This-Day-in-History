package com.thisdayhistory.ui;

import com.thisdayhistory.service.FactService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final FactService factService;

    public MainFrame(FactService factService) {
        this.factService = factService;
        initUI();
    }

    private void initUI() {
        setTitle("This Day in History");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        mainPanel.add(toolBar, BorderLayout.NORTH);

        // Center Panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Status Bar
        JLabel statusBar = new JLabel("Status: Ready");
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
