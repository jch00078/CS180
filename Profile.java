import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.io.*;
import java.util.Locale;

/**
 * Profile.java
 *
 * This class creates profile objects. It contains a run method for displaying a profile GUI. The run method
 * contains a timer object, so that the profile may automatically update to changes processed in the server.
 * This file also contains action listeners for functionality to implement on button clicks. Specifically,
 * there is an export file button which creates a CSV file of the profile object and an edit profile button which
 * sends any changes to the profile to the server and updates the GUI. Additionally, the profile GUI contains
 * buttons for navigating to a friends list screen, viewing any profile on the server, and sending friend
 * requests to any user.
 *
 * @author Team 060, Section 11
 * @version May 03, 2021
 * */

public class Profile implements Serializable, Runnable {

    //Profile Fields
    private String username;
    private ArrayList<String> interests;
    private ArrayList<String> friends;
    private ArrayList<String> requestsSent;
    private ArrayList<String> requestsReceived;
    private String education;
    private String email;
    private long phoneNumber;
    private String aboutMe;

    IOMachine ioMachine;

    //Frames and Panels
    transient JFrame frame;
    transient JPanel topPanel;
    transient JPanel bottomPanel;

    //top elements
    transient JButton export; //export button
    transient JButton edit; //edit button
    transient JButton requests; //requests button

    //bottom elements
    transient JComboBox<String> users; //all the users on the app
    transient JButton view; //view another users profile
    transient JButton sendFriendRequest; //sends friend request


    //Text Boxes
    transient JLabel nameText;
    transient JLabel phoneText;
    transient JLabel emailText;
    transient JLabel educationText;
    transient JLabel aboutMeText;
    transient JLabel interestsText;

    //Text Areas
    transient JTextArea usernameArea;
    transient JTextArea phoneArea;
    transient JTextArea emailArea;
    transient JTextArea educationArea;
    transient JTextArea aboutMeArea;
    transient JTextArea interestArea;

    public Profile(String username, ArrayList<String> interests, ArrayList<String> friends, String education,
                   String email, long phoneNumber, String aboutMe, ArrayList<String> requestsSent,
                   ArrayList<String> requestsReceived) {

        this.username = username;
        this.interests = interests;
        this.friends = friends;
        this.education = education;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.aboutMe = aboutMe;
        this.requestsSent = requestsSent;
        this.requestsReceived = requestsReceived;
    }

    public Profile() {

        this.username = "Will Stonebridge";
        this.interests = new ArrayList<String>() {
            {
                add("Rowing");
                add("Coding");
                add("History");
            }
        };
        this.friends = new ArrayList<String>() {
            {
                add("Calvin Carta");
                add("Jeff Chen");
            }
        };
        this.education = "Purdue";
        this.email = "jwstoneb@purdue.edu";
        this.phoneNumber = 8476360377L;
        this.aboutMe = "Why are we here, Just to suffer?";
        this.requestsReceived = new ArrayList<String>();
        this.requestsSent = new ArrayList<String>();
    }

    public Profile(String filename) throws IOException {

        String profile = null; //line of profile info from .csv
        String[] profileInfo; //array of profile info from .csv
        int listLength; //length interests and friends arrays
        int index = 0; //index of profileInfo
        int tempIndex; //holds index that is reached before appending to a list
        boolean fileRead = false;

        //initialize new interests and friends lists so they can be added to
        this.interests = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.requestsReceived = new ArrayList<>();
        this.requestsSent = new ArrayList<>();

        //read from file, runs while the boolean indicating the file has been read is false
        while (!fileRead) {

            try {
                //Read in file using correct filename using new bufferedReader
                File f = new File(filename);
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);

                //reads in the line from the file
                profile = bfr.readLine();

                bfr.close();

                //change fileRead to true to exit loop
                fileRead = true;

            } catch (IOException e) { //if IO Exception, likely due to incorrect file name

                //display error message and ask for new filename for next loop iteration
                JOptionPane.showMessageDialog(null, "Not a valid file name!", "CamsGram",
                        JOptionPane.ERROR_MESSAGE);

                filename = JOptionPane.showInputDialog(null, "Enter a valid filename: ",
                        "CampsGram", JOptionPane.QUESTION_MESSAGE);
            }
        }

