package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchFrame extends JFrame {

    private JLabel titleLabel;

    private JTextField searchField;

    private JButton searchButton;
    private JButton destinationButton;
    private JButton backButton;

    private JTextArea resultArea;

    public SearchFrame() {

        setTitle("CampusNav - Search");

        setSize(600, 450);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        titleLabel = new JLabel(
                "Find a Campus Destination",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        JPanel searchPanel = new JPanel();

        searchField = new JTextField(25);

        searchButton = new JButton("Search");

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        resultArea = new JTextArea();

        resultArea.setEditable(false);

        resultArea.setText(
                "Search results will appear here."
        );

        destinationButton =
                new JButton("View Destination");

        backButton =
                new JButton("Back");

        JPanel bottomPanel = new JPanel();

        bottomPanel.add(destinationButton);
        bottomPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);

        add(searchPanel, BorderLayout.CENTER);

        add(
                new JScrollPane(resultArea),
                BorderLayout.SOUTH
        );

        add(bottomPanel, BorderLayout.WEST);

        searchButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        resultArea.setText(
                                "Destination found:\n\n"
                                        + "Library\n"
                                        + "Main Block\n"
                                        + "2nd Floor"
                        );
                    }
                }
        );

        destinationButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Navigation.showDestination();

                        dispose();
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