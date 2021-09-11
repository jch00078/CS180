import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

/**
 * LoginPageGUI.java
 *
 * This program creates a GUI which constitutes the opening screen for the CampsGram application. It has
 * an email text field and password text field where users input information to log into an account.
 * Clicking the enter button will verify whether an account with the given email and password exists and
 * navigate to the account if it does, or else display an error message and ask the user to enter new inputs.
 * Clicking the make new account will verify whether the given email is associated with any existing account.
 * If not, it will create a new account and navigate to that account screen. If so, it will display an error
 * message and inform the user that an account with that email already exists.
 *
 * @author Team 060, Section 11
 * @version May 03, 2021
 * */

public class  LoginPageGUI implements Runnable {

    // Deals with network I/O
    transient IOMachine ioMachine;

    //Make GUI elements global fields
    //Labels and Text Fields
    JLabel instructions;
    JLabel emailLabel;
    JTextField emailField;
    JLabel passwordLabel;
    JPasswordField passwordField;

    //Buttons
    JButton enterButton;
    JButton makeAccountButton;

    //TEST BUTTON
    JButton testButton;

    public LoginPageGUI(IOMachine ioMachine) {
        this.ioMachine = ioMachine;
    }

    //Creates the GUI
    public void run() {
        //Set JFrame Settings
        JFrame frame = new JFrame("Welcome to CampsGram!"); //sets new frame
        frame.setSize(480,200); //sets frame size
        frame.setLocationRelativeTo(null); //sets the location of the frame
        frame.setResizable(false); //sets the frame as unresizable

        //Create Elements and add them to JPanel
        JPanel panel = new JPanel(); //create a panel
        instructions = new JLabel("Insert E-mail and Password to Login or Create a New Account");
        panel.add(instructions); //add label to panel
        emailLabel = new JLabel("Enter your Email:     "); //set email label text
        panel.add(emailLabel); //add label to panel
        emailField = new JTextField("", 20); //create text field
        panel.add(emailField); //add text field to panel
        passwordLabel = new JLabel("Enter your Password:       "); //set password label text
        panel.add(passwordLabel); //add label to panel
        passwordField = new JPasswordField("", 20); //create password field
        panel.add(passwordField); //add password field to panel
        enterButton = new JButton("            Enter             "); //create enter button
        enterButton.addActionListener(actionListener); //add action listener to enter button
        panel.add(enterButton); //add enter button to panel
        makeAccountButton = new JButton("    Make New Account    "); //create make account button
        makeAccountButton.addActionListener(actionListener); //add action listener to make account button
        panel.add(makeAccountButton); //add make account button to panel

        //TEST BUTTON
        testButton = new JButton("Test");
        testButton.addActionListener(actionListener);
        panel.add(testButton);

        //Add panel to frame and make frame visible
        frame.add(panel, BorderLayout.CENTER); //add JPanel to JFrame
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); //set default close operation to dispose
        frame.setVisible(true); //makes the frame visible to the user

    }

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) { //detects button clicks
            ;
            if (e.getSource() == enterButton) { //Code to perform when Enter Button is clicked
                //variables to use in the method
                boolean validAccount = false; //set initial value of boolean to false
                Account validate; //Account passed by server to check if the account exists
                String email;
                String password;


                //Checks whether input fields are empty. Display prompts user to fill out all fields if not.
                if (emailField.getText().equals("") || passwordField.getText().equals("")) {
                    emailField.setText(""); //resets email text field
                    passwordField.setText(""); //resets password text field
                    JOptionPane.showMessageDialog(null, "Please Enter Both an Email AND a Password!",
                            "CampsGram Login", JOptionPane.ERROR_MESSAGE);

                    /*Checks whether the email field input contains special characters '@' and '.'. Display prompts
                     user to input a valid email if not.*/
                } else if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
                    emailField.setText(""); //resets email text field
                    passwordField.setText(""); //resets password text field
                    JOptionPane.showMessageDialog(null, "Please Enter a Valid Email Address",
                            "CampsGram Login", JOptionPane.ERROR_MESSAGE);

                    /*Checks whether the password field contains at least 8 characters. Display prompts user to
                    make an adequately long password if not.*/
                } else if (passwordField.getText().length() < 8) {
                    emailField.setText(""); //resets email text field
                    passwordField.setText(""); //resets password text field
                    JOptionPane.showMessageDialog(null, "The Password Must Be At Least 8 " +
                            "Characters Long!", "CampsGram Login", JOptionPane.ERROR_MESSAGE);

                } else {
                    //If inputs are in the proper format, they are stored as variables
                    email = emailField.getText(); //store email field input in variable
                    emailField.setText(""); //reset the text field to empty
                    password = passwordField.getText(); //store password field input in variable
                    passwordField.setText(""); //reset the password field to empty

                    //Tries to connect to server and send inputs

                    try {
                        //gets the account associated with the requested email
                        validate = ioMachine.findAccount(email);

                        if (validate.getPassword().equals(password)) { //checks if the password is correct
                            validAccount = true; //update boolean
                        } else { //Displays an error message otherwise
                            JOptionPane.showMessageDialog(null, "No account matches the " +
                                    "email and password!", "CampsGram", JOptionPane.ERROR_MESSAGE);
                        }

                        if (validAccount) { //runs the account GUI if the login is validated
                            Account account = new Account(ioMachine.findAccount(email), ioMachine);
                            account.run();
                        } else { //Sends the user an error message otherwise
                            JOptionPane.showMessageDialog(null,
                                    "No account matches the email and password!", "CampsGram",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    } catch(Exception x) { //if an exception occurs, an error message is displayed
                        JOptionPane.showMessageDialog(null, "No account matches the " +
                                "email and password!", "CampsGram", JOptionPane.ERROR_MESSAGE);
                    }

                }


            }
            if (e.getSource() == makeAccountButton) { //Code to perform when Make Account Button is clicked

                //variables used in method
                boolean accountCreated = false; //initially set boolean to false
                String email;
                String password;

                //Checks whether input fields are empty. Display prompts user to fill out all fields
                if (emailField.getText() == null || passwordField.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Please Enter Both an Email AND a Password!",
                            "CampsGram", JOptionPane.ERROR_MESSAGE);
                    /*Checks whether the email field input contains special characters '@' and '.'.
                    Display prompts user to input a valid email if not.
                     */
                } else if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
                    emailField.setText(""); //resets email text field
                    JOptionPane.showMessageDialog(null, "Please Enter a Valid Email Address",
                            "CampsGram Login", JOptionPane.ERROR_MESSAGE);

                    /*Checks whether the password field contains at least 8 characters. Display prompts user to
                    make an adequately long password if not.*/
                } else if (passwordField.getText().length() < 8) {
                    passwordField.setText(""); //resets password text field
                    JOptionPane.showMessageDialog(null, "The Password Must Be At Least 8 " +
                            "Characters Long!", "CampsGram Login", JOptionPane.ERROR_MESSAGE);

                } else {
                    //If email and password have proper format, store them in variables
                    try {
                        email = emailField.getText(); //store email
                        emailField.setText(""); //reset email field
                        password = passwordField.getText(); //store password
                        passwordField.setText(""); //reset password field

                        //Sends a new account to the server
                        //check whether an account associated with that email exists
                        if (!Objects.nonNull(ioMachine.findAccount(email))) {
                            //if not, create new account
                            Account tempAccount = new Account(email, password, new ArrayList<Profile>());
                            accountCreated = ioMachine.addAccount(tempAccount);
                        } else { //if account exists, display error message
                            JOptionPane.showMessageDialog(null, "An account associated " +
                                    "with that email already exists!", "CampsGram", JOptionPane.ERROR_MESSAGE);
                        }
                        //Check whether account is properly created
                        if (accountCreated) {
                            Account account = new Account(ioMachine.findAccount(email), ioMachine);
                            account.run();
                        } //displays account if it is created
                    } catch (Exception ee) {
                        //if an exception occurs, an error message is displayed
                        JOptionPane.showMessageDialog(null, "Could not make new account!",
                                "CampsGram", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
            if (e.getSource() == testButton) {

                //adds sample profiles for testing purposes
                ArrayList<Profile> profiles = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    Profile p = new Profile();
                    p.setUsername(p.getUsername() + i);
                    profiles.add(p);
                }

                //creates a new account object
                Account newAccount = new Account("Will@purdue.edu", "12345678", profiles);
                newAccount = new Account(newAccount, ioMachine);

                //adds the account to the server
                ioMachine.addAccount(newAccount);

                //runs the new account
                newAccount.run();
            }

        }
    };



}
