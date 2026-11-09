
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Users.initialize();

        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}
