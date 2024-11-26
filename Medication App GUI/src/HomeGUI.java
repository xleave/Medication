import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

public class HomeGUI extends JPanel {

    public HomeGUI() {

        Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/marley/Library/Mobile Documents/com~apple~CloudDocs/Documents/University Work - NAS/Year 3/CE320 Large Scale Software Systems/Group_Project/Medication App/src/EB_Garamond,Roboto_Condensed/Roboto_Condensed/RobotoCondensed-VariableFont_wght.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(applicationFont);

        } catch (IOException | FontFormatException e) {
            System.out.println("Font could not be found!");
            throw new RuntimeException(e);
        }

        BufferedImage applicationImage;
        try {
            applicationImage = ImageIO.read(new File("/Users/marley/Library/Mobile Documents/com~apple~CloudDocs/Documents/University Work - NAS/Year 3/CE320 Large Scale Software Systems/Group_Project/Medication App GUI/src/App Icon.png"));

        } catch (IOException imageFileNotFound) {
            throw new RuntimeException(imageFileNotFound);
        }

        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setPreferredSize(new Dimension(980, 600));

        JPanel homePanelContents = new JPanel();
        homePanelContents.setPreferredSize(new Dimension(980, 600));
        homePanelContents.setLayout(null);

        JLabel homeSubmenuHeading = new JLabel("HOME");
        homeSubmenuHeading.setBounds(470, -20, 50, 50);;
        homeSubmenuHeading.setFont(applicationFont);

        JLabel applicationIconLabel = new JLabel(new ImageIcon(applicationImage));
        applicationIconLabel.setBounds(400, 100, 200, 200);

        JLabel homeWelcomeText = new JLabel("Welcome " + "Test User");
        homeWelcomeText.setBounds(300, 300, 500, 50);
        homeWelcomeText.setFont(new Font("Courier", Font.TRUETYPE_FONT, 40));

        JLabel nextMedicineDueText = new JLabel("Your next medication is due in " + " Hours");
        nextMedicineDueText.setBounds(275, 400, 500, 50);
        nextMedicineDueText.setFont(new Font("Courier", Font.TRUETYPE_FONT, 20));


        homePanelContents.add(homeSubmenuHeading);
        homePanelContents.add(applicationIconLabel);
        homePanelContents.add(homeWelcomeText);
        homePanelContents.add(nextMedicineDueText);

        homePanel.add(homePanelContents);
        add(homePanel);



    }
}
