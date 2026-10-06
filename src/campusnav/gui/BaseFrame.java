package campusnav.gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

// Parent class of every screen (Inheritance).
// It gives all frames the same size rules, colours, header and buttons.
public class BaseFrame extends JFrame {

    protected static final Color BLUE = new Color(25, 118, 210);
    protected static final Color GREY = new Color(120, 144, 156);
    protected static final Font FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public BaseFrame(String title, int width, int height) {
        super("CampusNav - " + title);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(0, 10));

        // blue header bar showing the screen name
        JLabel header = new JLabel(title, SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));
        add(header, BorderLayout.NORTH);
    }

    // flat coloured button
    protected JButton createButton(String text, Color colour) {
        JButton button = new JButton(text);
        button.setUI(new BasicButtonUI());
        button.setOpaque(true);
        button.setBackground(colour);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        return button;
    }

    protected JButton createButton(String text) {
        return createButton(text, BLUE);
    }

    // padded transparent panel for the middle part of a screen
    protected JPanel createBody(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 5, 30));
        return panel;
    }

    // panel for the buttons at the bottom of a screen
    protected JPanel createButtonBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setOpaque(false);
        return panel;
    }
}