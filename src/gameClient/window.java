package gameClient;

import javax.swing.*;

public class window extends JFrame{
    gamePanel panel;

    public window(String a) {
        //set frame
        super(a);
        panel = new gamePanel();
        this.add(panel);
//        this.setVisible(true); //make frame visible
//        this.setTitle("Kfir&Tehila's Arena");   //set title of frame
//        this.setSize(500, 500);      //set size
//        this.setLocationRelativeTo(null);       //centering the frame
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//set exit program plane
//        Panel gamePanel = new Panel();
//        gamePanel.setBackground(Color.lightGray);
//        this.setContentPane(gamePanel);
    }
}
