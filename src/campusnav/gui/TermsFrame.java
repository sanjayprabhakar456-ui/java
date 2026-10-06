package campusnav.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TermsFrame extends BaseFrame implements ActionListener {

    private JCheckBox acceptBox = new JCheckBox("I accept the terms and conditions");
    private JButton continueBtn = createButton("Continue");
    private JButton backBtn = createButton("Back", GREY);

    public TermsFrame() {
        super("Terms and Conditions", 420, 360);

        JTextArea terms = new JTextArea(
                "1. Use CampusNav only for finding places on campus.\n\n"
                        + "2. Routes are for guidance. Follow campus safety rules.\n\n"
                        + "3. Do not enter false information.\n\n"
                        + "4. Only logged-in users can report problems.");
        terms.setFont(FONT);
        terms.setEditable(false);
        terms.setLineWrap(true);
        terms.setWrapStyleWord(true);
        terms.setMargin(new Insets(10, 10, 10, 10));

        acceptBox.setFont(FONT);
        acceptBox.setOpaque(false);

        JPanel body = createBody(new BorderLayout(0, 8));
        body.add(new JScrollPane(terms), BorderLayout.CENTER);
        body.add(acceptBox, BorderLayout.SOUTH);
        add(body, BorderLayout.CENTER);

        continueBtn.setEnabled(false);          // disabled until the box is ticked
        JPanel bar = createButtonBar();
        bar.add(backBtn);
        bar.add(continueBtn);
        add(bar, BorderLayout.SOUTH);

        acceptBox.addActionListener(this);
        continueBtn.addActionListener(this);
        backBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == acceptBox) {
            continueBtn.setEnabled(acceptBox.isSelected());
        } else if (source == continueBtn) {
            dispose();
            Navigation.showHome();
        } else {
            dispose();
            Navigation.showWelcome();
        }
    }
}