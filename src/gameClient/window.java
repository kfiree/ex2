package gameClient;

import javax.swing.*;
import java.awt.*;

public class window extends JFrame{
    gamePanel panel;

    public window(String a) {
        //set frame
        super(a);
        this.setTitle("Kfir&Tehila's Arena");
        this.setBackground( new Color(0x1F7F1F));
        ImageIcon icon = new ImageIcon("icon.png");
        this.setIconImage(icon.getImage());
        panel = new gamePanel();
        this.add(panel);
    }
}
