import javax.swing.*;
import java.util.ArrayList;
/**
 * EnterInfoGUI.java
 *
 * This program prompts contains all the input
 * dialogs that asks the user information
 * for the creation of a profile.
 *
 * @author Team 060, Section 11
 * @version May 03, 2021
 * */
public class EnterInfoGUI {
    //declaration of final String arrays of state and university options
    private static final String[] statesOptions = {"Arizona", "Indiana", "Ohio", "Virginia"};

    private static final String[] arizonaOptions = {"Northern Arizona University", "Prescott College"};

    private static final String[] indianaOptions = {"Notre Dame", "Purdue University",
            "Indiana University", "Butler University"};
    private static final String[] ohioOptions = {"Ohio University", "Oberlin College"};

    private static final String[] virginiaOptions = {"University of Richmond", "Virginia Polytechnic",
            "Liberty University", "University of Virginia"};
    public static void main (String[] args) {
        String username; //unique username for the profile
        long phone; //phone of the user
        String email; //email of the user
        String education; //the university and state of the user
        String about; //extra information about the user
        String formattedAbout; //formats the About String
        ArrayList<String> interests; //interests of the user
        String formattedInterests; //interests formatted as a vertical list
        String output;   //contains all information of the user in order

        username = showUsernameInputDialog(); //calls the method that proper user to enter username
        phone = showPhoneInputDialog(); //calls the method that prompts user to enter phone
        email = showEmailInputDialog(); //calls the method that prompts user to enter name
        education = showEducationInputDialog(); //calls the method that prompts user to enter state, university
        about = showAboutInputDialog();  //calls the method that prompts user to enter about
        formattedAbout = formatAboutString(about); //calls the method that formats the About String
        interests = showInterestsInputDialog(); //calls the method that prompts user to enter interests
        formattedInterests = formatInterestsString(interests);
        showCreatingDialog(); // shows message indicating the profile is being created

        output = String.format("User Information:\n" + //contains all user information in order
                        "Username: %s\n" +
                        "Phone: %d\n" +
                        "Email: %s\n" +
                        "Education: %s\n" +
                        "About: %s\n" +
                        "Interests: %s\n", username, phone, email, education, formattedAbout, formattedInterests);
        System.out.println(output); //prints output
    } //end main

    public static String formatAboutString(String about) { //organizes the About information as a small paragraph
        String about2 = ""; //initializes about2 as empty
        String newAbout = about; //duplicates the String about
        boolean checking = false; //checker that notifies become true if while loop runs
        while (newAbout.length() > 30) { //while-loop runs if line is too long
            checking = true; //updates checker
            if (newAbout.charAt(30) == ' ') { //checks if where the text cuts has a space
                newAbout = newAbout.substring(0,30) + newAbout.substring(31); //deletes the extra space
                about = about2 + newAbout.substring(0, 30) + "\n"; //formats the String
            } else {
                about = about2 + newAbout.substring(0, 30) + "-\n"; //formats the String
            } //end if
            about2 = about; //updates about2
            newAbout = newAbout.substring(30); //updates NewAbout
        }
        if (checking) { //checks if while loop ran
            return "\n" + about + newAbout; //returns formatted About Info String
        } else {
            return "\n" + about; //returns normal About Info String
        } //end if
    } //formatAboutString

    public static String formatInterestsString(ArrayList<String> interests) { //converts array to formatted String
        String newInterests =  ""; //initializes String to be empty
        //returns Interests as formatted String
        if (interests == null) {
            newInterests = "User did not include any interests.";
        } else {
            for(int i = 0; i < interests.size(); i++) { //for-loop that updates 'elements' with all the interests
                newInterests = newInterests + "\n  "+ interests.get(i); //formats elements into a vertical list
            } //for-loop
        }
        return newInterests; //returns the formatted list of interests
    } //formatInterestsString

    public static String showUsernameInputDialog() {
        String username; //username of the user
        do {
            username = JOptionPane.showInputDialog(null, "Create Username:",
                    "CampsGram", JOptionPane.QUESTION_MESSAGE); //asks user to enter username
            if (username == null) {
                JOptionPane.showMessageDialog(null, "Must input a username!",
                        "CampsGram", JOptionPane.ERROR_MESSAGE);
            } else if (username.equals("")) { //checks if username is null
                JOptionPane.showMessageDialog(null, "Username cannot be empty!",
                        "CampsGram", JOptionPane.ERROR_MESSAGE); //shows error
            } else if (username.contains(" ")) {
                JOptionPane.showMessageDialog(null, "Username can only be a single word!",
                        "CampsGram", JOptionPane.ERROR_MESSAGE); //shows error
            } //end if
        } while (username == null || username.equals("") || username.contains(" ")); //do-while runs while username.. 
        // ..is null or it has a space
        return username; //returns the username for the profile
    } //showUsernameInputDialog

