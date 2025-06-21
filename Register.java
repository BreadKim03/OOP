package UserMode.PrivateInfo;
import java.util.*;
import java.io.*;
import UserMode.User;

public class Register {
    Scanner sc = new Scanner(System.in);

    public void register() {
    }

    public static void SaveFile(User user) {
        File dir = new File("UserMode.User");
        if(!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, user.ID+ ".dat");
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            oos.writeObject(user);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
