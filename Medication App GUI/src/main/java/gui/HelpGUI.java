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
                    new File("src/main/resources/fonts/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);
        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            applicationFont = new Font("Arial", Font.PLAIN, 16);
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
                {
                        "Home Panel",
                        "Welcome to the main page of the application! This panel provides:\n" +
                                "- A simple and clean overview of your next scheduled medication.\n" +
                                "- A reminder message displaying when your next dose is due.\n" +
                                "This page helps you quickly see what’s coming up without navigating through other sections."
                },
                {
                        "Medications Panel",
                        "This panel is your go-to place for managing your medications. Features include:\n" +
                                "- **Add Medication**: Enter details such as name, dosage, frequency, and start/end dates.\n" +
                                "- **Edit Medication**: Modify the details of any existing medication.\n" +
                                "- **Remove Medication**: Delete medications that are no longer needed.\n" +
                                "Use this panel to ensure your medication list stays accurate and up-to-date."
                },
                {
                        "Medication History Panel",
                        "Track your adherence and performance with this panel, which includes:\n" +
                                "- **Visual Graphs**: View a graphical representation of your medication intake history.\n" +
                                "- **Adherence Statistics**: Analyze trends and identify missed or late doses.\n" +
                                "- **Detailed Logs**: Explore a detailed chronological record of all taken medications.\n" +
                                "This panel helps you monitor your habits and share progress with healthcare providers."
                },
                {
                        "Help Panel",
                        "Access step-by-step guidance and FAQs about the app. This panel allows you to:\n" +
                                "- Learn how to use each feature of the app effectively.\n" +
                                "- Browse a list of frequently asked questions (FAQs) for troubleshooting common issues.\n" +
                                "- Get quick tips for optimizing your experience.\n" +
                                "Use this section whenever you need help or want to explore advanced functionality."
                },
                {
                        "About Panel",
                        "Learn more about the application and its development. This panel includes:\n" +
                                "- **App Information**: Find details about the purpose and functionality of the app.\n" +
                                "- **Version Updates**: Stay updated with new features and improvements.\n" +
                                "- **Developer Contact**: Reach out to the creators for feedback or support.\n" +
                                "Explore this panel to understand the vision behind the app and stay informed about updates."
                },
                {
                        "Settings Panel",
                        "Customize the app to suit your needs with the following options:\n" +
                                "- **Notification Settings**: Configure reminders with sound, vibration, or silent options.\n" +
                                "- **User Profile**: Add personal details to make the app more personalized.\n" +
                                "- **App Theme**: Switch between light and dark modes for better visibility.\n" +
                                "- **Backup and Sync**: Enable cloud backups to ensure your data is safe and accessible across devices.\n" +
                                "This panel helps you tailor the app to fit your lifestyle."
                }
        };


        for (String[] txt : helpInfo) {
            content.add(createFAQEntry(txt[0], txt[1], applicationFont));
            content.add(Box.createVerticalStrut(10));
        }


        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel createFAQEntry(String question, String answer, Font font) {
        JPanel faqPanel = new JPanel(new BorderLayout());
        faqPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        faqPanel.setBackground(Color.WHITE);

        String arrowDown = "&#9660;";
        String arrowUp = "&#9650;";

        JButton questionButton = new JButton("<html>" + question + " " + arrowDown + "</html>");
        questionButton.setFont(font.deriveFont(Font.BOLD, 16f));
        questionButton.setHorizontalAlignment(SwingConstants.LEFT);
        questionButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        questionButton.setContentAreaFilled(false);
        questionButton.setFocusPainted(false);
        questionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        questionButton.setToolTipText("Click to expand/collapse");

        questionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                questionButton.setForeground(new Color(0, 102, 204));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                questionButton.setForeground(Color.BLACK);
            }
        });

        JPanel answerPanel = new JPanel(new BorderLayout());
        answerPanel.setBackground(new Color(245, 245, 245));
        answerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        answerPanel.setVisible(false);

        // Adjust HTML for wrapping by setting a maximum width
        JLabel answerLabel = new JLabel("<html><div style='width:400px;'>" + answer + "</div></html>");
        answerLabel.setFont(font.deriveFont(Font.PLAIN, 14f));
        answerPanel.add(answerLabel, BorderLayout.CENTER);

        questionButton.addActionListener(e -> {
            boolean isVisible = answerPanel.isVisible();
            answerPanel.setVisible(!isVisible);

            questionButton.setText("<html>" + question + " " + (isVisible ? arrowDown : arrowUp) + "</html>");

            faqPanel.revalidate();
            faqPanel.repaint();
        });

        faqPanel.add(questionButton, BorderLayout.NORTH);
        faqPanel.add(answerPanel, BorderLayout.CENTER);
        faqPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        return faqPanel;
    }
}
