package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminFrame extends JFrame {

    private JLabel titleLabel;

    private JTextArea reportArea;

    private JButton viewButton;
    private JButton backButton;
    private JButton logoutButton;

    public AdminFrame() {

        setTitle("CampusNav - Admin");

        setSize(700, 500);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        titleLabel = new JLabel(
                "Admin Dashboard",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 26)
        );

        reportArea = new JTextArea();

        reportArea.setEditable(false);

        reportArea.setText(
                "Reported issues will appear here."
        );

        viewButton =
                new JButton("View Reports");

        backButton =
                new JButton("Back");

        logoutButton =
                new JButton("Logout");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(viewButton);
        buttonPanel.add(backButton);
        buttonPanel.add(logoutButton);

        add(titleLabel, BorderLayout.NORTH);

        add(
                new JScrollPane(reportArea),
                BorderLayout.CENTER
        );

        add(buttonPanel, BorderLayout.SOUTH);

        viewButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        reportArea.setText(
                                "Sample Report 1\n"
                                + "Location: Main Block\n"
                                + "Issue: Broken Light\n\n"

                                + "Sample Report 2\n"
                                + "Location: CSE Block\n"
                                + "Issue: Damaged Door"
                        );
                    }
                }
        );

        backButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Navigation.showLogin();

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
