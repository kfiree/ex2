package gameClient;

import api.game_service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class window extends JFrame implements ActionListener {
    gamePanel panel;
    private boolean loggedIn;
    private JButton button;
    private JTextField textField;
    private game_service game;

    public window(String name, game_service game) {
        //set frame
        super(name);
        this.setTitle(name);
        this.setBackground( new Color(0x1F7F1F));
        ImageIcon icon = new ImageIcon("icon.png");
        this.setIconImage(icon.getImage());
        panel = new gamePanel(game);
        this.add(panel);
//        drawTextField();
        this.game=game;
    }
    public boolean drawTextField(){
        this.setLayout( new FlowLayout());

        button = new JButton("Login");
        button.addActionListener(this);

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(250, 40));

        this.add(button);
        this.add(textField);

        this.pack();
        this.setVisible(true);
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button) {
//            panel = new gamePanel(game);
            this.add(panel);
            loggedIn = true;
            try {
                int userID = Integer.parseInt(textField.getText());
                game.login(userID);
            } catch (NumberFormatException nfe) {
                //TODO add error message
            }
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
