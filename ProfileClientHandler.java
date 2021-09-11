import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * ProfileClientHandler
 *
 * This class contains the server side processing and user interface logic for the project. It is created from a thread
 * created by the ProfileServer class. The constructor allows multiple objects of this type to be created, allowing for
 * multiple clients to connect to the server.
 *
 * Geeksforgeeks was referenced to determine how to utilize threads for multiple client-server connections
 *
 * @author Team 60, Section 11
 * @version 5/03/2021
 *
 */

class ProfileClientHandler extends Thread {

    final ObjectInputStream dis; //data input stream used to communicate with the server
    final ObjectOutputStream dos; //data output stream used to communicate with the server
    final Socket s; //socket used to communicate with the server
    ArrayList<Account> accounts = ProfileServer.getAccounts();  //the master arraylist of accounts on the server

    /**
     * Creates a new ProfileClientHandler object
     *
     * @param s data input stream used to communicate with the server
     * @param dis data output stream used to communicate with the server
     * @param dos socket used to communicate with the server
     */

    public ProfileClientHandler(Socket s, ObjectInputStream dis, ObjectOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    /**
     * Runs functionality for the server. This involves a case/switch structure that reads a command from the client.
     * Each command has associated functionality, and this functionality will run when the proper command is sent.
     * The majority of cases send a true/false string to the client to indicate successful or unsuccessful completion
     * of the functionality.
     */

    @Override
    public void run() {

        //Initialize variables common across multiple cases

        String command;  //command from the client to access functionality
        String username;  //username passed from client
        String email;  //email passed from the client
        String password;  //password passed from client
        String parameter;  // generic parameter passed by the client
        boolean objectFound;  //whether or not an object was found for a search (usually profile or account)
        Profile profile;  //profile sent by the client
        ArrayList<Profile> profiles;  //profile arraylist sent by client
        ArrayList<String> usernames;  //string arraylist, generally of usernames, passed to the client

        try {
            while (true) { //loops infinitely so that client send commands are read properly

                switch(command = (String) dis.readObject()) { //looks for command sent from client

                    case "AddProfile": //adds a profile to an account based on account email

                        //read in and initialize variables
                        email = (String) dis.readObject();
                        profile = (Profile) dis.readObject();
                        objectFound = false;

                        for (int i = 0; i < accounts.size(); i++) { //search the accounts for a matching email

                            if (accounts.get(i).getEmail().equals(email)) { //if account matches, add passed profile
                                accounts.get(i).addProfile(profile);
                                dos.writeObject("True");
                                objectFound = true;
                            }
                        }

                        if (!objectFound) { //if account not found, send back false
                            dos.writeObject("False");
                        }

                        break;

                    case "DeleteProfile": //Deletes a profile based on username

                        //read in and initialize variables
                        username = (String) dis.readObject();
                        objectFound = false;

                        for (int i = 0; i < accounts.size(); i++) {  //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) {  //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) {  //if the
                                    // username of the profile matches
                                    dos.writeObject("True");
                                    accounts.get(i).deleteProfile(username);
                                    objectFound = true;
                                }

                            }
                        }

                        if (!objectFound) { //if account not found, send back false
                            dos.writeObject("False");
                        }

                        break;

                    case "SendProfile": //Sends a profile specified by username to the client.

                        //read in and initialize variables
                        objectFound = false;
                        username = (String) dis.readObject();

                        for (int i = 0; i < accounts.size(); i++) {  //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) {  //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) {  //if profile
                                    // username matches
                                    dos.writeObject("True");
                                    dos.writeObject(accounts.get(i).getProfiles().get(j));
                                    objectFound = true;
                                }
                            }
                        }

                        if (!objectFound) { //if a matching profile is not found
                            dos.writeObject("False");
                        }

                        break;

                    case "SendAllProfiles": //Sends a profile specific by username to the client.

                        //read in and initialize variables
                        objectFound = false; //if profiles exist on the server
                        usernames = new ArrayList<String>();

                        for (int i = 0; i < accounts.size(); i++) { //iterate accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) { //iterate profiles

                                usernames.add(accounts.get(i).getProfiles().get(j).getUsername());
                                objectFound = true;

                            }
                        }

