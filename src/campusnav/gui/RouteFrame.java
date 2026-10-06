package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RouteFrame extends BaseFrame implements ActionListener {

    private JButton backBtn = createButton("Back", GREY);
    private JButton homeBtn = createButton("Home");

    public RouteFrame() {
        super("Route to Library", 420, 340);

        JTextArea route = new JTextArea(
                "Start: Main Gate\n\n"
                        + "1. Walk towards Main Block.\n"
                        + "2. Enter the Main Block.\n"
                        + "3. Go to the 2nd Floor.\n"
                        + "4. Library is in Room 204.");
        route.setFont(FONT);
        route.setEditable(false);
        route.setMargin(new Insets(10, 10, 10, 10));

        JPanel body = createBody(new BorderLayout());
        body.add(new JScrollPane(route), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        JPanel bar = createButtonBar();
        bar.add(backBtn);
        bar.add(homeBtn);
        add(bar, BorderLayout.SOUTH);

        backBtn.addActionListener(this);
        homeBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        dispose();
        if (e.getSource() == backBtn) {
            Navigation.showDestination();
        } else {
            Navigation.showHome();
        }
    }
}