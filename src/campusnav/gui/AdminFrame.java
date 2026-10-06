package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminFrame extends BaseFrame implements ActionListener {

    private JTextArea reportArea = new JTextArea("Click 'View Reports' to see reported issues.");
    private JButton viewBtn = createButton("View Reports");
    private JButton logoutBtn = createButton("Logout", GREY);

    public AdminFrame() {
        super("Admin Dashboard", 460, 380);

        reportArea.setFont(FONT);
        reportArea.setEditable(false);
        reportArea.setMargin(new Insets(10, 10, 10, 10));

        JPanel body = createBody(new BorderLayout());
        body.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        JPanel bar = createButtonBar();
        bar.add(logoutBtn);
        bar.add(viewBtn);
        add(bar, BorderLayout.SOUTH);

        viewBtn.addActionListener(this);
        logoutBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewBtn) {
            if (ReportStore.getAll().isEmpty()) {
                reportArea.setText("No reports yet.");
                return;
            }
            String text = "";
            int number = 1;
            for (String report : ReportStore.getAll()) {
                text += number + ". " + report + "\n\n";
                number++;
            }
            reportArea.setText(text);
        } else {
            Session.logout();
            dispose();
            Navigation.showWelcome();
        }
    }
}