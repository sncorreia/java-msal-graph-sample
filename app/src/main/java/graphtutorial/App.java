/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package graphtutorial;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.microsoft.graph.models.extensions.User;
import java.io.Console;

/**
 * Graph Tutorial main method
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Java Graph Tutorial");
        System.out.println();

        // Load OAuth settings
        final String appId = "<your_client_id>";
        final String[] appScopes = new String[] { "https://graph.microsoft.com/.default" };
        final String appSecret = "<your_client_secret>";
        final String authority = "https://login.microsoftonline.com/<your_tenant_id>/";

        // Get an access token
        // Authentication.initialize(appId, authority);j
        Authentication.initialize(appId, authority, appSecret);
        final String accessToken = Authentication.getClientCredsAccessToken(appScopes);
        System.out.println("Access token = " + accessToken);

        String upn = promptForUPN();

        // Greet the user
        User user = Graph.getUser(accessToken, upn);
        if (user != null) {
            System.out.println("You have select user " + user.displayName);
        }

        Scanner input = new Scanner(System.in);

        int choice = -1;

        while (choice != 0) {
            System.out.println();
            System.out.println("Please choose one of the following options:");
            System.out.println("0. Exit");
            System.out.println("1. Display access token");
            System.out.println("2. Input upn to work with");
            System.out.println("3. Get this users info");
            System.out.println("4. Input new password to PATCH the user");

            try {
                choice = input.nextInt();
            } catch (InputMismatchException ex) {
                // Skip over non-integer input
                input.nextLine();
            }

            // Process user choice
            switch (choice) {
                case 0:
                    // Exit the program
                    System.out.println("Goodbye...");
                    break;
                case 1:
                    // Display access token
                    System.out.println("Access token: " + accessToken);
                    break;
                case 2:
                    upn = promptForUPN();

                    // Greet the user
                    user = Graph.getUser(accessToken, upn);
                    if (user != null) {
                        System.out.println("You have selected user " + user.displayName);
                    }
                    break;

                case 3:
                    if (user != null) {
                        System.out.println("User info:");
                        System.out.println("    id= " + user.id);
                        System.out.println("    mail= " + user.mail);
                    } else {
                        System.out.println("*** No user selected ***");
                    }
                    break;
                case 4:
                    String pwd = promptForNewPassword();
                    System.out.println(pwd);
                    Graph.changeUserPassword(accessToken, upn, pwd);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }

        input.close();
    }

    private static String promptForUPN() {
        String upn;
        Console console = System.console();
        upn = console.readLine("Enter user upn: ");
        return upn;
    }

    private static String promptForNewPassword() {
        String pwd;
        Console console = System.console();
        pwd = console.readLine("Enter new password for upn: ");
        return pwd;
    }
}