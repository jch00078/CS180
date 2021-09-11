import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * FriendsListGUI.java
 *
 * This program creates objects constructed from the fields of a profile. It creates a GUI displaying a list
 * of profile usernames this profile sent a friend request to, a list of profile usernames this profile
 * received a friend request from, and this profile's list of current friends. In the run method that
 * constructs the GUI, there is a timer object which is used to repeatedly updates the GUI to account for
 * changes in the server. This program also contains action listeners that respond to button clicks.
 * Specifically, it can manage rescinding a friend request sent to another user, accepting or declining a
 * friend request received from another user, viewing the profile of a current friend, or unfriending a
 * current friend.
 *
 * @author Team 060, Section 11
 * @version May 03, 2021
 * */

public class FriendsListGUI implements Runnable {

    //Deals with network io
    IOMachine ioMachine;

    //GUI elements are global fields
    //NorthPanel element
    JButton backButton;

    //CentralPanelNorth elements
    JComboBox<String> requestsSentList;
    JButton rescindRequestButton;

    //CentralPanelSouth elements
    JComboBox<String> requestsPendingList;
    JButton acceptButton;
    JButton declineButton;

    //SouthPanel elements
    JComboBox<String> friendsList;
    JButton viewProfileButton;
    JButton unfriendButton;

    //Panels
    JPanel northPanel;
    JPanel centralPanel;
    JPanel centralPanelNorth;
    JPanel centralPanelSouth;
    JPanel southPanel;

    JFrame frame;

    //Fields for creating FriendsListGUI object
    private ArrayList<String> sentList;
    private ArrayList<String> receivedList;
    private ArrayList<String> friendList;
    private String username;


    //FriendsListGUI object constructor
    //Uses fields from profile
    public FriendsListGUI(String username, ArrayList<String> sentList, ArrayList<String> receivedList,
                          ArrayList<String> friendList) throws NullPointerException {
        this.username = username;
        this.sentList = sentList;
        this.receivedList = receivedList;
        this.friendList = friendList;
    }

    public FriendsListGUI(FriendsListGUI list, IOMachine ioMachine) throws NullPointerException {
        this.username = list.username;
        this.sentList = list.sentList;
        this.receivedList = list.receivedList;
        this.friendList = list.friendList;
        this.ioMachine = ioMachine;
    }


    //Accessor method for username of profile associated with these friend lists
    public String getUsername() {
        return this.username;
    }



