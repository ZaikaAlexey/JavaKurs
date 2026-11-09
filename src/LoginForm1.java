
import javax.swing.*;
import java.awt.*;

public class LoginForm1 extends JFrame {

    private JTextField loginField;
    private JPasswordField passwordField;
    private JTextField textField1;
    private JTextField textField2;

//    public LoginForm() {
//        setTitle("Авторизация");
//        setSize(380, 260);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setResizable(false);
//
//        JPanel panel = new JPanel(new GridBagLayout());
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(7, 5, 7, 5);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        JLabel title = new JLabel("Вход в систему", SwingConstants.CENTER);
//        title.setFont(new Font("Arial", Font.BOLD, 22));
//
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 2;
//        panel.add(title, gbc);
//
//        gbc.gridwidth = 1;
//
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        panel.add(new JLabel("Логин:"), gbc);
//
//        loginField = new JTextField(18);
//        gbc.gridx = 1;
//        panel.add(loginField, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        panel.add(new JLabel("Пароль:"), gbc);
//
//        passwordField = new JPasswordField(18);
//        gbc.gridx = 1;
//        panel.add(passwordField, gbc);
//
//        JButton loginButton = new JButton("Войти");
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.gridwidth = 2;
//        panel.add(loginButton, gbc);
//
//        loginButton.addActionListener(e -> authorize());
//
//        passwordField.addActionListener(e -> authorize());
//
//        setContentPane(panel);
//    }

//    private void authorize() {
//        String login = loginField.getText().trim();
//        String password = new String(passwordField.getPassword());
//
//        final boolean isAdmin;
//
//        if (login.equals("user") && password.equals("user123")) {
//            isAdmin = false;
//        } else if (login.equals("admin") && password.equals("admin123")) {
//            isAdmin = true;
//        } else {
//            JOptionPane.showMessageDialog(
//                    this,
//                    "Неверный логин или пароль!",
//                    "Ошибка авторизации",
//                    JOptionPane.ERROR_MESSAGE
//            );
//
//            passwordField.setText("");
//            return;
//        }
//
//        dispose();
//
//        SwingUtilities.invokeLater(() -> {
//            MainForm form = new MainForm(isAdmin);
//
//            JFrame frame = new JFrame("Практическое задание №13");
//
//            frame.setContentPane(form.getMainPanel());
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(850, 550);
//            frame.setMinimumSize(new Dimension(650, 450));
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }

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
