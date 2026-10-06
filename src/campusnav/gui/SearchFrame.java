package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchFrame extends BaseFrame implements ActionListener {

    private JTextField searchField = new JTextField();
    private JTextArea resultArea = new JTextArea("Search results will appear here.");
    private JButton searchBtn = createButton("Search");
    private JButton destinationBtn = createButton("View Destination");
    private JButton backBtn = createButton("Back", GREY);

    public SearchFrame() {
        super("Search", 440, 380);

        searchField.setFont(FONT);
        resultArea.setFont(FONT);
        resultArea.setEditable(false);
        resultArea.setMargin(new Insets(10, 10, 10, 10));

        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setOpaque(false);
        top.add(searchField, BorderLayout.CENTER);
        top.add(searchBtn, BorderLayout.EAST);

        JPanel body = createBody(new BorderLayout(0, 10));
        body.add(top, BorderLayout.NORTH);
        body.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        JPanel bar = createButtonBar();
        bar.add(backBtn);
        bar.add(destinationBtn);
        add(bar, BorderLayout.SOUTH);

        searchBtn.addActionListener(this);
        destinationBtn.addActionListener(this);
        backBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == searchBtn) {
            if (searchField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a place name.");
            } else {
                resultArea.setText("Destination found:\n\nLibrary\nMain Block\n2nd Floor");
            }
        } else if (source == destinationBtn) {
            dispose();
            Navigation.showDestination();
        } else {
            dispose();
            Navigation.showHome();
        }
    }
}