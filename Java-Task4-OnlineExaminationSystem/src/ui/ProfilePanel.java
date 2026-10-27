package ui;

import model.User;
import service.AuthService;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    public ProfilePanel(MainFrame frame) {
        User user = frame.getCurrentUser();
        setBackground(Theme.BG);
        setLayout(new GridBagLayout());

        JTextField nameField = new JTextField(user.getDisplayName(), 20);
        JPasswordField passField = new JPasswordField(20);
        Theme.styleField(nameField);
        Theme.styleField(passField);
        RoundedButton updateBtn = new RoundedButton("Update Profile", Theme.SECONDARY_BG, Theme.TEXT);
        RoundedButton startBtn = new RoundedButton("Normal Exam", Theme.PRIMARY, Color.WHITE);
        RoundedButton adaptiveBtn = new RoundedButton("Adaptive Exam (AI)", Theme.SUCCESS, Color.WHITE);

        Theme.CardPanel card = new Theme.CardPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(4, 0, 4, 0);

        c.gridy = 0; card.add(Theme.label("Welcome, " + user.getDisplayName(), Theme.TITLE, Theme.PRIMARY), c);
        c.gridy = 1; c.insets = new Insets(0, 0, 18, 0);
        card.add(Theme.label("Update your profile before starting the exam", Theme.BODY, Theme.MUTED), c);
        c.insets = new Insets(4, 0, 4, 0);
        c.gridy = 2; card.add(Theme.label("Display Name", Theme.SMALL, Theme.MUTED), c);
        c.gridy = 3; card.add(nameField, c);
        c.gridy = 4; card.add(Theme.label("New Password (leave empty to keep old)", Theme.SMALL, Theme.MUTED), c);
        c.gridy = 5; card.add(passField, c);

        JPanel buttons = new JPanel(new GridLayout(1, 3, 10, 0));
        buttons.setOpaque(false);
        buttons.add(updateBtn);
        buttons.add(startBtn);
        buttons.add(adaptiveBtn);
        c.gridy = 6; c.insets = new Insets(18, 0, 0, 0); card.add(buttons, c);

        add(card);

        updateBtn.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newPass = new String(passField.getPassword());
            if(newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty");
                return;
            }
            user.setDisplayName(newName);
            if(!newPass.isEmpty()) {
                if(newPass.length() < 4) {
                    JOptionPane.showMessageDialog(this, "Password must be at least 4 characters");
                    return;
                }
                user.setPassword(newPass);
            }
            AuthService.save();
            passField.setText("");
            JOptionPane.showMessageDialog(this, "Profile updated!");
        });

        startBtn.addActionListener(e -> {
            frame.addScreen("EXAM", new ExamPanel(frame, false));
            frame.showScreen("EXAM");
        });

        adaptiveBtn.addActionListener(e -> {
            frame.addScreen("Exam", new ExamPanel(frame, true));
            frame.showScreen("Exam");
        });
    }
}
