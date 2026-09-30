package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportIssueFrame extends JFrame {

    private JLabel issueLabel;
    private JLabel locationLabel;
    private JLabel descriptionLabel;

    private JComboBox<String> issueBox;

    private JTextField locationField;

    private JTextArea descriptionArea;

    private JButton submitButton;
    private JButton backButton;

    public ReportIssueFrame() {

        setTitle("CampusNav - Report Issue");

        setSize(600, 450);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(
                new GridLayout(5, 2, 10, 10)
        );

        issueLabel =
                new JLabel("Issue Type:");

        locationLabel =
                new JLabel("Location:");

        descriptionLabel =
                new JLabel("Description:");

        issueBox =
                new JComboBox<>(
                        new String[]{
                                "Broken Light",
                                "Water Leakage",
                                "Damaged Door",
                                "Cleanliness",
                                "Other"
                        }
                );

        locationField =
                new JTextField();

        descriptionArea =
                new JTextArea();

        submitButton =
                new JButton("Submit");

        backButton =
                new JButton("Back");

        add(issueLabel);
        add(issueBox);

        add(locationLabel);
        add(locationField);

        add(descriptionLabel);
        add(
                new JScrollPane(
                        descriptionArea
                )
        );

        add(submitButton);
        add(backButton);

        submitButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        JOptionPane.showMessageDialog(
                                ReportIssueFrame.this,
                                "Issue submitted successfully."
                        );
                    }
                }
        );

        backButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Navigation.showHome();

                        dispose();
                    }
                }
        );
    }
}
