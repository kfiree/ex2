package gameClient;

import api.game_service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * This class is the frame that will contain the game graphics
 */
public class window extends JFrame {
    /**
     * window constructor
     * @param name frame name
     */
    public window(String name) {
        //set frame
        super(name);
        this.setTitle(name);
        this.setBackground( new Color(0x1F7F1F));
        ImageIcon icon = new ImageIcon("icon.png");
        this.setIconImage(icon.getImage());
    }
}
