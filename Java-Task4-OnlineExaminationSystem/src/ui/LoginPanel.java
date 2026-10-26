package ui;

import model.User;
import service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel(MainFrame frame) {
        setLayout(new GridBagLayout());
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.gridx = 0; c.gridy = 0; add(new JLabel("Username:"), c);
        c.gridx = 1; add(userField, c);
        c.gridx = 0; c.gridy = 1; add(new JLabel("Password:"), c);
        c.gridx = 1; add(passField, c);
        c.gridx = 1; c.gridy = 2; add(loginBtn, c);

        loginBtn.addActionListener(e -> {
            String u = userField.getText().trim();
            String p = new String (passField.getPassword());
            User user = AuthService.login(u, p);
            if(user != null) {
                frame.setCurrentUser(user);
                frame.addScreen("PROFILE", new ProfilePanel(frame));
                frame.showScreen("PROFILE");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
