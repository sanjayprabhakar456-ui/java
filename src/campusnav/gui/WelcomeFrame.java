package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeFrame extends BaseFrame implements ActionListener {

    private JButton navigateBtn = createButton("Navigate Campus");
    private JButton reportBtn = createButton("Report a Problem", new Color(230, 81, 0));
    private JButton exitBtn = createButton("Exit", GREY);

    public WelcomeFrame() {
        super("Welcome", 380, 320);

        JPanel body = createBody(new GridLayout(3, 1, 0, 12));
        body.add(navigateBtn);
        body.add(reportBtn);
        body.add(exitBtn);
        add(body, BorderLayout.CENTER);

        navigateBtn.addActionListener(this);
        reportBtn.addActionListener(this);
        exitBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == exitBtn) {
            System.exit(0);
        }
        dispose();
        if (source == navigateBtn) {
            Navigation.showTerms();          // navigate: only terms needed
        } else if (Session.isLoggedIn()) {
            Navigation.showReportIssue();    // report: login needed
        } else {
            Navigation.showLogin();
        }
    }
}