                        if (!objectFound) { //if a matching profile is not found
                            dos.writeObject("False");
                        } else {
                            dos.writeObject("True");
                            dos.writeObject(usernames);
                        }

                        break;

                    case "AddAccount": //Creates an account and adds it to the Account Master ArrayList

                        //read in and initialize parameters
                        email = (String) dis.readObject();
                        password = (String) dis.readObject();
                        profiles = (ArrayList<Profile>) dis.readObject();

                        //create new account and add it to the Account Master ArrayList
                        accounts.add(new Account(email, password, profiles));

                        dos.writeObject("True");

                        break;

                    case "DeleteAccount": //Deletes an account from the Account Master Arraylist

                        //read in and initialize parameters
                        objectFound = false;
                        email = (String) dis.readObject();

                        for (int i = 0; i < accounts.size(); i++) { //search accounts for a matching email

                            if (accounts.get(i).getEmail().equals(email)) { //if the account is found

                                dos.writeObject("True");
                                accounts.remove(i);
                                objectFound = true;
                            }
                        }

                        if (!objectFound) { //if a matching account is not found
                            dos.writeObject("False");
                        }

                        break;

                    case "SendAccount":  //sends an account to the client based on email

                        //read in and initialize parameters
                        objectFound = false;
                        email = (String) dis.readObject();

                        for (int i = 0; i < accounts.size(); i++) {  //search accounts for a matching email

                            if (accounts.get(i).getEmail().equals(email)) {  //if the account is found

                                dos.writeObject("True");
                                dos.writeObject(accounts.get(i).getPassword());
                                dos.writeObject(accounts.get(i).getProfiles());

                                objectFound = true;
                            }
                        }

                        if (!objectFound) {  //if a matching account is not found
                            dos.writeObject("False");
                        }

                        break;

                    case "AddInterest":  //Adds an interest to a profile

                        //read in and initialize parameters
                        objectFound = false;
                        username = (String) dis.readObject();
                        parameter = (String) dis.readObject();