    public static long showPhoneInputDialog() {
        String phone; //phone number of user
        String phone2; //phone number duplicate
        long phoneLong = 0; //initializes the phone as 0
        boolean checking = true; //initializes 'checking' as true
        do {
            phone = JOptionPane.showInputDialog(null, "Phone Number:",
                    "CampsGram", JOptionPane.QUESTION_MESSAGE); //asks user to input phone number
            phone2 = phone;  //duplicates the phone number to check validity later
            try {
                if (phone == null) {
                    phoneLong = 1111111111;
                    checking = false;
                    break;
                } else if (phone.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid phone number!",
                            "CampsGram", JOptionPane.ERROR_MESSAGE); //shows error
                } else {
                    phoneLong = Long.parseLong(phone); //throws exception is phone number is not a Long
                    checking = false; //makes loop stop if no exception is thrown
                    if (phone.length() != 10) { //checks if phone number has more than 10 digits
                        JOptionPane.showMessageDialog(null, 
                                "Please enter a valid phone number! " +
                                        "It should be 10 digits long!", "CampsGram",
                                JOptionPane.ERROR_MESSAGE); //shows error
                        checking = true; //makes loop run again which asks user to input again
                    } else if (phone.substring(0, 1).equals("0")) {
                        JOptionPane.showMessageDialog(null, 
                                "Please enter a valid phone number!" +
                                        " Phone number cannot start with 0!", "CampsGram",
                                JOptionPane.ERROR_MESSAGE); //shows error
                        checking = true; //makes loop run again which asks user to input again
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid phone number!",
                        "CampsGram", JOptionPane.ERROR_MESSAGE); //shows error
            } //try-catch block
        } while (checking); //do-while runs while 'checking' is true
        //formats the phone number correctly
        return phoneLong; //returns phone String in correct format
    } //showPhoneInputDialog

    public static String formatPhoneString(long phone) {
        String phoneS = String.valueOf(phone);
        String phoneFormat = ""; //declares phone number formatted
        if (phone == 1111111111) {
            phoneFormat = "Phone Number Not Specified";
        } else {
            phoneFormat = "(" + phoneS.substring(0, 3)+ ") " + phoneS.substring(3,6) + "-" + phoneS.substring(6, 10);
            //formats the phone number correctly
        }
        return phoneFormat; //returns phone String in correct format
    } //formatPoneString

    public static String showEmailInputDialog() {
        String email; //email of the user
        boolean checking = true; //initializes 'checking' as true
        do {
            email = JOptionPane.showInputDialog(null, "Email: ",
                    "CampsGram", JOptionPane.QUESTION_MESSAGE); //asks user to input email

            if (email == null) {
                email = "Email Not Specified";
                checking = false;
                break;
            } else if (email.equals("")) {
                JOptionPane.showMessageDialog(null, "Email cannot be empty!",
                        "CampsGram", JOptionPane.ERROR_MESSAGE); //shows error
            } else if (!email.contains("@") || !email.contains(".")) { //if email does not contain "@" or "."
                JOptionPane.showMessageDialog(null, "Please enter a valid  email!",
                        "CampsGram", JOptionPane.ERROR_MESSAGE); //shows error
            } else {
                checking = false; //updates checking to false
            }//end if
        } while (checking); //do-while loop runs while 'checking' is true
        return email; //returns String email
    } //showEmailInputDialog

    public static ArrayList<String> showInterestsInputDialog() {
        ArrayList<String> interests = new ArrayList<>(); //List with user interests
        String interest; //personal interests of the user
        String elements = ""; //personal interests formatted
        boolean checking = true; //initializes 'checking' as true
        do {
            try {
                interest = JOptionPane.showInputDialog(null,
                        "Enter personal interests separated by commas: ", "CampsGram",
                        JOptionPane.QUESTION_MESSAGE); //asks user to input interests
                interest = interest.replace(" ", "");

                interest = interest.toLowerCase();
                if(interest == null) {
                    checking = false;
                    break;
                } else if (interest.equals("")) { //checks if interest ArrayList is null
                    JOptionPane.showMessageDialog(null, "Please enter your interests!",
                            "CampsGram", JOptionPane.ERROR_MESSAGE); //shows error
                } else {
                    if (!interest.contains(",")) {
                        interests.add(interest);
                    } else {
                        String [] array = interest.split(",");
                        for (int i = 0; i < array.length; i++) {
                            interests.add(array[i]);
                        }
                    }
                    checking = false; //stops loops since interests format is valid
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please format your interests correctly!",
                        "CamsGram", JOptionPane.ERROR_MESSAGE); //shows error
            } //try-catch block
        } while (checking); //do-while loops runs while 'checking' is true
        return interests; //returns the String 'elements' will all the interests formatted correctly
    } //showInterestsInputDialog

