import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DatabaseHelper.init();
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}