    //Assemble the GUI
    public void run() {
        //Settings for the JFrame
        frame = new JFrame("CampsGram Friends List"); //title JFrame
        frame.setSize(600, 240); //sets size of JFrame
        frame.setLocationRelativeTo(null); //sets frame to appear in the middle of the screen
        frame.setResizable(false); //makes JFrame unable to be resized

        //Add Back Button to Top of Screen
        northPanel = new JPanel(); //create north panel
        backButton = new JButton("Back"); //create back button
        backButton.addActionListener(actionListener); //add action listener to back button
        northPanel.add(backButton); //add back button to  panel
        frame.add(northPanel, BorderLayout.NORTH); //add north panel to frame

        centralPanel = new JPanel(); //create  panel
        centralPanelNorth = new JPanel(); //create  panel
        JLabel sentLabel = new JLabel("Sent Friend Requests:"); //create label
        centralPanelNorth.add(sentLabel); //add label to panel


        //creates the drop down menu with usernames assuming there are any other profiles
        if (this.sentList.size() > 0) {
            //turns username list into a usable array
            String[] usernames = this.sentList.toArray(new String[this.sentList.size()]);

            //initializes the Drop down menu of usernames
            requestsSentList = new JComboBox<String>(usernames);
            requestsSentList.setMaximumRowCount(6);
        } else {
            requestsSentList = new JComboBox<>();
        }

        centralPanelNorth.add(requestsSentList); //adds drop down menu to panel
        rescindRequestButton = new JButton("Rescind Friend Request"); //create rescind request button
        rescindRequestButton.addActionListener(actionListener); //add action listener to rescind friend request button
        centralPanelNorth.add(rescindRequestButton); //add rescind request button to panel
        centralPanel.add(centralPanelNorth, BorderLayout.NORTH); //add panel to frame



        centralPanelSouth = new JPanel(); //create new panel
        JLabel received = new JLabel("Pending Friend Requests:"); //create label
        centralPanelSouth.add(received); //add label to panel

        //creates the drop down menu with usernames assuming there are any other profiles
        if (this.receivedList.size() > 0) {
            //turns username list into a usable array
            String[] usernames = this.receivedList.toArray(new String[this.receivedList.size()]);

            //initializes the Drop down menu of usernames
            requestsPendingList = new JComboBox<String>(usernames);
            requestsPendingList.setMaximumRowCount(6);
        } else {
            requestsPendingList = new JComboBox<>();
        }


        centralPanelSouth.add(requestsPendingList); //add drop down menu to panel
        acceptButton = new JButton("Accept"); //create accept button
        acceptButton.addActionListener(actionListener); //add action listener to accept button
        centralPanelSouth.add(acceptButton); //add accept button to panel
        declineButton = new JButton("Decline");  //create decline button
        declineButton.addActionListener(actionListener); //add action listener to decline button
        centralPanelSouth.add(declineButton); //add decline button to panel
        centralPanel.add(centralPanelSouth, BorderLayout.SOUTH); //add panel to panel
        frame.add(centralPanel, BorderLayout.CENTER); //add panel to frame

        

        //Add friend list, view profile button, and unfriend button to screen
        southPanel = new JPanel(); //create new panel
        JLabel friendsLabel = new JLabel("Current Friends: "); //create label
        southPanel.add(friendsLabel); //add label to panel


        //creates the drop down menu with usernames assuming there are any other profiles
        if (this.friendList.size() > 0) {
            //turns username list into a usable array
            String[] usernames = this.friendList.toArray(new String[this.friendList.size()]);

            //initializes the Drop down menu of usernames
            friendsList = new JComboBox<String>(usernames);
            friendsList.setMaximumRowCount(6);
        } else {
            friendsList = new JComboBox<>();
        }


        southPanel.add(friendsList); //add drop down menu to panel
        viewProfileButton = new JButton("View Profile"); //create view profile button
        viewProfileButton.addActionListener(actionListener); //add action listener to view profile button
        southPanel.add(viewProfileButton); //add view profile button to panel
        unfriendButton = new JButton("Unfriend"); //create unfriend button
        unfriendButton.addActionListener(actionListener); //add action listener to unfriend button
        southPanel.add(unfriendButton); //add unfriend button to panel
        frame.add(southPanel, BorderLayout.SOUTH); //add panel to frame

        
        
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); //set default close operation to dispose
        frame.setVisible(true); //make the frame visible


