package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JLabel usernameLabel;
    private JLabel passwordLabel;

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JButton loginButton;
    private JButton adminButton;
    private JButton backButton;

    public LoginFrame() {

        setTitle("CampusNav - Login");

        setSize(500, 350);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new GridLayout(4, 2, 10, 10));

        usernameLabel = new JLabel("Username:");

        passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();

        passwordField = new JPasswordField();

        loginButton = new JButton("Student Login");

        adminButton = new JButton("Admin Login");

        backButton = new JButton("Back");

        add(usernameLabel);
        add(usernameField);

        add(passwordLabel);
        add(passwordField);

        add(loginButton);
        add(adminButton);

        add(backButton);

        loginButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Navigation.showHome();

                        dispose();
                    }
                }
        );

        adminButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Navigation.showAdmin();

                        dispose();
                    }
                }
        );

        backButton.addActionListener(
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
