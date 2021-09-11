import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ProfileTester {
    public static void main(String[] args) throws IOException {
        ArrayList<String> interests = new ArrayList<String>();
        interests.add("running");
        interests.add("gaming");
        interests.add("movies");
        ArrayList<String> friends = new ArrayList<String>();
        friends.add("Joe");
        friends.add("Eric");
        friends.add("Naia");
        //Profile jeff = new Profile("chen3801", interests, friends, "Purdue University",
          //      "chen3801@purdue.edu", 5167341551L, "hello, I am Jeff");
        //jeff.writeExportFile();
        System.out.println(friends.size());
    }
}
