package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeFrame extends JFrame {

    private JLabel titleLabel;

    private JButton searchButton;
    private JButton reportButton;
    private JButton logoutButton;

    public HomeFrame() {

        setTitle("CampusNav - Home");

        setSize(600, 400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        titleLabel = new JLabel(
                "CampusNav Home",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        searchButton =
                new JButton("Find Destination");

        reportButton =
                new JButton("Report Issue");

        logoutButton =
                new JButton("Logout");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(searchButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(logoutButton);

        add(titleLabel, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Navigation.showSearch();

                        dispose();
                    }
                }
        );

        reportButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Navigation.showReportIssue();

                        dispose();
                    }
                }
        );

        logoutButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Navigation.showWelcome();

                        dispose();
                    }
                }
        );
    }
}
