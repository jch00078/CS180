import java.util.ArrayList;
import java.util.Objects;

/**
 * AccountTestCases.java
 *
 * This class tests the methods in Account that are unrelated to GUI
 *
 * @author Team 060, Section 11
 * @version May 03, 2021
 * */
public class AccountTestCases {

    public static void main(String[] args) {
        //An account which will be used for testing
        Account testAccount = new Account("Will@purdue.edu", "12345678", new ArrayList<Profile>());

        //Successful Test cases
        try {
            testAccount.getEmail();
            testAccount.getPassword();
            testAccount.getProfiles();
            testAccount.setProfiles(new ArrayList<Profile>());
            testAccount.addProfile(new Profile());
            testAccount.deleteProfile("WillStonebridge");
        } catch (Exception e) {
            System.out.println("Test Cases Ran incorrectly");
            e.printStackTrace();
        }

        //Test cases that should fail due to incorrect input
        int failedCases = 0;

        testAccount.setProfiles(null);

        try {
            testAccount.addProfile(null);
        } catch (Exception e) {
            failedCases++;
        }

        try {
            testAccount.deleteProfile(null);
        } catch (Exception e) {
            failedCases++;
        }

        try {
            Objects.requireNonNull(testAccount.getProfiles());
        } catch (Exception e) {
            failedCases++;
        }

        testAccount = new Account(null, null, null);

        try {
            Objects.requireNonNull(testAccount.getEmail());
        } catch (Exception e) {
            failedCases++;
        }

        try {
            Objects.requireNonNull(testAccount.getPassword());
        } catch (Exception e) {
            failedCases++;
        }

        if (failedCases == 5) {
            System.out.println("All Test cases ran without issue");
        }

    }
}
