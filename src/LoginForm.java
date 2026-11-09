
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JPanel LoginPanel;


    public LoginForm() {
        setTitle("Авторизация");
        setSize(380, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setContentPane(LoginPanel);   // панель из дизайнера

        loginButton.addActionListener(e -> authorize());
        passwordField.addActionListener(e -> authorize());
    }

    private void authorize() {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (!Users.authenticate(login, password)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Неверный логин или пароль!",
                    "Ошибка авторизации",
                    JOptionPane.ERROR_MESSAGE
            );
            passwordField.setText("");
            return;
        }

        boolean isAdmin = Users.isAdmin(login);

        dispose();

        SwingUtilities.invokeLater(() -> {
            MainForm form = new MainForm(isAdmin);
            JFrame frame = new JFrame("Практическое задание №13");
            frame.setContentPane(form.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(850, 550);
            frame.setMinimumSize(new Dimension(650, 450));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}
