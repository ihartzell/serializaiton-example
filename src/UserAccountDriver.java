import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
// This is the driver class. It's the class where the program begins.
public class UserAccountDriver 
{
	// This is the main method where the program is essentially executed.
	public static void main(String[] args) 
	{
		Scanner input = new Scanner(System.in);
		
		// Initializied variables so I could use them in my method
		// calls such as getUser(username).
		String username = null;
		String password = null;
		String passwordHint = null;
		
		int menuChoice = 0;
		
		// I created a facebook object from my Facebook class
		// and I assign it to the deSerialization method so I don't lose the data.
		// If I don't assign the deSerialization method won't be assigned to anything and essentially does nothing.
		Facebook facebookObj = deSerialization();
		
		
		// I basically repeat my menu as long as the user doesn't choose option
		// 5 to exit the program, and the user picks an option not valid.
		do
		{	
			// I assign my menu method to the variable menuChoice so I can make a switch statement
			// for my menu.
			menuChoice = menu();
			
			switch(menuChoice){
			
			// Lists all friends of some user.
			// I assign an object named user from the FacebookUser class to my
			// facebook object and call for the getUser method.
			case 1: username = requestString("Please enter your username.");
					FacebookUser user = facebookObj.getUser(username);
					
					if (user == null)
					{
						System.out.println("No user with username: " + username);
						System.out.println();
						break;
					}
					System.out.println("Friends of" + username);
					
					// for each friend I use the user object to call for my getFriends method
					// where I print out all the friends from the friends array list.
					for (FacebookUser friend: user.getFriends())
					{
						System.out.println(friend.toString());
					}
					System.out.println();
					
				break;
				
			// Adds a user.	
			case 2: System.out.println("Creation of new user:");
					System.out.print("----------------------");
					System.out.println();
					username = requestString("Username:");
					password = requestString("Password:");
					passwordHint = requestString("What do you want the hint for your password to be?");
					System.out.println("Added: " + username);
					facebookObj.addUser(username, password, passwordHint);
					System.out.println();
					
				break;
				
			// Removes a user.	
			case 3: System.out.println("Deletion of user:");
					System.out.println("----------------");
					username = requestString("Remove user:");
					facebookObj.deleteUser(username);
					System.out.println();
					
				break;
				
			// Gives the password help.	
			case 4:	System.out.println("*Password Help*");
					username = requestString("What is the username?");
					FacebookUser userObj = facebookObj.getUser(username);
					
					if (userObj == null)
					{
						System.out.println("No user with username: " + username);
						System.out.println();
						break;
					}
					else
					{
						userObj.getPasswordHelp();
						System.out.println();
					}
					
				break;
				
			// Exits the program.
			// Apon termination I serialize the facebook object.
			case 5: serialization(facebookObj);
					System.out.println("Logging off...");
				break;
			}
		// end of the do-while loop and really what runs the program.	
		}while(menuChoice >= 1 && menuChoice <= 4 );
	}
	// This method makes String input much easier and cleaner throughout the program.
	// I pass to the method a String variable and I then print that out.
	// I then return whatever String input back to the method.
	public static String requestString(String request)
	{
		Scanner input = new Scanner(System.in);
		System.out.print(request);
		
		return input.nextLine();
	}
	// This method purely displays the menu as well as checks to see
	// if the user's input is < 1 or > 5 and if it is the menu will repeat
	// untill the user selects a valid menu option.
	public static int menu()
	{
		int menuChoice = 0;
		Scanner input = new Scanner(System.in);
		
		System.out.println("Menu:");
		System.out.println("-------------");
		System.out.println("1.List User(s):");
		System.out.println("2.Add User:");
		System.out.println("3.Delete User:");
		System.out.println("4.Password Hint:");
		System.out.println("5.Quit:");
		System.out.println();
		System.out.print("Choose 1-5.");
		menuChoice = input.nextInt();
		while (menuChoice < 1 || menuChoice > 5)
		{
			System.out.println("Menu:");
			System.out.println(menuChoice + " is an invalid option, please choose 1-5.");
			System.out.println("-------------");
			System.out.println("1.List User(s):");
			System.out.println("2.Add User:");
			System.out.println("3.Delete User:");
			System.out.println("4.Password Hint:");
			System.out.println("5.Quit:");
			System.out.println();
			System.out.print("Choose 1-5.");
			menuChoice = input.nextInt();
		}
		return menuChoice;
	}
	// This method serializes the Facebook object. In other words it writes the object to the file.
	public static void serialization(Facebook facebookObj)
	{
		try 
		{
			// I'm creating a file output object which has the file name assigned to it.
			FileOutputStream fbDataOutput = new FileOutputStream("fbDataFile.txt");
			
			// Here I'm creating an output stream object which is assigned to the file output object.
			ObjectOutputStream objOutStream = new ObjectOutputStream(fbDataOutput);
			
			// I am then writing the facebookObj to the file.
			objOutStream.writeObject(facebookObj.getUsers());
			
			//closing file object and output stream object.
			objOutStream.close();
			fbDataOutput.close();
			
			System.out.printf(" Data saved successfully, and is now viewable in your file which can only be read by the computer. ");
		} 
		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
		
	}
	@SuppressWarnings("unchecked")
	public static Facebook deSerialization()
	{
		// Creating a facebook object.
		Facebook facebookObj = new Facebook();
		try 
		{
			// I'm making a file input object so I can now read in the program the file.
			FileInputStream fbDataInput = new FileInputStream("fbDataFile.txt");
			
			// I'm making an input stream object so I can read in the facebook object.
			ObjectInputStream objInStream = new ObjectInputStream(fbDataInput);
			
			// This line is involved but I'm basically using the facebook object to call for the setUsers method where I essentially
			// cast the object input stream and call for the readObject method so I can read in the object.
			facebookObj.setUsers((ArrayList<FacebookUser>) objInStream.readObject());
			
            // I'm closing the file input object and the object input stream.
			objInStream.close();
			fbDataInput.close();
			
			return facebookObj;
		} 
		catch (IOException ex) 
		{
			System.out.println("Starting app with no data!");
			return facebookObj;
		}
		catch (ClassNotFoundException ex)
		{
			System.out.println("Failed to serialize! possibility of data corruption.");
			System.out.println("Starting app with no data!");
			return facebookObj;
		}
	} 	// End of Main method.
} 		// End of driver class.






