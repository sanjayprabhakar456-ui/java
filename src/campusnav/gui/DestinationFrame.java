package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DestinationFrame extends JFrame {

    private JLabel titleLabel;

    private JTextArea destinationDetails;

    private JButton routeButton;
    private JButton backButton;

    public DestinationFrame() {

        setTitle("CampusNav - Destination");

        setSize(600, 400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        titleLabel = new JLabel(
                "Destination Details",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        destinationDetails = new JTextArea();

        destinationDetails.setEditable(false);

        destinationDetails.setText(
                "Destination: Library\n\n"
                + "Building: Main Block\n"
                + "Floor: 2nd Floor\n"
                + "Room: 204"
        );

        routeButton = new JButton("Get Route");

        backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(routeButton);
        buttonPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);

        add(
                new JScrollPane(destinationDetails),
                BorderLayout.CENTER
        );

        add(buttonPanel, BorderLayout.SOUTH);

        routeButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Navigation.showRoute();

                        dispose();
                    }
                }
        );

        backButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Navigation.showSearch();

                        dispose();
                    }
                }
        );
    }
}
