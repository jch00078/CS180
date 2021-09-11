import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * ProfileClient
 *
 * The .java file that the a user will run to connect to the server and access the social media functionality. It
 * contains a main method that automatically connects the user to the server, and then runs the GUI to interact.
 *
 * N/A
 *
 * @author Team 60, Section 11
 * @version 5/03/2021
 *
 */

public class ProfileClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        try { //runs client side stuff

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");  //"10.186.29.62"

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

            //Runs the Login GUI
            LoginPageGUI Login = new LoginPageGUI(ioMachine);
            Login.run();

        } catch (IOException e) { //display error message is connection not established
            JOptionPane.showMessageDialog(null, "Error: Connection Cannot be Established",
                    "Campsgram", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: There was an Error in the code " +
                    "somewhere", "Campsgram", JOptionPane.ERROR_MESSAGE);
        }

    }
}