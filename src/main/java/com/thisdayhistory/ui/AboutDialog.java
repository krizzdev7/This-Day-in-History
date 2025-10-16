package com.thisdayhistory.ui;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {

    public AboutDialog(Frame owner) {
        super(owner, "About This Day in History", true);
        initUI();
    }

    private void initUI() {
        setSize(300, 200);
        setLocationRelativeTo(getOwner());

        JLabel titleLabel = new JLabel("This Day in History");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel versionLabel = new JLabel("Version 1.0.0");
        versionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea infoArea = new JTextArea();
        infoArea.setText("A simple application to explore historical facts.");
        infoArea.setEditable(false);
        infoArea.setOpaque(false);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(titleLabel);
        panel.add(versionLabel);
        panel.add(infoArea);

        add(panel);
    }
}
