package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DestinationFrame extends BaseFrame implements ActionListener {

    private JButton routeBtn = createButton("Get Route");
    private JButton backBtn = createButton("Back", GREY);

    public DestinationFrame() {
        super("Destination", 420, 320);

        JTextArea details = new JTextArea(
                "Destination: Library\n\n"
                        + "Building: Main Block\n"
                        + "Floor: 2nd Floor\n"
                        + "Room: 204");
        details.setFont(FONT);
        details.setEditable(false);
        details.setMargin(new Insets(10, 10, 10, 10));

        JPanel body = createBody(new BorderLayout());
        body.add(new JScrollPane(details), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        JPanel bar = createButtonBar();
        bar.add(backBtn);
        bar.add(routeBtn);
        add(bar, BorderLayout.SOUTH);

        routeBtn.addActionListener(this);
        backBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        dispose();
        if (e.getSource() == routeBtn) {
            Navigation.showRoute();
        } else {
            Navigation.showSearch();
        }
    }
}