                        for (int i = 0; i < accounts.size(); i++) {  //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) {  //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) {  //if the
                                    // profile is found
                                    dos.writeObject("True");
                                    accounts.get(i).getProfiles().get(j).addInterest(parameter);
                                    objectFound = true;
                                }

                            }
                        }

                        if (!objectFound) {  //if account not found, send back false
                            dos.writeObject("False");
                        }

                        break;

                    case "RemoveInterest":  //Removes an interest from a profile

                        objectFound = false;
                        username = (String) dis.readObject();
                        parameter = (String) dis.readObject();

                        for (int i = 0; i < accounts.size(); i++) {  //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) {  //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) {  //if profile
                                    // found

                                    for (int k = 0; k < accounts.get(i).getProfiles().get(j).getInterests().size();
                                         k++) {  //search interests

                                        if (accounts.get(i).getProfiles().get(j).getInterests().get(k).equals(parameter
                                        )) {  //if interest found
                                            accounts.get(i).getProfiles().get(j).getInterests().remove(k);
                                            objectFound = true;
                                        }
                                    }
                                }
                            }
                        }

                        if (!objectFound) { //if account not found, send back false
                            dos.writeObject("False");
                        }

                        break;

                    case "EditEducation":  //edits the education of a profile found based on username

                        //read in and initialize parameters
                        objectFound = false;
                        username = (String) dis.readObject();
                        parameter = (String) dis.readObject();

                        for (int i = 0; i < accounts.size(); i++) {  //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) {  //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) {  //if the
                                    // profile is found
                                    dos.writeObject("True");
                                    accounts.get(i).getProfiles().get(j).setEducation(parameter);
                                    objectFound = true;
                                }

                            }
                        }

                        if (!objectFound) {  //if account not found, send back false
                            dos.writeObject("False");
                        }

                        break;

                    case "EditEmail":  //edits the email of a profile found based on username

                        objectFound = false;
                        username = (String) dis.readObject();
                        parameter = (String) dis.readObject();

                        for (int i = 0; i < accounts.size(); i++) {  //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) {  //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) { //if the
                                    // profile is found
                                    dos.writeObject("True");
                                    accounts.get(i).getProfiles().get(j).setEmail(parameter);
                                    objectFound = true;
                                }

                            }
                        }

                        if (!objectFound) {  //if account not found, send back false
                            dos.writeObject("False");
                        }

                        break;

                    case "EditPhoneNumber":  //edits the phone number of a profile found based on username

                        //read in and initialize parameters
                        objectFound = false;
                        username = (String) dis.readObject();
                        parameter = (String) dis.readObject();

                        for (int i = 0; i < accounts.size(); i++) { //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) { //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) { //if the
                                    // profile is found
                                    dos.writeObject("True");
                                    accounts.get(i).getProfiles().get(j).setPhoneNumber(Long.parseLong(parameter));
                                    objectFound = true;
                                }

                            }
                        }

                        if (!objectFound) {  //if account not found, send back false
                            dos.writeObject("False");
                        }

                        break;

                    case "EditAboutMe":  //edits the about me section of a profile found based on username

                        //read in and initialize parameters
                        objectFound = false;
                        username = (String) dis.readObject();
                        parameter = (String) dis.readObject();

                        for (int i = 0; i < accounts.size(); i++) {  //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) {  //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) {  //if the
                                    // profile is found
                                    dos.writeObject("True");
                                    accounts.get(i).getProfiles().get(j).setAboutMe(parameter);
                                    objectFound = true;
                                }

                            }
                        }

                        if (!objectFound) {  //if account not found, send back false
                            dos.writeObject("False");
                        }

                        break;

                    case "AddFriend":  //Accepts a friend request

                        objectFound = false;
                        username = (String) dis.readObject();  //profile being edited
                        parameter = (String) dis.readObject();  //profile being added/removed

                        for (int i = 0; i < accounts.size(); i++) {  //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) {  //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) { //if the
                                    // profile is found
                                    dos.writeObject("True");
                                    accounts.get(i).getProfiles().get(j).addFriend(parameter);
                                    objectFound = true;
                                }

                            }
                        }

                        if (!objectFound) {  //if account not found, send back false
                            dos.writeObject("False");
                        }

                        break;

                    case "RemoveFriend":  //removes a friend from a profile

                        objectFound = false;
                        username = (String) dis.readObject();  //profile being edited
                        parameter = (String) dis.readObject();  //profile being added/removed

                        for (int i = 0; i < accounts.size(); i++) {  //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) {  //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) {  //if profile
                                    //is found

                                    for (int k = 0; k < accounts.get(i).getProfiles().get(j).getFriends().size(); k++)
                                    {  //search friends

                                        if (accounts.get(i).getProfiles().get(j).getFriends().get(k).equals(parameter))
                                        {  //if friend found

                                            accounts.get(i).getProfiles().get(j).getFriends().remove(k);
                                            objectFound = true;
                                            dos.writeObject("True");
                                        }
                                    }
                                }
                            }
                        }

                        if (!objectFound) {  //if account not found, send back false
                            dos.writeObject("False");
                        }

                        break;

                    case "AddRequestsSent":  //adds a friend request sent

                        //read in and initialize variables
                        objectFound = false;
                        username = (String) dis.readObject();  //profile being edited
                        parameter = (String) dis.readObject();  //profile being added/removed

                        //add received request
                        for (int i = 0; i < accounts.size(); i++) {  //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) {  //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) {  //if
                                    // profile found
                                    accounts.get(i).getProfiles().get(j).addSentRequest(parameter);
                                    objectFound = true;
                                }
                            }
                        }

                        if (!objectFound) {  //if the profile is not found
                            dos.writeObject("False");

                        } else {

                            dos.writeObject("True");

                        }

                        break;

                    case "RemoveRequestsSent":  //removes a sent friend request

                        //read in and initialize parameters
                        objectFound = false;
                        username = (String) dis.readObject(); //profile being edited
                        parameter = (String) dis.readObject(); //profile being added/removed

                        //add received request
                        for (int i = 0; i < accounts.size(); i++) { //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) { //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) { //if profile
                                    // found
                                    accounts.get(i).getProfiles().get(j).removeSentRequest(parameter);
                                    objectFound = true;
                                }
                            }
                        }

                        if (!objectFound) {  //if the profile is not found
                            dos.writeObject("False");
                            break;
                        }

                        dos.writeObject("True");

                        break;

                    case "AddRequestsReceived":  //add a friend request received

                        //read in and initialize parameters
                        objectFound = false;
                        username = (String) dis.readObject(); //profile being edited
                        parameter = (String) dis.readObject(); //profile being added/removed

                        //add received request
                        for (int i = 0; i < accounts.size(); i++) { //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) { //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) { //if profile
                                    // found
                                    accounts.get(i).getProfiles().get(j).addReceivedRequest(parameter);
                                    objectFound = true;
                                }
                            }
                        }

                        if (!objectFound) {  //if the profile is not found
                            dos.writeObject("False");
                            break;
                        }

                        dos.writeObject("True");

                        break;

                    case "RemoveRequestsReceived":  //removes a received friend request

                        //read in and initialize parameters
                        objectFound = false;
                        username = (String) dis.readObject(); //profile being edited
                        parameter = (String) dis.readObject(); //profile being added/removed

                        //add received request
                        for (int i = 0; i < accounts.size(); i++) { //search accounts

                            for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) { //search profiles

                                if (accounts.get(i).getProfiles().get(j).getUsername().equals(username)) { //if profile
                                    // found
                                    accounts.get(i).getProfiles().get(j).removeReceivedRequest(parameter);
                                    objectFound = true;
                                }
                            }
                        }

                        if (!objectFound) {  //if the profile is not found
                            dos.writeObject("False");
                            break;
                        }

                        dos.writeObject("True");

                        break;

                    default:  //if no cases match the command
                        System.out.println("You sent to the server, but didn't match a case. Command was: " + command);


                }

            }
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Error: There was a connection issue",
                   // "Campsgram", JOptionPane.ERROR_MESSAGE);

            try {
                //Create or overwrite backup file to save the server if there is an exception
                File f = new File("ServerBackupFile.csv");
                PrintWriter pw = new PrintWriter(new FileWriter(f, false));

                for(int i = 0; i < accounts.size(); i++) { //iterates through every account on the server

                    //write the email and password for each account to a CSV file
                    pw.print(accounts.get(i).getEmail() + ",");
                    pw.print(accounts.get(i).getPassword());

                    //loops through the profiles associated with an account, exports them using the
                    // writeExportFile() method in Profile.java, and writes the username of the profile to the CSV file
                    for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) {
                        accounts.get(i).getProfiles().get(j).writeExportFile();
                        pw.print("," + accounts.get(i).getProfiles().get(j).getUsername());
                    }

                    //prints a new line for the next account to be written
                    pw.println();
                }

                pw.close();
                System.out.println("The server was backed up");

            } catch (Exception f) {
                f.printStackTrace();
                System.out.println("The server was not backed up");
            }

        }

        try {
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            //JOptionPane.showMessageDialog(null, "Error: There was a connection issue",
                   // "Campsgram", JOptionPane.ERROR_MESSAGE);

            try {
                //Create or overwrite backup file to save the server if there is an exception
                File f = new File("ServerBackupFile.csv");
                PrintWriter pw = new PrintWriter(new FileWriter(f, false));

                for(int i = 0; i < accounts.size(); i++) { //iterates through every account on the server

                    //write the email and password for each account to a CSV file
                    pw.print(accounts.get(i).getEmail() + ",");
                    pw.print(accounts.get(i).getPassword());

                    //loops through the profiles associated with an account, exports them using the
                    // writeExportFile() method in Profile.java, and writes the username of the profile to the CSV file
                    for (int j = 0; j < accounts.get(i).getProfiles().size(); j++) {
                        accounts.get(i).getProfiles().get(j).writeExportFile();
                        pw.print("," + accounts.get(i).getProfiles().get(j).getUsername());
                    }

                    //prints a new line for the next account to be written
                    pw.println();
                }

                pw.close();
                System.out.println("The server was backed up");

            } catch (Exception f) {
                f.printStackTrace();
                System.out.println("The server was not backed up");
            }

        }
    }
}
