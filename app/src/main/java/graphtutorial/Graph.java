package graphtutorial;

import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.PasswordProfile;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.GraphServiceClient;

/**
 * Graph
 */
public class Graph {

    private static IGraphServiceClient graphClient = null;
    private static SimpleAuthProvider authProvider = null;

    private static void ensureGraphClient(String accessToken) {
        if (graphClient == null) {
            // Create the auth provider
            authProvider = new SimpleAuthProvider(accessToken);

            // Create default logger to only log errors
            DefaultLogger logger = new DefaultLogger();
            logger.setLoggingLevel(LoggerLevel.ERROR);

            // Build a Graph client
            graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).logger(logger)
                    .buildClient();
        }
    }

    public static User getUser(String accessToken, String upn) {
        ensureGraphClient(accessToken);

        try {
            User user = graphClient.users(upn).buildRequest().get();
            return user;
        } catch (Exception ex) {
            System.out.println("Error getting user " + ex.getMessage());
            return null;
        }
    }

    public static void changeUserPassword(String accessToken, String upn, String pwd) {
        ensureGraphClient(accessToken);

        User user = new User();
        PasswordProfile passwordProfile = new PasswordProfile();
        passwordProfile.forceChangePasswordNextSignIn = false;
        passwordProfile.forceChangePasswordNextSignInWithMfa = false;
        passwordProfile.password = pwd;
        user.passwordProfile = passwordProfile;

        try {
            graphClient.users("90060d20-5bd0-413d-a37e-cebacaaff3c4").buildRequest().patch(user);
            System.out.println("Congrats! You changed user's " + upn + " password to " + pwd);
            return;
        } catch (Exception ex) {
            System.out.println("Error getting user " + ex.getMessage());
            return;
        }
    }
}