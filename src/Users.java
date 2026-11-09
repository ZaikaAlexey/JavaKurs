import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Users {

    private static final Path FILE = Path.of("users.properties");
    private static final Properties PROPS = new Properties();

    public static void initialize() {
        try {
            if (!Files.exists(FILE)) {
                PROPS.setProperty("user.password", "user123");
                PROPS.setProperty("user.admin", "false");
                PROPS.setProperty("admin.password", "admin123");
                PROPS.setProperty("admin.admin", "true");
                PROPS.store(Files.newOutputStream(FILE), "Users");
                return;
            }
            try (InputStream in = Files.newInputStream(FILE)) {
                PROPS.load(in);
            }
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить users.properties", e);
        }
    }

    public static boolean authenticate(String login, String password) {
        String saved = PROPS.getProperty(login + ".password");
        return saved != null && saved.equals(password);
    }

    public static boolean isAdmin(String login) {
        return Boolean.parseBoolean(PROPS.getProperty(login + ".admin", "false"));
    }
}