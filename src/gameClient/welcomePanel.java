package gameClient;

import api.game_service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class welcomePanel extends JPanel  implements ActionListener {
    private JButton button;
    private JTextField ID_txtField;
    private JTextField lvl_txtField;
    private boolean alreadyDoneThat=false;

    public welcomePanel() {
        super();
        setBorder(BorderFactory.createLineBorder (Color.black));
        drawTextField();

    }

    public void drawTextField(){
        this.setFont(new Font("MV Boli", Font.BOLD,15));
        button = new JButton ("Login");
        button.setBounds(375, 525, 200 , 50);

        ID_txtField = new JTextField ("id", 9);
        ID_txtField.addActionListener(this);
        ID_txtField.setBounds(200, 350, 250, 60);
//        ID_txtField.setLocation(new Point(200, 350));
//        ID_txtField.setPreferredSize(new Dimension(250, 60));

        lvl_txtField = new JTextField ("lvl",3 );
        lvl_txtField.setBounds(550, 350, 250, 60);
        lvl_txtField.addActionListener(this);
//        lvl_txtField.setLocation(new Point(550, 350));
//        lvl_txtField.setPreferredSize(new Dimension(250, 60));


        JLabel ID_Title = new JLabel ("enter your ID to login to the game And choose level to play:");
        ID_Title.setBounds(200, 300, 200  ,50);
//        ID_Title.setLocation(new Point(200, 300));
//        ID_Title.setPreferredSize(new Dimension(200  ,50));

//        JLabel lvlTitle = new JLabel ("choose what level you want to play 0-23:");
//        lvlTitle.setBounds(550, 300,200  ,50 );
//        lvlTitle.setLocation(new Point(550, 300));
//        lvlTitle.setPreferredSize(new Dimension(200  ,50));

        button.addActionListener(this);
//        button.setLocation(new Point(1,1))
//        button.setPreferredSize(new Dimension(25, 25);

        this.add(ID_Title);
//        this.add(lvlTitle);
        this.add(ID_txtField);
        this.add(lvl_txtField);
        this.add(button);

        button.addActionListener(this);

    }

//    public game_service getGame() {
//        return game;
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button) {
            String stringID = ID_txtField.getText();
            String stringLVL = lvl_txtField.getText();

            try {
                int lvl = Integer.parseInt(stringLVL);
                int ID = Integer.parseInt(stringID);
                Ex2.setScenario_num(lvl);
                Ex2.setUserID(ID);
            } catch (NumberFormatException nfe) {
                System.out.println("Error. ID and level can only be numbers.");
            }

            if(!alreadyDoneThat) {
                Ex2.startThread();
                alreadyDoneThat = true;
            }
        }
        if(e.getSource() == ID_txtField && ID_txtField.getText()=="id"){
            ID_txtField.setText("");
        }

        if(e.getSource() == lvl_txtField && lvl_txtField.getText()== "lvl"){
            lvl_txtField.setText("");
        }
    }

}
