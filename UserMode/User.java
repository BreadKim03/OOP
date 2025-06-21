package UserMode;
import java.io.*;
import java.util.*;

public class User implements Serializable
{
    private static final long serialVersionUID = -6468968745769408863L;
    public String name;
    public String ID;
    private String Password;
    ArrayList <String> books = new ArrayList<String>();
    public static ArrayList<User> userList = new ArrayList<>();


    public User(String name, String ID, String Password)
    {
        this.name = name;
        this.ID = ID;
        this.Password = Password;
    }

    public String getName()
    {
        return this.name;
    }

    public String getID()
    {
        return this.ID;
    }

    public String getPassword()
    {
        return this.Password;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String newPassword) {
        this.Password = newPassword;
    }

    public void AddBook(String title)
    {
        books.add(title);
    }

    public void DeleteBook(String title)
    {
        for(int i = 0; i<books.size(); i++)
        {
            if(title.equals(books.get(i)))
            {
                books.remove(i);
            }
        }
    }

    public static boolean isDuplicateID(String ID) {
        for(User user : userList) {
            if(user.getID().equals(ID)) {
                return true;
            }
        }
        return false;
    }
}
