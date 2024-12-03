package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HelpGUI extends JPanel {
    public HelpGUI() {

        // Set up fonts
        Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/resources/fonts/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);
        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            applicationFont = new Font("Arial", Font.PLAIN, 16); // Use default font
        }

        setLayout(new BorderLayout());

        JLabel helpHeader = new JLabel("Help and Instructions", SwingConstants.CENTER);
        helpHeader.setFont(applicationFont.deriveFont(Font.BOLD, 20f));
        helpHeader.setBorder(new EmptyBorder(20, 10, 20, 10));
        add(helpHeader, BorderLayout.NORTH);

        // Main info
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Information
        String[][] helpInfo = {
                {"Home Panel", "View your upcoming medication schedule and alerts."},
                {"Medications Panel", "Add, update, or remove medications."},
                {"Medication History Panel", "Track your past medication intake."},
                {"Help Panel", "View instructions on how to use the app."},
                {"About Panel", "Learn more about the app and its developers."},
                {"Settings Panel", "Customize your preferences."}
        };

        for (String[] txt : helpInfo) {
            content.add(FAQEntry(txt[0], txt[1], applicationFont));
            content.add(Box.createVerticalStrut(10));
        }


        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel FAQEntry(String question, String answer, Font font) {
        JPanel faqPanel = new JPanel();
        faqPanel.setLayout(new BorderLayout());
        faqPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));


        JButton questionButton = new JButton("<html><b>" + question + "</b></html>");
        questionButton.setFont(font.deriveFont(16f));
        questionButton.setHorizontalAlignment(SwingConstants.LEFT);
        questionButton.setBorder(new EmptyBorder(10, 10, 10, 10));
        questionButton.setContentAreaFilled(false); // Make button look like a label
        questionButton.setFocusPainted(false);


        JPanel answerPanel = new JPanel(new BorderLayout());
        answerPanel.setVisible(false);
        JLabel answerLabel = new JLabel("<html>" + answer + "</html>");
        answerLabel.setFont(font.deriveFont(14f));
        answerLabel.setBorder(new EmptyBorder(10, 20, 10, 10));
        answerPanel.add(answerLabel, BorderLayout.CENTER);


        questionButton.addActionListener(e -> {
            boolean isVisible = answerPanel.isVisible();
            answerPanel.setVisible(!isVisible);
            faqPanel.revalidate();
            faqPanel.repaint();
        });


        faqPanel.add(questionButton, BorderLayout.NORTH);
        faqPanel.add(answerPanel, BorderLayout.CENTER);

        return faqPanel;
    }
}
