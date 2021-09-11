import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * TestServer
 *
 * This class contains test cases used to ensure that the server is functioning properly. This class tests the
 * following classes: ProfileClient.java, ProfileClientHandler.java, and ProfileServer.java.
 *
 * @author Team 60, Section 11
 * @version 5/03/2021
 *
 */

public class TestServer {

    public static void main(String[] args) {

        /*
         * Success Case
         *
         * The following code is copied straight from ProfileClient. It connects to the server successfully.
         */

        boolean check = false; //used to determine whether a connection to the server has been achieved

        try { //connect to server and set up network io, display error message if connection unsuccessful
            while(!check) {
                Socket socket = new Socket("localhost", 1234);

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());

                check = true;

                System.out.println("Connection Successful! (Success case)");

            }

        } catch (IOException e) { //display error message is connection not established
            JOptionPane.showMessageDialog(null, "Error: Connection Cannot be Established " +
                            "(Success case)", "Campsgram", JOptionPane.ERROR_MESSAGE);
        }

        /*
         * Two Connection Case
         *
         * The following code is copied straight from ProfileClient. It connects to the server successfully, and tests
         * whether the server can successfully connect multiple clients.
         */

        check = false; //used to determine whether a connection to the server has been achieved

        try { //connect to server and set up network io, display error message if connection unsuccessful
            while(!check) {
                Socket socket = new Socket("localhost", 1234);

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());

                check = true;

                System.out.println("Connection Successful! (Two Connection Case)");

            }

        } catch (IOException e) { //display error message is connection not established
            JOptionPane.showMessageDialog(null, "Error: Connection Cannot be Established " +
                    "(Two Connection Case)", "Campsgram", JOptionPane.ERROR_MESSAGE);
        }

        /*
         * Failure Case
         *
         * The following code is copied straight from ProfileClient. It connects to the server unsuccessfully. Check has
         * been slightly altered, as it has already been defined in the previous test case.
         */

        check = false; //used to determine whether a connection to the server has been achieved

        try { //connect to server and set up network io, display error message if connection unsuccessful
            while(!check) {
                Socket socket = new Socket("localhost", 1235);

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());

                check = true;

                System.out.println("Connection Successful! (Failure case)");

            }

        } catch (IOException e) { //display error message is connection not established
            JOptionPane.showMessageDialog(null, "Error: Connection Cannot be Established " +
                            "(Failure case)", "Campsgram", JOptionPane.ERROR_MESSAGE);
        }

    }

}