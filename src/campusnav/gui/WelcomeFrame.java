package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeFrame extends JFrame {

    private JLabel titleLabel;
    private JLabel subtitleLabel;

    private JButton startButton;
    private JButton exitButton;

    public WelcomeFrame() {

        setTitle("CampusNav - Welcome");

        setSize(600, 400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        titleLabel = new JLabel(
                "Welcome to CampusNav",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        subtitleLabel = new JLabel(
                "Campus Navigation System",
                SwingConstants.CENTER
        );

        startButton = new JButton("Start");

        exitButton = new JButton("Exit");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);

        add(subtitleLabel, BorderLayout.NORTH);

        add(titleLabel, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);

        startButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Navigation.showLogin();

                        dispose();
                    }
                }
        );

        exitButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        System.exit(0);
                    }
                }
        );
    }
}