        //Create timer object. Object has 3-second delay and uses refresher listener
        Timer timer = new Timer(3000, refresher);
        timer.setRepeats(true);
        timer.start();
    }

    //Create action listener to respond to button clicks
    ActionListener actionListener = new ActionListener() {
       public void actionPerformed(ActionEvent e) {
           if (e.getSource() == acceptButton) {
               String username = (String) requestsPendingList.getSelectedItem(); //get user
               if (ioMachine.acceptFriend(getUsername(), username)){ //changes saved in server (request accepted)
                   JOptionPane.showMessageDialog(null, "Friend Accepted!",
                           "CampsGram", JOptionPane.INFORMATION_MESSAGE);
               }


           } //code that is run if accept button is clicked
           if (e.getSource() == declineButton) {
               String username = (String) requestsPendingList.getSelectedItem(); //get user
               if (ioMachine.declineFriend(getUsername(), username)) { //changes saved in server (request declined)
                    JOptionPane.showMessageDialog(null, "Request Declined!",
                            "CampsGram", JOptionPane.INFORMATION_MESSAGE);
               }

           } //code that is run if decline button is clicked
           if (e.getSource() == viewProfileButton) {
               String username = (String) friendsList.getSelectedItem(); //get username from drop down menu
               Profile friend = ioMachine.findProfile(username); //retrieve friend's profile from the server
               ViewProfile view = new ViewProfile(friend, ioMachine);
               view.run(); //view auto-updating profile

           } //code that is run if view profile button is clicked
           if (e.getSource() == unfriendButton) {
               String username = (String) friendsList.getSelectedItem(); //get user
               if (ioMachine.unfriend(getUsername(), username)) { //changes saved in server (unfriended)
                   JOptionPane.showMessageDialog(null, "Unfriended!",
                           "CampsGram", JOptionPane.INFORMATION_MESSAGE);
               }

           } //code that is run if unfriend button is clicked
           if (e.getSource() == rescindRequestButton) {
               String username = (String) requestsSentList.getSelectedItem(); //get user
               if (ioMachine.rescindRequest(getUsername(), username)) { //changes saved in server (request rescinded)
                   JOptionPane.showMessageDialog(null, "Request Rescinded!",
                           "CampsGram", JOptionPane.INFORMATION_MESSAGE);
               }

           }
           if (e.getSource() == backButton) {
               frame.dispose(); //close the GUI screen
           }//code that is run if back button is clicked
        }
    };

   //Code that Runs every 3 seconds, updating the view of another profile list
    transient ActionListener refresher = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            //saves the current selection
            String currentSelectionReceived = (String) requestsPendingList.getSelectedItem();

            //retrieves the complete list of users, remove the current users name from the list
            ArrayList<String> receivedRequests = ioMachine.findProfile(getUsername()).getRequestsReceived();
            System.out.println(receivedRequests.toString());
            System.out.println("!");
            //sets the usernames to the current list of users
            String[] received = receivedRequests.toArray(new String[receivedRequests.size()]);
            DefaultComboBoxModel<String> modelReceived = new DefaultComboBoxModel<>(received);
            requestsPendingList.setModel(modelReceived);

            //if no pending requests, then disable view and unfriend buttons
            if (requestsPendingList.getItemCount() == 0) {
                acceptButton.setEnabled(false);
                declineButton.setEnabled(false);
            } else {
                acceptButton.setEnabled(true);
                declineButton.setEnabled(true);
            }

            //sets the selection what it was previously
            requestsPendingList.setSelectedItem(currentSelectionReceived);




            //saves the current selection
            String currentSelectionFriend = (String) friendsList.getSelectedItem();

            //retrieves the complete list of users, remove the current users name from the list
            ArrayList<String> friends = ioMachine.findProfile(getUsername()).getFriends();

            //sets the usernames to the current list of users
            String[] friend = friends.toArray(new String[friends.size()]);
            DefaultComboBoxModel<String> modelFriend = new DefaultComboBoxModel<>(friend);
            friendsList.setModel(modelFriend);

            //if no friends, then disable view and unfriend buttons
            if (friendsList.getItemCount() == 0) {
                viewProfileButton.setEnabled(false);
                unfriendButton.setEnabled(false);
            } else {
                viewProfileButton.setEnabled(true);
                unfriendButton.setEnabled(true);
            }

            //sets the selection what it was previously
            friendsList.setSelectedItem(currentSelectionFriend);




            //saves the current selection
            String currentSelectionSent = (String) requestsSentList.getSelectedItem();

            //retrieves the complete list of users, remove the current users name from the list
            ArrayList<String> sentRequests = ioMachine.findProfile(getUsername()).getRequestsSent();

            //sets the usernames to the current list of users
            String[] sent = sentRequests.toArray(new String[sentRequests.size()]);
            DefaultComboBoxModel<String> modelSent = new DefaultComboBoxModel<>(sent);
            requestsSentList.setModel(modelSent);
            
            //sets the selection what it was previously
            requestsSentList.setSelectedItem(currentSelectionSent);

            //if no friends, then disable view and unfriend buttons
            if (requestsSentList.getItemCount() == 0) {
                rescindRequestButton.setEnabled(false);
            } else {
                rescindRequestButton.setEnabled(true);
            }

            //updates the GUI
            requestsPendingList.repaint();
            friendsList.repaint();
            requestsSentList.repaint();
            frame.revalidate();


        }
    };

}
