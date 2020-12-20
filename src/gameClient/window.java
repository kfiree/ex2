package gameClient;

import api.game_service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * This class
 */
public class window extends JFrame {
    Component panel;
//    JComponent panel;

    public window(String name) {
        //set frame
        super(name);
        this.setTitle(name);
        this.setBackground( new Color(0x1F7F1F));
        ImageIcon icon = new ImageIcon("icon.png");
        this.setIconImage(icon.getImage());
//        panel = new gamePanel(game);
//        this.add(panel);
////        drawTextField();
//        this.game=game;
    }

    public void setPanel(Component c) {
        this.panel = c;

    }
}
