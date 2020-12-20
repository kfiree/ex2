package gameClient;

import api.game_service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * this class is the entry panel
 */
public class welcomePanel extends JPanel  implements ActionListener {
    private JButton button;
    private JTextField ID_txtField;
    private JTextField lvl_txtField;
    private boolean alreadyDoneThat=false;

    /**
     * welcomePanel constructor
     */
    public welcomePanel() {
        super();
        setBorder(BorderFactory.createLineBorder (Color.black));
        drawTextField();

    }

    /**
     * add to panel 2 text field (for level number and user ID)
     * and a button to log in
     */
    public void drawTextField(){
        this.setFont(new Font("MV Boli", Font.BOLD,15));

        //login button
        button = new JButton ("Login");
        button.setBounds(375, 525, 200 , 50);
        button.addActionListener(this);

        //id text field
        ID_txtField = new JTextField ("id", 9);
        ID_txtField.addActionListener(this);
        ID_txtField.setBounds(200, 350, 250, 60);

        //level text field
        lvl_txtField = new JTextField ("lvl",3 );
        lvl_txtField.setBounds(550, 350, 250, 60);
        lvl_txtField.addActionListener(this);

        //instruction title
        JLabel ID_Title = new JLabel ("enter your ID to login to the game And choose level to play:");
        ID_Title.setBounds(200, 300, 200  ,50);

        //add elements to panel
        this.add(ID_Title);
        this.add(ID_txtField);
        this.add(lvl_txtField);
        this.add(button);

    }

    /**
     * if button was pressed and user insert his id and level number then start game
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //if button was pressed
        if(e.getSource() == button) {
            //get text inputs
            String stringID = ID_txtField.getText();
            String stringLVL = lvl_txtField.getText();

            //check if input is numbers
            try {
                int lvl = Integer.parseInt(stringLVL);
                int ID = Integer.parseInt(stringID);
                Ex2.setScenario_num(lvl);
                Ex2.setUserID(ID);
            } catch (NumberFormatException nfe) {
                System.out.println("Error. ID and level can only be numbers.");
            }

            //if thread is not running =>start thread
            if(!alreadyDoneThat) {
                Ex2.startThread();
                alreadyDoneThat = true;
            }
        }
    }
}
