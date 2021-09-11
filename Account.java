import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Account.java
 *
 * This program create account objects. It contains a run method for creating a graphical user interface. The
 * GUI displays all the profiles created associated with this account. It also contains action listeners to
 * respond to button clicks. Specifically, this GUI contains an add profile method, which calls a series of
 * JOptionPanes that accept input from the user that are used to create a profile for this account; a delete
 * account method, which removes the account and associated profiles from the server and disposes of the GUI;
 * and an import button, which enables the user to create a new profile for this account from a CSV file.
 * Additionally, for each profile that is created, there is a view button which enables the user to see the
 * particular profile, and a delete button which removes the profile from the list of profiles associated
 * with this account.
 *
 * @author Team 060, Section 11
 * @version May 03, 2021
 * */

public class Account implements Serializable {

    private final String email;
    private final String password;
    private ArrayList<Profile> profiles;
    
    //Hashmaps and a counter used to keep track of which profiles are associated with which panels
    private HashMap<Integer, Profile> profileHashMap = new HashMap();
    private HashMap<Integer, JPanel> profilePanes = new HashMap();
    private int counter = 0;
  
    //for Network I/O functionality
    transient IOMachine ioMachine;

    //frame and panels
    JFrame frame;
    JPanel bottomBar;
    JPanel mainPanel;

    //bottom bar buttons
    JButton deleteAcc; //deletes the account
    JButton addProfile; //creates a new profile when pushed
    JTextField importFileField; //field where user may enter a file name
    JButton importButton; //if the file is valid, the profile is imported

    public Account(String email, String password, ArrayList<Profile> profiles) throws NullPointerException {
        this.email = email;
        this.password = password;
        this.profiles = profiles;
    }

