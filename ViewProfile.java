import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ViewProfile.java
 *
 * This program establishes a way for a user to view the profiles of other users that automatically updates the
 * profiles in the event that the other user edits his or her profile.
 *
 * @author Team 060, Section 11
 * @version May 03, 2021
 * */

public class ViewProfile extends Profile {

    public ViewProfile(Profile profile, IOMachine ioMachine) {
        super(profile, ioMachine);
    }

    public void run() {
        frame = new JFrame(getUsername());
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //Everything below is for the center panel which holds user info

        //name panel
        JPanel namePanel = new JPanel();
        nameText = new JLabel("USER:          ");
        namePanel.add(nameText);
        usernameArea = new JTextArea(getUsername(), 5, 15);
        usernameArea.setEditable(false);
        namePanel.add(usernameArea);

        //phone panel
        JPanel phonePanel = new JPanel();
        phoneText = new JLabel("PHONE:          ");
        phonePanel.add(phoneText);
        phoneArea = new JTextArea(EnterInfoGUI.formatPhoneString(getPhoneNumber()), 5, 15);
        phoneArea.setEditable(false);
        phonePanel.add(phoneArea);

        //email panel
        JPanel emailPanel = new JPanel();
        emailText = new JLabel("EMAIL:         ");
        emailPanel.add(emailText);
        emailArea = new JTextArea(getEmail(), 5,15);
        emailArea.setEditable(false);
        emailPanel.add(emailArea);

        //education panel
        JPanel educationPanel = new JPanel();
        educationText = new JLabel("EDUCATION: ");
        educationPanel.add(educationText);
        educationArea = new JTextArea(getEducation(), 5, 15);
        educationArea.setEditable(false);
        educationPanel.add(educationArea);

        //about me panel
        JPanel aboutMePanel = new JPanel();
        aboutMeText = new JLabel("ABOUT ME:");
        aboutMePanel.add(aboutMeText);
        aboutMeArea = new JTextArea(EnterInfoGUI.formatAboutString(getAboutMe()), 8, 15);
        aboutMeArea.setEditable(false);
        aboutMePanel.add(aboutMeArea);


        //interests panel
        JPanel interestsPanel = new JPanel();
        interestsText = new JLabel("INTERESTS:  ");
        interestsPanel.add(interestsText);
        interestArea = new JTextArea(EnterInfoGUI.formatInterestsString(getInterests()),
                8, 15);
        interestArea.setEditable(false);
        interestsPanel.add(interestArea);

        //Combines the above panels in grid layout
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new GridLayout(3, 2));
        profilePanel.add(namePanel);
        profilePanel.add(phonePanel);
        profilePanel.add(emailPanel);
        profilePanel.add(educationPanel);
        profilePanel.add(aboutMePanel);
        profilePanel.add(interestsPanel);
        frame.add(profilePanel, BorderLayout.CENTER);

        frame.setVisible(true);

        //begins a timer that automatically updates the user list every 3 seconds
        Timer timer = new Timer(3000, viewRefresher);
        timer.setRepeats(true);
        timer.start();
    }

    //Code that Runs every 3 seconds, updating the profile list
    transient ActionListener viewRefresher = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            //gets the most recent version of the profile
            Profile updatedProfile = ioMachine.findProfile(getUsername());

            //updates the text areas
            phoneArea.setText(EnterInfoGUI.formatPhoneString(updatedProfile.getPhoneNumber()));
            emailArea.setText(updatedProfile.getEmail());
            educationArea.setText(updatedProfile.getEducation());
            aboutMeArea.setText(EnterInfoGUI.formatAboutString(updatedProfile.getAboutMe()));
            interestArea.setText(EnterInfoGUI.formatInterestsString(updatedProfile.getInterests()));

            //updates the GUI
            phoneArea.repaint();
            emailArea.repaint();
            educationArea.repaint();
            aboutMeArea.repaint();
            interestArea.repaint();
            frame.revalidate();
        }
    };
}
