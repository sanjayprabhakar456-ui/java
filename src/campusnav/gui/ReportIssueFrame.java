package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportIssueFrame extends BaseFrame implements ActionListener {

    private JComboBox<String> issueBox = new JComboBox<String>(new String[] {
            "Broken Light", "Water Leakage", "Damaged Door", "Cleanliness", "Other" });
    private JTextField locationField = new JTextField();
    private JTextArea descriptionArea = new JTextArea();
    private JButton submitBtn = createButton("Submit");
    private JButton backBtn = createButton("Back", GREY);

    public ReportIssueFrame() {
        super("Report Issue", 420, 420);

        locationField.setFont(FONT);
        descriptionArea.setFont(FONT);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JPanel top = new JPanel(new GridLayout(2, 2, 10, 10));
        top.setOpaque(false);
        top.add(new JLabel("Issue Type"));
        top.add(issueBox);
        top.add(new JLabel("Location"));
        top.add(locationField);

        JPanel descPanel = new JPanel(new BorderLayout(0, 5));
        descPanel.setOpaque(false);
        descPanel.add(new JLabel("Description"), BorderLayout.NORTH);
        descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        JPanel body = createBody(new BorderLayout(0, 10));
        body.add(top, BorderLayout.NORTH);
        body.add(descPanel, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        JPanel bar = createButtonBar();
        bar.add(backBtn);
        bar.add(submitBtn);
        add(bar, BorderLayout.SOUTH);

        submitBtn.addActionListener(this);
        backBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitBtn) {
            if (locationField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the location.");
                return;
            }
            ReportStore.add(issueBox.getSelectedItem() + " at " + locationField.getText().trim()
                    + " - " + descriptionArea.getText().trim()
                    + " (by " + Session.getCurrentUser() + ")");
            JOptionPane.showMessageDialog(this, "Issue submitted successfully.");
            locationField.setText("");
            descriptionArea.setText("");
        } else {
            dispose();
            Navigation.showWelcome();
        }
    }
}