        //split the .csv by commas into a string array for data processing
        profileInfo = profile.split(",");

        //set username to first index in array and iterate the index counter
        this.username = profileInfo[index];
        index++;

        //find length of interests list and iterate the index counter
        listLength = Integer.parseInt(profileInfo[index]);
        index++;

        //set tempIndex to index for loop iterate
        tempIndex = index;

        //loop from current index to index at start of loop + the number of items in the loop to ensure all list items
        //are found
        while (index < listLength + tempIndex) {
            interests.add(profileInfo[index]);
            index++;
        }

        //repeat the same process as previous for the friends arraylist
        listLength = Integer.parseInt(profileInfo[index]);
        index++;

        tempIndex = index;

        while (index < listLength + tempIndex) {
            friends.add(profileInfo[index]);
            index++;
        }

        listLength = Integer.parseInt(profileInfo[index]);
        index++;

        tempIndex = index;

        while (index < listLength + tempIndex) {
            requestsReceived.add(profileInfo[index]);
            index++;
        }

        listLength = Integer.parseInt(profileInfo[index]);
        index++;

        tempIndex = index;

        while (index < listLength + tempIndex) {
            requestsSent.add(profileInfo[index]);
            index++;
        }

        //set fields to indexes from the .csv as appropriate for the remaining fields
        this.education = profileInfo[index];
        index++;

        this.email = profileInfo[index];
        index++;

        this.phoneNumber = Long.parseLong(profileInfo[index]);
        index++;

        this.aboutMe = profileInfo[index] + ", ";
        index++;

        //loop through the rest of the indexes in profileInfo and append to the aboutMe section with a comma
        //to account for user entered commas in the about me section
        while (index < profileInfo.length) {
            this.aboutMe = this.aboutMe + profileInfo[index] + ", ";
            index++;
        }

