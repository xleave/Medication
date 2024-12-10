import junit.framework.TestCase;
import javax.swing.*;

public class GUITestLogic extends TestCase {

    private JFrame testJFrame;

    protected void tearDown() throws Exception {
        if(this.testJFrame != null) {
            this.testJFrame.dispose();
            this.testJFrame = null;
        }
    }

    public JFrame getTestJFrame() {
        if (this.testJFrame == null) {
            this.testJFrame = new JFrame("TEST JFRAME");
        }
        return this.testJFrame;
    }

}
