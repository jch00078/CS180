import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * ProfileTestCases.java
 *
 * This class tests the methods in Profile that are unrelated to GUI
 *
 * @author Team 060, Section 11
 * @version May 03, 2021
 * */
public class ProfileTestCases {

    public static void main(String[] args) throws IOException {
        Profile testProfile = new Profile();

        //Every Test case below is an example of proper input
        try {
            testProfile.getUsername();
            testProfile.getInterests();
            testProfile.getFriends();
            testProfile.getEducation();
            testProfile.getEmail();
            testProfile.getPhoneNumber();
            testProfile.getAboutMe();
            testProfile.getRequestsSent();
            testProfile.getRequestsReceived();
            testProfile.setAboutMe("Something something");
            testProfile.setEducation("Yale");
            testProfile.setEmail("jbird@yale.edu");
            testProfile.setFriends(new ArrayList<String>());
            testProfile.addFriend("John");
            testProfile.removeFriends("Josh");
            testProfile.setInterests(new ArrayList<String>());
            testProfile.addInterest("Hockey");
            testProfile.removeInterest("Hockey");
            testProfile.setPhoneNumber(6663331111L);
            testProfile.setUsername("Jackson");
            testProfile.setRequestsSent(new ArrayList<String>());
            testProfile.addSentRequest("Charles");
            testProfile.removeSentRequest("Charles");
            testProfile.setRequestsReceived(new ArrayList<String>());
            testProfile.addReceivedRequest("Casper");
            testProfile.removeReceivedRequest("Casper");
            testProfile.writeExportFile();

        } catch (Exception e) {
            System.out.println("There was an error running the test cases");
            e.printStackTrace();
        }

        //Every test case below fails because it is given incorrect input
        Boolean failed;

        testProfile.setAboutMe(null);
        testProfile.setEducation(null);
        testProfile.setEmail(null);

        int casesFailed = 0;

        try {
            Objects.requireNonNull(testProfile.getAboutMe());
        } catch (Exception e) {
            casesFailed++;
        }

        try {
            Objects.requireNonNull(testProfile.getEducation());
        } catch (Exception e) {
            casesFailed++;
        }

        try {
            Objects.requireNonNull(testProfile.getEmail());
        } catch (Exception e) {
            casesFailed++;
        }

        testProfile.setFriends(null);

        try {
            testProfile.addFriend("John");
        } catch (Exception e) {
            casesFailed++;
        }

        try {
            testProfile.removeFriends("Josh");
        } catch (Exception e) {
            casesFailed++;
        }

        testProfile.setInterests(null);

        try {
            testProfile.removeInterest("Hockey");
        } catch (Exception e) {
            casesFailed++;
        }

        try {
            testProfile.addInterest("Hockey");
        } catch (Exception e) {
            casesFailed++;
        }

        testProfile.setUsername(null);

        try {
            Objects.requireNonNull(testProfile.getUsername());
        } catch (Exception e) {
            casesFailed++;
        }

        testProfile.setRequestsSent(null);

        try {
            testProfile.addSentRequest("Charles");
        } catch (Exception e) {
            casesFailed++;
        }

        try {
            testProfile.removeSentRequest("Charles");
        } catch (Exception e) {
            casesFailed++;
        }

        testProfile.setRequestsReceived(null);

        try {
            testProfile.addReceivedRequest("Casper");
        } catch (Exception e) {
            casesFailed++;
        }

        try {
            testProfile.removeReceivedRequest("Casper");
        } catch (Exception e) {
            casesFailed++;
        }

        if(casesFailed == 12) {
            System.out.println("All cases ran successfully");
        }
    }


}
