import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * ProfileServer
 *
 * This class consists of a main method that manages server connections. The main logic loops constantly to detect new
 * client connections. When a client connects, this class creates a new ProfileClientHandler object that is then used
 * to interface with the client.
 *
 * Geeksforgeeks was referenced to determine how to utilize threads for multiple client-server connections
 *
 * @author Team 60, Section 11
 * @version 5/03/2021
 *
 */

//Class uses port 1234
public class ProfileServer
{

    private static ArrayList<Account> accounts = new ArrayList<>(); //Account Master ArrayList

    /**
     * returns the Account Master Arraylist
     *
     * @return the Account Master Arraylist
     * */
    public static ArrayList<Account> getAccounts() {
        return accounts;
    }

    /**
     * Runs the main logic of the server, is this case running an initial server reboot from a backup file if it
     * exists, and then looping to look for clients trying to connect to the server
     * */
    public static void main(String[] args) throws IOException
    {

        //Create server socket on port 1234
        ServerSocket ss = new ServerSocket(1234); //server socket that the server uses to connect to clients

        ArrayList<String> serverBackup = new ArrayList<>(); //Arraylist of all the lines in the server backup
        ArrayList<Profile> profiles = new ArrayList<>(); //Arraylist of profiles associated with the account
        String[] splitLine; //Array containing the line, split by commas
        String line = "banana"; //The line from the .csv server backup file, initialized as a random word
        boolean backupEmpty = true; //indicates whether the backup file is empty or not if it exists

        try { //Server Reboot try/catch, using a backup file created when the server shuts down. The backup file
                //contains the email, password, and associated profile usernames for every account on the server

            //Read in file using correct filename using new bufferedReader
            File f = new File("ServerBackupFile.csv");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);

            //reads in the line from the file and adds it to an arrayList if it is not null
            while (!(line == null)) {

                line = bfr.readLine();

                if (!(line == null)) {
                    serverBackup.add(line);
                    backupEmpty = false;
                }
            }

            bfr.close();

            //loops through the server backup arrayList, containing all the lines of the server backup file
            for (int i = 0; i < serverBackup.size(); i++) {

                splitLine = serverBackup.get(i).split(","); //split each line (account information) by the commas

                for (int j = 2; j < splitLine.length; j++) { //loops through all of the profile usernames associated
                    // with an account

                    profiles.add(new Profile(splitLine[j] + "Export.csv")); //creates a new profile using the
                    // backup file created for each profile when the server shuts down or a client disconnects and adds
                    //it to a temporary arrayList

                }

                accounts.add(new Account(splitLine[0], splitLine[1], profiles)); //creates a new account using the
                // saved email, password, and newly created profile arrayList

                profiles = new ArrayList<Profile>(); //reset the profiles arrayList

            }

            System.out.println("Server reset from backup");

        } catch (Exception e) { //if IO Exception, likely due to the backup file not existing
            System.out.println("There was no valid server backup");
        }

        while (true) //loops indefinitely so that the server is always responsive to new connections
        {
            Socket s = null; //socket used by the client

            try
            {

                s = ss.accept();

                //confirm a client is connected, and outputs the socket information
                System.out.println("A new client is connected : " + s);

                //Create new object input and output stream to communicate with client
                ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream dis = new ObjectInputStream(s.getInputStream());

                //Create new thread to handle client
                Thread t = new ProfileClientHandler(s, dis, dos);
                t.start();

            }
            catch (Exception e){ //close socket if an exception occurs

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

                s.close();

            }
        }
    }
}