package gameClient;

import javax.swing.*;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame{
    gamePanel panel;

    public MyFrame(String a){
        super(a);
        panel = new gamePanel(null);
        this.add(panel);
    }
}
