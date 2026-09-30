package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RouteFrame extends JFrame {

    private JLabel titleLabel;

    private JTextArea routeArea;

    private JButton backButton;
    private JButton homeButton;

    public RouteFrame() {

        setTitle("CampusNav - Route");

        setSize(600, 450);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        titleLabel = new JLabel(
                "Route to Library",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        routeArea = new JTextArea();

        routeArea.setEditable(false);

        routeArea.setText(
                "Starting Point: Main Gate\n\n"
                + "1. Walk towards Main Block.\n"
                + "2. Enter the Main Block.\n"
                + "3. Go to the 2nd Floor.\n"
                + "4. Library is in Room 204."
        );

        backButton = new JButton("Back");

        homeButton = new JButton("Home");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(backButton);
        buttonPanel.add(homeButton);

        add(titleLabel, BorderLayout.NORTH);

        add(
                new JScrollPane(routeArea),
                BorderLayout.CENTER
        );

        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Navigation.showDestination();

                        dispose();
                    }
                }
        );

        homeButton.addActionListener(
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
