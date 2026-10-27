package ui;

import model.User;
import service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel(MainFrame frame) {
        setBackground(Theme.BG);
        setLayout(new GridBagLayout());

        JTextField userField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);
        Theme.styleField(userField);
        Theme.styleField(passField);
        RoundedButton loginBtn = new RoundedButton("Login", Theme.PRIMARY, Color.WHITE);

        Theme.CardPanel card = new Theme.CardPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(4, 0, 4, 0);

        c.gridy = 0; card.add(Theme.label("Online Examination System", Theme.TITLE, Theme.PRIMARY), c);
        c.gridy = 1; c.insets = new Insets(0, 0, 18, 0);
        card.add(Theme.label("Sign in to start your exam", Theme.BODY, Theme.MUTED), c);
        c.insets = new Insets(4, 0, 4, 0);
        c.gridy = 2; card.add(Theme.label("Username", Theme.SMALL, Theme.MUTED), c);
        c.gridy = 3; card.add(userField, c);
        c.gridy = 4; card.add(Theme.label("Password", Theme.SMALL, Theme.MUTED), c);
        c.gridy = 5; card.add(passField, c);
        c.gridy = 6; c.insets = new Insets(18, 0, 0, 0); card.add(loginBtn, c);

        add(card);

        Runnable doLogin = () -> {
            String u = userField.getText().trim();
            String p = new String(passField.getPassword());
            User user = AuthService.login(u, p);
            if (user != null) {
                frame.setCurrentUser(user);
                frame.addScreen("PROFILE", new ProfilePanel(frame));
                frame.showScreen("PROFILE");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password",
                        "Login failed", JOptionPane.ERROR_MESSAGE);
            }
        };
        loginBtn.addActionListener(e -> doLogin.run());
        passField.addActionListener(e -> doLogin.run());
    }
}
