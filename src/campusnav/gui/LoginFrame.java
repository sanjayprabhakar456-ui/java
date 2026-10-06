package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends BaseFrame implements ActionListener {

    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton loginBtn = createButton("Login");
    private JButton backBtn = createButton("Back", GREY);

    public LoginFrame() {
        super("Login", 380, 300);

        usernameField.setFont(FONT);
        passwordField.setFont(FONT);

        JPanel body = createBody(new GridLayout(2, 2, 10, 20));
        body.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        body.add(new JLabel("Username"));
        body.add(usernameField);
        body.add(new JLabel("Password"));
        body.add(passwordField);
        add(body, BorderLayout.CENTER);

        JPanel bar = createButtonBar();
        bar.add(backBtn);
        bar.add(loginBtn);
        add(bar, BorderLayout.SOUTH);

        loginBtn.addActionListener(this);
        backBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            dispose();
            Navigation.showWelcome();
            return;
        }
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        try {
            String role = authenticate(username, password);
            Session.login(username);
            dispose();
            if (role.equals("admin")) {
                Navigation.showAdmin();
            } else {
                Navigation.showReportIssue();
            }
        } catch (InvalidLoginException ex) {          // Exception handling
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // demo accounts; throws our own exception for wrong details
    private String authenticate(String user, String pass) throws InvalidLoginException {
        if (user.equals("admin") && pass.equals("admin123")) {
            return "admin";
        }
        if (user.equals("student") && pass.equals("1234")) {
            return "student";
        }
        throw new InvalidLoginException("Wrong username or password.");
    }
}
