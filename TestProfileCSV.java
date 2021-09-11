import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * TestProfileCSV
 *
 * This class contains tests for importing and exporting profiles as .csv files. The basic format of the .csv files is
 * as follows: username, length of the interests array list, strings in the interests array list, length of the
 * friends array list, strings in the friends array list, education, email, phone number, and about me section. There
 * are four test cases in this class; Proper Import, Improper Import, Proper/Improper Export, and
 * Import an Exported Profile. These cases examine the full functionality of the .csv methods Profile() and
 * writeExportFile(). Testing for an Proper/Improper Export case is combined due to the nature of the Improper Export,
 * as explained in the header for that test case. There is a continue dialogue after each test to ensure proper
 * logic flow.
 *
 * N/A
 *
 * @author Team 60, Section 11
 * @version 5/03/2021
 *
 */

public class TestProfileCSV {

    public static void main(String args[]) throws IOException {

        Scanner scanner = new Scanner(System.in);
        String block; //blocks logic from continuing to next test case until user enters input

        /*
         * Proper Import Case
         *
         * The following code tests to ensure that a profile can be properly imported from an existing .csv file, in
         * this case the file "testProfile.csv", which can be found on the gitHub repository. All fields will be output
         * to ensure accuracy in the creation of the profile.
         */

        //Logic block
        System.out.println("Enter any input to run Proper Import Case: ");
        block = scanner.nextLine();

        Profile profile = new Profile("testProfile.csv");

        String output = String.format("User Information:\n" +
                "Name: %s\n" +
                "Phone: %s\n" +
                "Email: %s\n" +
                "Education: %s\n" +
                "About: %s\n" +
                "Interests: %s\n", profile.getUsername(), profile.getPhoneNumber(), profile.getEmail(),
                profile.getEducation(), profile.getAboutMe(), profile.getInterests());
        System.out.println(output);

        /*
         * Improper Import Case
         *
         * The following code tests to ensure that a profile can be properly imported from an existing .csv file,
         * however the hardcoded filename is not an existing file. This represents the method being called with an
         * improper filename. The test verifies that the method can display the proper error GUIs and ask for a new
         * filename to ensure that the process is completed successfully. The file from the first test,
         * "testProfile.csv", can be used to continue past the error screen. All fields will be output to ensure
         * accuracy in the creation of the profile.
         */

        //Logic block
        System.out.println("Enter any input to run Improper Import Case: ");
        block = scanner.nextLine();

        Profile newProfile = new Profile("test.csv");

        output = String.format("User Information:\n" +
                        "Name: %s\n" +
                        "Phone: %s\n" +
                        "Email: %s\n" +
                        "Education: %s\n" +
                        "About: %s\n" +
                        "Interests: %s\n", newProfile.getUsername(), newProfile.getPhoneNumber(), newProfile.getEmail(),
                newProfile.getEducation(), newProfile.getAboutMe(), newProfile.getInterests());
        System.out.println(output);

        /*
         * Proper/Improper Export Case
         *
         * The following code tests to ensure that a profile can be properly exported to a new or existing .csv file.
         * The test case creates a new profile and populates the fields with various values, then calls the
         * writeExportFile function. To verify proper function, the newly created or overwritten .csv file can be
         * checked if the process was successful. Export failure only occurs in a few unique cases, most likely that the
         * file is open on the device and this cannot be edited. Thus, it cannot be tested directly. To test export,
         * simply have the file "chen3801Export.csv" open on the device.
         */

        //Logic block
        System.out.println("Enter any input to run Proper/Improper Export Case: ");
        block = scanner.nextLine();

         ArrayList<String> interests = new ArrayList<String>();
         interests.add("running");
         interests.add("gaming");
         interests.add("movies");
        interests.add("testing code");
         ArrayList<String> friends = new ArrayList<String>();
         friends.add("Joe");
         friends.add("Eric");
         friends.add("Naia");
        friends.add("Calvin");
         Profile jeff = new Profile("chen3801", interests, friends, "Purdue University",
                "chen3801@purdue.edu", 5167341551L, "hello, I am Jeff", new ArrayList<>(), new ArrayList<>());
         jeff.writeExportFile();

        /*
         * Import an Exported Profile Case
         *
         * The following code tests to ensure that a profile can be properly imported from an existing .csv file
         * created using the writeExportFile() methods. This verifies that the processes are compatible.  All fields
         * will be output to ensure accuracy in the creation of the profile.
         */

        //Logic block
        System.out.println("Enter any input to run Import an Exported Profile Case: ");
        block = scanner.nextLine();

        Profile newJeff = new Profile("chen3801Export.csv");

        output = String.format("User Information:\n" +
                        "Name: %s\n" +
                        "Phone: %s\n" +
                        "Email: %s\n" +
                        "Education: %s\n" +
                        "About: %s\n" +
                        "Interests: %s\n", newJeff.getUsername(), newJeff.getPhoneNumber(), newJeff.getEmail(),
                newJeff.getEducation(), newJeff.getAboutMe(), newJeff.getInterests());
        System.out.println(output);

    }
}