    public static String showAboutInputDialog() {
        String about; //extra information about the user
        about = JOptionPane.showInputDialog(null, "Information about yourself " +
                        "(Leave empty if you do not wish to add this to your profile) : ", "CampsGram",
                JOptionPane.QUESTION_MESSAGE); //asks user to input information about themselves
        if (about == null) {
            about = "User decided not to share info!"; //displays when user leaves input empty
        } else if (about.equals("")) {
            about = "User decided not to share info"; //displays when user leaves input empty
        }
        return about; //returns String 'about'
    } //showAboutInputDialog

    public static String showEducationInputDialog() {
        String state =""; //state of the user
        String university = ""; //university of the user
        do { //do-while
            state = (String) JOptionPane.showInputDialog(null, "Select your U.S. State:",
                    "CampsGram", JOptionPane.QUESTION_MESSAGE, null, statesOptions,
                    statesOptions[0]); //makes user select a US State
            if(state == null) {
                state = "State not specified";
                break;
            } else if (state.equals("")) { //checks if state choice is null
                JOptionPane.showMessageDialog(null, "Select a State!", "CampsGram",
                        JOptionPane.ERROR_MESSAGE); //shows error
            } //end if
        } while (state.equals("")); //do-while loop runs while state is null
        if (state == null) {
            university = "University not Specified";
        } else if (state.equals("Arizona")) { //if user selects Arizona as their state
            university = (String) JOptionPane.showInputDialog(null, "Select your University:",
                    "CampsGram", JOptionPane.QUESTION_MESSAGE, null, arizonaOptions,
                    arizonaOptions[0]); //makes user choose between Arizona options
            if (university == null) {
                university = "University not Specified";
            } else if (university.equals("")) { //checks if university is empty
                JOptionPane.showMessageDialog(null, "Select a University!", "CampsGram",
                        JOptionPane.ERROR_MESSAGE); //shows error
            } //end if
        } else if (state.equals("Indiana")) { //if user selects Indiana as their state
            university = (String) JOptionPane.showInputDialog(null, "Select your University:",
                    "CampsGram", JOptionPane.QUESTION_MESSAGE, null, indianaOptions,
                    indianaOptions[0]); //makes user select between Indiana universities
            if (university == null) {
                university = "University not Specified";
            } else if (university.equals("")) { //checks if university choice is empty
                JOptionPane.showMessageDialog(null, "Select a University!", "CampsGram",
                        JOptionPane.ERROR_MESSAGE); //shows error
            } //end if
        } else if (state.equals("Ohio")) { //if user selects Ohio as their state
            university = (String) JOptionPane.showInputDialog(null, "Select your University:",
                    "CampsGram", JOptionPane.QUESTION_MESSAGE, null, ohioOptions,
                    ohioOptions[0]); //makes user choose between Ohio universities
            if (university == null) {
                university = "University not Specified";
            } else if (university.equals("")) { //checks if university choice is empty
                JOptionPane.showMessageDialog(null, "Select a University!", "CampsGram",
                        JOptionPane.ERROR_MESSAGE); //shows error
            } //end if
        } else if (state.equals("Virginia")) { //if user selects Virgina as their state
            university = (String) JOptionPane.showInputDialog(null, "Select your University:",
                    "CampsGram", JOptionPane.QUESTION_MESSAGE, null, virginiaOptions,
                    virginiaOptions[0]); //make user choose between Virgina universities
            if (university == null) {
                university = "University not Specified";
            } else if (university.equals("")) { //checks if university choice is empty
                JOptionPane.showMessageDialog(null, "Select a University!", "CampsGram",
                        JOptionPane.ERROR_MESSAGE); //shows error
            } //end if
        } //end else if
        return (university + " - " + state); //return String University, State
    } //showEducationInputDialog

    public static void showCreatingDialog() {
        JOptionPane.showMessageDialog(null, "Creating User Profile...",
                "CampsGram", JOptionPane.INFORMATION_MESSAGE); //displays message
    } //showCreatingDialog
}