        //remove last two characters, as the aboutMe loop will leave a comma and space at the end of the process
        this.aboutMe = this.aboutMe.substring(0, this.aboutMe.length() - 2);
    }

    /*this constructor creates another profile object identical to the one passed to it as an argument, it will be used
    when the server send profiles back to the client to display after logging in*/
    public Profile(Profile profile, IOMachine ioMachine) {
        this.username = profile.getUsername();
        this.interests = profile.getInterests();
        this.friends = profile.getFriends();
        this.education = profile.getEducation();
        this.email = profile.getEmail();
        this.phoneNumber = profile.getPhoneNumber();
        this.aboutMe = profile.getAboutMe();
        this.requestsSent = profile.getRequestsSent();
        this.requestsReceived = profile.getRequestsReceived();
        this.ioMachine = ioMachine;
    }

    public void run() {
        frame = new JFrame(username);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //top panel
        topPanel = new JPanel();
        export = new JButton("Export Profile");
        export.addActionListener(actionListener);
        topPanel.add(export);
        edit = new JButton("Edit Profile");
        edit.addActionListener(actionListener);
        topPanel.add(edit);
        requests = new JButton("View Friend Requests");
        requests.addActionListener(actionListener);
        topPanel.add(requests);
        frame.add(topPanel, BorderLayout.NORTH);

        //bottom panel
        bottomPanel = new JPanel();
        JLabel userLabel = new JLabel("Find Other Users: ");
        bottomPanel.add(userLabel);

        //Drop down box of usernames

        //retrieves the complete list of users, remove the current users name from the list
        ArrayList<String> usernameList = ioMachine.viewAllProfiles();
        usernameList.remove(getUsername());

        //creates the drop down menu with usernames assuming there are any other profiles
        if (usernameList.size() > 0) {
            //turns username list into a usable array
            String[] usernames = usernameList.toArray(new String[usernameList.size()]);

            //initializes the Drop down menu of usernames
            users = new JComboBox<String>(usernames);
            users.setMaximumRowCount(6);
        } else {
            users = new JComboBox<>();
        }

        bottomPanel.add(users);

        view = new JButton("View");
        view.addActionListener(actionListener);
        bottomPanel.add(view);

        //creates send Friend Request button
        sendFriendRequest = new JButton("Send Friend Request");
        sendFriendRequest.addActionListener(actionListener); //add action listener to button
        bottomPanel.add(sendFriendRequest);

        frame.add(bottomPanel, BorderLayout.SOUTH); // add panel to frame

        //Everything below is for the center panel which holds user info

        //name panel
        JPanel namePanel = new JPanel();
        nameText = new JLabel("USER:          ");
        namePanel.add(nameText);
        usernameArea = new JTextArea(this.username, 5, 15);
        usernameArea.setEditable(false);
        namePanel.add(usernameArea);

        //phone panel
        JPanel phonePanel = new JPanel();
        phoneText = new JLabel("PHONE:          ");
        phonePanel.add(phoneText);
        phoneArea = new JTextArea(EnterInfoGUI.formatPhoneString(this.phoneNumber), 5, 15);
        phoneArea.setEditable(false);
        phonePanel.add(phoneArea);

        //email panel
        JPanel emailPanel = new JPanel();
        emailText = new JLabel("EMAIL:         ");
        emailPanel.add(emailText);
        emailArea = new JTextArea(this.email, 5,15);
        emailArea.setEditable(false);
        emailPanel.add(emailArea);

        //education panel
        JPanel educationPanel = new JPanel();
        educationText = new JLabel("EDUCATION: ");
        educationPanel.add(educationText);
        educationArea = new JTextArea(this.education, 5, 15);
        educationArea.setEditable(false);
        educationPanel.add(educationArea);

        //about me panel
        JPanel aboutMePanel = new JPanel();
        aboutMeText = new JLabel("ABOUT ME:");
        aboutMePanel.add(aboutMeText);
        aboutMeArea = new JTextArea(EnterInfoGUI.formatAboutString(this.aboutMe), 8, 15);
        aboutMeArea.setEditable(false);
        aboutMePanel.add(aboutMeArea);

        //interests panel
        JPanel interestsPanel = new JPanel();
        interestsText = new JLabel("INTERESTS:  ");
        interestsPanel.add(interestsText);
        interestArea = new JTextArea(EnterInfoGUI.formatInterestsString(this.interests),
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
        Timer timer = new Timer(3000, refresher);
        timer.setRepeats(true);
        timer.start();

    }

    transient ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == export) {
                try {
                    writeExportFile();
                    JOptionPane.showMessageDialog(null, "Successfully Exported Profile!",
                            "CampsGram", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Oops! There was a problem exporting" +
                            " file. Try Again.", "CampsGram", JOptionPane.ERROR_MESSAGE);
                }

            }
            if (e.getSource() == edit) {
                //GUI for all editing
                phoneNumber = EnterInfoGUI.showPhoneInputDialog();
                education = EnterInfoGUI.showEducationInputDialog();
                interests = EnterInfoGUI.showInterestsInputDialog();
                aboutMe = EnterInfoGUI.showAboutInputDialog();

                //sets the text to the new fields
                phoneArea.setText(EnterInfoGUI.formatPhoneString(phoneNumber));
                educationArea.setText(education);
                interestArea.setText(EnterInfoGUI.formatInterestsString(interests));
                aboutMeArea.setText(EnterInfoGUI.formatAboutString(aboutMe));

                //updates the profile in the server
                Profile profile = new Profile(username, interests, friends, education, email, phoneNumber, aboutMe,
                        requestsSent, requestsReceived);
                ioMachine.deleteProfile(username);
                ioMachine.addProfile(profile, email);

                //updates the GUI
                frame.repaint();
                frame.revalidate();
            }
            if(e.getSource() == requests) {
                FriendsListGUI friend = new FriendsListGUI(username, requestsSent,
                        requestsReceived, friends);
                FriendsListGUI usableFriend = new FriendsListGUI(friend, ioMachine);
                usableFriend.run();
            }

            //Bottom Buttons
            if (e.getSource() == view) { //view another users profile
                //takes the username in users drop down box
                String requestedUser = (String) users.getSelectedItem();

                Profile profile = ioMachine.findProfile(requestedUser);
                ViewProfile viewProfile = new ViewProfile(profile, ioMachine);

                viewProfile.run();
            }
            if (e.getSource() == sendFriendRequest) {
                String requestedUser = (String) users.getSelectedItem();
                if (ioMachine.sendRequest(getUsername(), requestedUser)) {
                    JOptionPane.showMessageDialog(null, "Friend Request Sent!",
                            "CampsGram", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Unable to send Friend Request." +
                            " Please Try Again.", "CampsGram", JOptionPane.ERROR_MESSAGE);
                }

            }
        }
    };

    //Code that Runs every 3 seconds, updating the profile list
    transient ActionListener refresher = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            //saves the current selection
            String currentSelection = (String) users.getSelectedItem();

            //retrieves the complete list of users, remove the current users name from the list
            ArrayList<String> usernameList = ioMachine.viewAllProfiles();
            usernameList.remove(getUsername());

            //sets the usernames to the current list of users
            String[] usernames = usernameList.toArray(new String[usernameList.size()]);
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(usernames);
            users.setModel(model);

            //sets the selection what it was previously
            users.setSelectedItem(currentSelection);

            //updates the GUI
            frame.revalidate();
            bottomPanel.repaint();
        }
    };

    public void writeExportFile() throws IOException {

        try {
            //creating new file and printWriter for this profile
            File f = new File(this.username + "Export.csv");
            PrintWriter pw = new PrintWriter(new FileWriter(f, false));

            pw.print(this.username + ',' + interests.size() + ',');

            //loop to write each element of interests list to file
            for (int i = 0; i < interests.size(); i++) {
                pw.print(this.interests.get(i) + ',');
            }

            pw.print(this.friends.size());
            pw.print(",");

            //loop to write each element of friends list to file
            for (int x = 0; x < friends.size(); x++) {
                pw.print(friends.get(x) + ',');
            }

            pw.print(this.getRequestsReceived().size());
            pw.print(",");

            //loop to write each element of friend requests received list to file
            for (int x = 0; x < requestsReceived.size(); x++) {
                pw.print(requestsReceived.get(x) + ',');
            }

            pw.print(this.getRequestsSent().size());
            pw.print(",");

            //loop to write each element of friend requests sent list to file
            for (int x = 0; x < requestsSent.size(); x++) {
                pw.print(requestsSent.get(x) + ',');
            }

            pw.print(this.education + ',' + this.email + ',' + this.phoneNumber + ',' + this.aboutMe);

            pw.close();

        } catch (IOException e) { //If the file is open or another error occurs, display this error message
            JOptionPane.showMessageDialog(null, "There was an error exporting your file, please " +
                    "close the file if it is open on your device", "CamsGram", JOptionPane.ERROR_MESSAGE);
        }

    }

    public String getUsername() {
        return this.username;
    }

    public ArrayList<String> getInterests() {
        return this.interests;
    }

    public ArrayList<String> getFriends() {
        return this.friends;
    }

    public String getEducation() {
        return this.education;
    }

    public String getEmail() {
        return this.email;
    }

    public long getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getAboutMe() {
        return this.aboutMe;
    }

    public ArrayList<String> getRequestsSent() {
        return this.requestsSent;
    }

    public ArrayList<String> getRequestsReceived() {
        return this.requestsReceived;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public void addFriend(String friend) {
        this.friends.add(friend);
    }

    public void removeFriends(String friend) { 

        for (int i = 0; i < friends.size(); i++) {

            if (friends.get(i).equals(friend)) {
                friends.remove(i);
            }
        }
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public void addInterest(String interest) {
        this.interests.add(interest);
    }

    public void removeInterest(String interest) {

        for (int i = 0; i < interests.size(); i++) {

            if (interests.get(i).equals(interest)) {
                interests.remove(i);
            }
        }
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRequestsSent(ArrayList<String> requestsSent) {
        this.requestsSent = requestsSent;
    }

    public void addSentRequest(String username) {
        this.requestsSent.add(username);
    }

    public void removeSentRequest(String username) {
        for (int i = 0; i < requestsSent.size(); i++) {

            if (requestsSent.get(i).equals(username)) {
                requestsSent.remove(i);
            }
        }
    }

    public void setRequestsReceived(ArrayList<String> requestsReceived) {
        this.requestsReceived = requestsReceived;
    }

    public void addReceivedRequest(String username) {
        this.requestsReceived.add(username);
    }

    public void removeReceivedRequest(String username) {
        for (int i = 0; i < requestsReceived.size(); i++) {

            if (requestsReceived.get(i).equals(username)) {
                requestsReceived.remove(i);
            }
        }
    }


}
