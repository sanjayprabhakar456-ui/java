package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeFrame extends BaseFrame implements ActionListener {

    private JButton searchBtn = createButton("Find Destination");
    private JButton reportBtn = createButton("Report Issue", new Color(230, 81, 0));
    private JButton backBtn = createButton("Main Menu", GREY);

    public HomeFrame() {
        super("Home", 380, 320);

        JPanel body = createBody(new GridLayout(3, 1, 0, 12));
        body.add(searchBtn);
        body.add(reportBtn);
        body.add(backBtn);
        add(body, BorderLayout.CENTER);

        searchBtn.addActionListener(this);
        reportBtn.addActionListener(this);
        backBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        dispose();
        if (source == searchBtn) {
            Navigation.showSearch();
        } else if (source == reportBtn) {
            if (Session.isLoggedIn()) {
                Navigation.showReportIssue();
            } else {
                Navigation.showLogin();
            }
        } else {
            Navigation.showWelcome();
        }
    }
}