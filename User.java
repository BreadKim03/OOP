package UserMode;
import java.io.*;

public class User implements Serializable
{
	public String name;
	public String ID;
	private String Password;
	
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
}
