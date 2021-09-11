import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * testIOMachine
 *
 * This class was made to test the methods in IOMachine.java. Each method is tested with inputs that
 * will result in successful operation, as well as inputs that will result in failed operation. This
 * is done through running this class alongside the server programs.
 *
 * @author Team 60, Section 11
 * @version 5/03/2021
 *
 */

public class testIOMachine implements Serializable {

    public static void main(String[] args) throws IOException {

        try { //runs client side stuff

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 1234
            Socket s = new Socket(ip, 1234);   //CHANGE FOR USE WITH UNITY TO 5555

            // obtaining input and out streams
            ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream dis = new ObjectInputStream(s.getInputStream());

            //Confirm connection successful
            JOptionPane.showMessageDialog(null, "Connection Established", "CampsGram",
                    JOptionPane.INFORMATION_MESSAGE);

            //creates an IOMachine to easily access the server
            IOMachine ioMachine = new IOMachine(dos, dis);

            //initializing fields to create new profile and account objects
            ArrayList<String> interests = new ArrayList<>(); //interests list
            interests.add("running");
            interests.add("gaming");
            interests.add("tech");

            ArrayList<String> friends = new ArrayList<>(); //friends list
            friends.add("Joe");
            friends.add("Jack");
            friends.add("Nick");

            ArrayList<String> requestsSent = new ArrayList<>(); //friend requests sent
            requestsSent.add("1");
            requestsSent.add("2");

            ArrayList<String> requestsReceived = new ArrayList<>(); //friend requests received
            requestsReceived.add("1");
            requestsReceived.add("2");

            //creating new profile objects to use in testing
            Profile pf = new Profile("chen3801", interests, friends, "Purdue University",
                    "jchsda@gmail.com", 1231231234L, "jdsfhladhflajdshfladhflal " +
                    "dsfasdfhlahfl",
                    requestsSent, requestsReceived);

            Profile pf1 = new Profile("chen380", interests, friends, "Purdue University",
                    "jchsda@gmail.com", 1231231234L, "jdsfhladhflajdshfladhflal " +
                    "dsfasdfhlahfl",
                    requestsSent, requestsReceived);

            ArrayList<Profile> pfList = new ArrayList<>();

            pfList.add(pf);

            //creating new account objects to use in testing
            Account newAccount = new Account("jchsda@gmail.com", "asahdlh@dahl123", pfList);

            Account newAccount2 = new Account("jch000@gmail.com", "asahdlh@dahl123", pfList);

            //Success Case: addAccount()
            // adds account to server successfully
            try {
                ioMachine.addAccount(newAccount);
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: addAccount()
            // fails to add account to server due to incorrect input
            try {
                ioMachine.addAccount(pf);
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: findAccount()
            // finds account in server successfully
            try {
                Account account = ioMachine.findAccount("jchsda@gmail.com");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: findAccount()
            // returns false due to failing to find account associated with this email
            try {
                Account account = ioMachine.findAccount("NaN@gmail.com");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: deleteAccount()
            // deletes account from server successfully
            try {
                boolean accDeleted = ioMachine.deleteAccount(newAccount);
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: deleteAccount()
            // fails to delete account due to invalid input
            try {
                boolean accDeleted = ioMachine.deleteAccount(pf);
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: addProfile()
            // successfully adds a profile to the account associated with that email
            try {
                ioMachine.addProfile(pf1, "jchsda@gmail.com");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: addProfile()
            // fails to add a profile to the account due to no account associated with the email
            try {
                ioMachine.addProfile(pf1, "NaN@gmail.com");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: findProfile()
            // successfully finds profile
            try {
                Profile profile = ioMachine.findProfile("chen3801");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: findProfile()
            // fails to find profile due to no such profile existing
            try {
                Profile profile = ioMachine.findProfile("NaN");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: deleteProfile()
            // deletes profile successfully
            try {
                boolean deleted = ioMachine.deleteProfile("chen3801");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: deleteProfile()
            //fails to delete profile due to invalid input
            try {
                boolean deleted = ioMachine.deleteProfile("NaN");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: editProfile()
            // edits profile successfully
            try {
                boolean profileEdit = ioMachine.editProfile("chen3801",
                        "PhoneNumber", "2342342345");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: editProfile()
            // fails to edit profile due to invalid input for the fields
            try {
                boolean profileEdit = ioMachine.editProfile("NaN",
                        "Address", "NaNNaNNaNNaN");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: editProfileList()
            // edits list fields for specified profile successfully
            try {
                boolean profileEditList = ioMachine.editProfileList("Add",
                        "chen3801", "Interest", "sleeping");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: editProfileList()
            // fails to edit list field due to invalid input
            try {
                boolean profileEditList = ioMachine.editProfileList("Pause",
                        "NaN", "NaN", "NaN");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: sendRequest()
            // successfully sends a friend request to specified profile
            try {
                boolean requestSent = ioMachine.sendRequest("chen3801", "chen380");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: sendRequest()
            // fails to send a friend request due to invalid input
            try {
                boolean requestSent = ioMachine.sendRequest("NaN", "NaNNaN");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: rescindRequest()
            // successfully rescinds friend request
            try {
                boolean rescindRequest = ioMachine.rescindRequest("chen3801", "chen380");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: rescindRequest()
            // fails to rescind friend request due to improper input
            try {
                boolean rescindRequest = ioMachine.rescindRequest("NaN", "NaNNaN");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: acceptFriend()
            // successfully accepts specified friend request
            try {
                boolean friendAccepted = ioMachine.acceptFriend("chen3801", "chen380");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: acceptFriend()
            // fails to accept
            try {
                boolean friendAccepted = ioMachine.acceptFriend("NaN", "NaNNaN");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: declineFriend()
            // successfully declines friend request
            try {
                boolean friendDeclined = ioMachine.declineFriend("chen3801", "chen380");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: declineFriend()
            // fails to decline friend request due to invalid input
            try {
                boolean friendDeclined = ioMachine.declineFriend("NaN", "NaNNaN");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: unfriend()
            // successfully unfriends specified profile
            try {
                boolean unfriended = ioMachine.unfriend("chen3801", "chen380");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Failure Case: unfriend()
            // fails to unfriend profile due to invalid input
            try {
                boolean unfriended = ioMachine.unfriend("NaN", "NaNNaN");
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }

            //Success Case: viewAllProfiles()
            // successfully assigns arraylist of strings containing all usernames to variable
            try {
                ArrayList<String> viewAllProfiles = ioMachine.viewAllProfiles();
            } catch (Exception e) {
                System.out.println("Error: An Exception Has Occurred");
            }


        } catch (IOException e) { //display error message is connection not established
            JOptionPane.showMessageDialog(null, "Error: Connection Cannot be Established",
                    "Campsgram", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: There was an Error in the code " +
                    "somewhere", "Campsgram", JOptionPane.ERROR_MESSAGE);
        }

    }
}