    public Account(Account account, IOMachine ioMachine) throws NullPointerException {
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.profiles = account.getProfiles();
        this.ioMachine = ioMachine;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public ArrayList<Profile> getProfiles() {
        return this.profiles;
    }

    public void setProfiles(ArrayList<Profile> profiles) {
        this.profiles = profiles;
    }

    //adds a profile to the profile arraylist
    public void addProfile(Profile profile) {
        this.profiles.add(profile);
    }

    //removes a profile from the account profile arraylist
    public void deleteProfile(String username) {

        for (int i = 0; i < this.profiles.size(); i++) {

            if (profiles.get(i).getUsername().equals(username)) {
                profiles.remove(i);
            }
        }
    }

    public void run() {
        //Creates the frame
        frame = new JFrame("Account: " + email);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null); //sets the location of the frame
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //Sets up the bottom bar and its buttons
        bottomBar = new JPanel();
        deleteAcc = new JButton("Delete");
        deleteAcc.addActionListener(bottomBarListener);
        addProfile = new JButton("Add Profile");
        addProfile.addActionListener(bottomBarListener);
        JLabel importProfile = new JLabel("Import Profile:");
        importFileField = new JTextField("", 10);
        importButton = new JButton("Import");
        importButton.addActionListener(bottomBarListener);
        bottomBar.add(deleteAcc);
        bottomBar.add(addProfile);
        bottomBar.add(importProfile);
        bottomBar.add(importFileField);
        bottomBar.add(importButton);

        //The profiles will be laid out like a grid with 2 columns
        mainPanel = new JPanel(new GridLayout(profiles.size() / 2 + 1, 2));
        
        //sets the counter equal to the amount of profiles
        counter = profiles.size();

        for(int i = 0; i < profiles.size(); i++) {

            //Creates a panel with a view and delete button
            //The buttons will be assigned the action commands with either
            //the word "view" or "delete" followed by their profiles index
            //in the profile arraylist
            JPanel profilePanel = new JPanel();
            JLabel profileName = new JLabel(profiles.get(i).getUsername());
            JButton view = new JButton("View");
            view.addActionListener(profileListener);
            view.setActionCommand("view" + i);
            JButton delete = new JButton("Delete");
            delete.addActionListener(profileListener);
            delete.setActionCommand("delete" + i);
            profilePanel.add(profileName);
            profilePanel.add(view);
            profilePanel.add(delete);

            //adds the profile panel to the main panel
            mainPanel.add(profilePanel);
            
            //Adds the profile and its panes to the hashmaps
            profileHashMap.put(i, profiles.get(i));
            profilePanes.put(i, profilePanel);
        }

        //adds the main panel and bottom bar to the frame
        frame.add(mainPanel);
        frame.add(bottomBar, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    transient ActionListener bottomBarListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == deleteAcc) {

                ioMachine.deleteAccount(ioMachine.findAccount(email));
                for (int i = 0; i < profiles.size(); i++) {

                    //gets a profile in the account
                    Profile toDelete = profiles.get(i);

                    //unfriends that profile from all of its friends and recinds/declines its requests
                    for (int r = 0; r < toDelete.getFriends().size(); r++) {
                        ioMachine.unfriend(toDelete.getFriends().get(r), toDelete.getUsername());
                    }
                    for(int r = 0; r < toDelete.getRequestsSent().size(); r++) {
                        ioMachine.rescindRequest(toDelete.getRequestsSent().get(r), toDelete.getUsername());
                    }
                    for(int r = 0; r < toDelete.getRequestsReceived().size(); r++) {
                        ioMachine.declineFriend(toDelete.getRequestsReceived().get(r), toDelete.getUsername());
                    }

                    ioMachine.deleteProfile(toDelete.getUsername());
                }

                ioMachine.deleteAccount(ioMachine.findAccount(email));
                frame.dispose();
            }
            if(e.getSource() == addProfile) {
                //Uses EnterInfo GUI to create fields for a profile
                boolean usernameTaken = true;
                String username;
                do {
                    username = EnterInfoGUI.showUsernameInputDialog();
                    if(ioMachine.findProfile(username) == null){
                        usernameTaken = false;
                    } else {
                        JOptionPane.showMessageDialog(null, "Username Taken", "CampsGram",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } while (usernameTaken);
                ArrayList<String> interests = EnterInfoGUI.showInterestsInputDialog();
                ArrayList<String> friends = new ArrayList<>();
                String education = EnterInfoGUI.showEducationInputDialog();
                String email = getEmail();
                Long phone =  EnterInfoGUI.showPhoneInputDialog();
                String aboutMe = EnterInfoGUI.showAboutInputDialog();

                //creates a new profile and adds it to the array list of profiles
                Profile profile = new Profile(username, interests, friends, education, email, phone, 
                                              aboutMe, new ArrayList<>(), new ArrayList<>());
                profiles.add(profile);

                //Adds the profile to the server
                ioMachine.addProfile(profile, getEmail());

                //Updates the GUI
                JPanel profilePanel = new JPanel();
                JLabel profileName = new JLabel(profile.getUsername());
                JButton view = new JButton("View");
                view.addActionListener(profileListener);
                view.setActionCommand("view" + counter);
                JButton delete = new JButton("Delete");
                delete.addActionListener(profileListener);
                delete.setActionCommand("delete" +  counter);
                profilePanel.add(profileName);
                profilePanel.add(view);
                profilePanel.add(delete);

                //Adds the profile and its panes to the hashmaps
                profileHashMap.put(counter, profile);
                profilePanes.put(counter, profilePanel);
                counter++;

                mainPanel.add(profilePanel);
                mainPanel.revalidate();
                mainPanel.repaint();

            }
            if (e.getSource() == importButton) {
                 try {
                    //If Import
                    String filename = importFileField.getText();
                    Profile profile = new Profile(filename);
                    if (ioMachine.addProfile(profile, getEmail())) {
                        //Updates the GUI
                        JPanel profilePanel = new JPanel();
                        JLabel profileName = new JLabel(profile.getUsername());
                        JButton view = new JButton("View");
                        view.addActionListener(profileListener);
                        view.setActionCommand("view" + counter);
                        JButton delete = new JButton("Delete");
                        delete.addActionListener(profileListener);
                        delete.setActionCommand("delete" +  counter);
                        profilePanel.add(profileName);
                        profilePanel.add(view);
                        profilePanel.add(delete);

                        //Adds the profile and its panes to the hashmaps
                        profileHashMap.put(counter, profile);
                        profilePanes.put(counter, profilePanel);
                        counter++;

                        mainPanel.add(profilePanel);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                    importFileField.setText("");
                } catch (Exception ex) {
                    importFileField.setText("");
                }
            }
        }
    };

    transient ActionListener profileListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {


            if (e.getActionCommand().contains("view")) //Runs if the received action command contains view
            {
                /*retrieves the integer in the action command, which represents the index of the profile in the
                profiles array*/
                int profileIndex = Integer.parseInt(e.getActionCommand().substring(4));

                //grants the profile net I/O access
                Profile profile = new Profile(profileHashMap.get(profileIndex), ioMachine);

                //runs the profiles GUI
                profile.run();
            }
            else if (e.getActionCommand().contains("delete")) //Runs if the received action command contains delete
            {
                int profileIndex = Integer.parseInt(e.getActionCommand().substring(6));

                //unfriends the user from everyone in its friends list and rescinds all its requests
                Profile toDelete =  profileHashMap.get(profileIndex);
                for(int i = 0; i < toDelete.getFriends().size(); i++) {
                    ioMachine.unfriend(toDelete.getFriends().get(i), toDelete.getUsername());
                }
                for(int i = 0; i < toDelete.getFriends().size(); i++) {
                    ioMachine.rescindRequest(toDelete.getFriends().get(i), toDelete.getUsername());
                }

                //removes the profile from the arraylist of profiles
                profiles.remove(profileHashMap.get(profileIndex));

                //removes the profile from the server
                ioMachine.deleteProfile(profileHashMap.get(profileIndex).getUsername());

                //removes the profile's gui
                mainPanel.remove(profilePanes.get(profileIndex));
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        }
    };
}
