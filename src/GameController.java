import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.io.Console;

public class GameController
{

    // We set the lists public as they are constantly used throughout the program
    public static List playerList; // List containing player objects for each line in users.csv.
    public static List questionList; // List containing question objects for each line in quiz.csv
    private static final int minPasswordLength = 6;

    // We decided to use lists instead of arrays because they are easier to deal with: they do not need to be
    // initialized, which is especially useful when appending the playerList

    public static void main(String[] args)
    {
        Player aPlayer = null;
        PlayerRestrictions aPlayerRestriction = new PlayerRestrictions();
        // Stores both users and questions in lists at startup
        playerList = FileManager.getUsers();
        questionList = FileManager.getQuestions();

        String option = "";

        while (!option.equals("q"))
        {
            option = menu(); // We call the menu() method to take the user input

            switch (option)
            {
                case "l":
                    aPlayer = loginDetails(); // Stores the player if a match is found or null otherwise
                    aPlayerRestriction.login(aPlayer);
                    break;
                case "r":
                    aPlayer = registerDetails();
                    aPlayerRestriction.register(aPlayer);
                    break;
                case "p":
                    aPlayer = aPlayerRestriction.authGame(aPlayer);
                    break;
                case "a":
                    aboutText();
                    break;
                case "q":
                    // Exits the game
                    break;
                default:
                    System.out.println("Unknown option");
                    break;
            }
        }

        FileManager.printToFile(playerList);   // Prints the updated playerList to the file at shutdown
        System.out.println("Bye, bye");
    }

    private static String menu()
    {
        Scanner scan = new Scanner(System.in);

        String userInput = "";
        while (userInput.isEmpty()) // This covers the case where the user does not input anything
        {
            System.out.print("\n\\\\---------------------");
            System.out.print("\n\\\\ Welcome to The Game:");
            System.out.println("\n\\\\---------------------");

            System.out.println("\tLogin (L/l)");
            System.out.println("\tRegister (R/r)");
            System.out.println("\tPlay (P/p)");
            System.out.println("\tAbout (A/a)");
            System.out.println("\tQuit (Q/q)");

            System.out.print("\nPlease choose an option: ");
            userInput = scan.nextLine().toLowerCase();
        }

        userInput = userInput.toLowerCase().substring(0, 1); // .substring(0, 1) is the equivalent of charAt(0) for a String
        return (userInput);
    }

    //-----------------------------------------------------------------------
    //  Inputs the details of a player passed as a parameter for logging in.
    //-----------------------------------------------------------------------
    private static Player loginDetails()
    {
        Scanner scan = new Scanner(System.in);
        Console console = System.console();

        String username = "";
        while (username.isEmpty())
        {
            System.out.print("\n\tEnter the player\'s username: ");
            username = scan.nextLine();
        }

        String password = new String(console.readPassword("\n\tEnter the player\'s password (6 characters or more): "));

        while (password.isEmpty() || !passwordRestriction(password))
        {
            password = new String(console.readPassword("\n\tEnter the player\'s password (6 characters or more): "));
        }

        // This checks if the user exists or not by triggering only part of checkRegistered
        Player aPlayer = PlayerRestrictions.checkRegistered(username, password);

        if (aPlayer == null) // aPlayer is null if no match has been found
        {
            System.out.println("\nYou must register before you login.");
        }
        else
        {
            // Player managed to log in
        }
        return aPlayer;

    }

    //------------------------------------------------------------------------
    //  Inputs the details of a player passed as a parameter for registration.
    //------------------------------------------------------------------------
    private static Player registerDetails()
    {
        Scanner scan = new Scanner(System.in);
        Console console = System.console();

        String firstName = "";
        while (firstName.isEmpty() || firstName.contains(","))
        {
            System.out.print("\n\tEnter your first name: ");
            firstName = scan.nextLine();
        }

        String lastName = "";
        while (lastName.isEmpty() || lastName.contains(","))
        {
            System.out.print("\n\tEnter your last name: ");
            lastName = scan.nextLine();
        }

        String username = "";
        while (username.isEmpty())
        {
            System.out.print("\n\tEnter the player\'s login name: ");
            username = scan.nextLine();
        }

        String password = new String(console.readPassword("\n\tEnter the player\'s password (6 characters or more): "));

        while (password.isEmpty() || !passwordRestriction(password))
        {
            password = new String(console.readPassword("\n\tEnter the player\'s password (6 characters or more): "));
        }

        // This only passes the username as only this needs to be unique when registering
        // In turn, it triggers only part of checkRegistered
        Player aPlayer = PlayerRestrictions.checkRegistered(username, "");

        if (aPlayer == null) // aPlayer is null if a username match has been found
        {
            System.out.print("\n\tThis username is already taken.");
            return null;
        }
        else
        {
            return new Player(firstName, lastName, username, password, 0, 0);
        }

    }

    private static void aboutText()
    {
        Scanner scan = new Scanner(System.in);

        System.out.println("\n\tGAME INSTRUCTIONS:\n\n\tYou will be presented 10 questions, and for each of them you will" +
                "\n\tneed to choose the synonym of a word from a list displayed. " +
                "\n\tYou must select the synonym in the list in order to gain a point. " +
                "\n\tAt the end of the game, you will be able to see your score!");
        System.out.print("\nPress \"Enter\" when you are ready to play: ");
        System.out.println("\n\nPress Enter to continue:");

        try
        {
            System.in.read();
        }
        catch (Exception e)
        {

        }

    }

    private static boolean passwordRestriction(String password)
    {
        return password.length() >= minPasswordLength;
    }

}
