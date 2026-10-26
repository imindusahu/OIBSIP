package ui;

import model.User;
import service.AuthService;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    public ProfilePanel(MainFrame frame) {
        User user = frame.getCurrentUser();
        setLayout(new GridBagLayout());

        JLabel title = new JLabel("Welcome, " + user.getDisplayName());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        JTextField nameField = new JTextField(user.getDisplayName(), 15);
        JPasswordField passField = new JPasswordField(15);
        JButton updateBtn = new JButton("Update Profile");
        JButton startBtn = new JButton("Start Exam");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.weighty = 0;

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2; add(title, c);
        c.gridwidth = 1;

        c.gridx = 0; c.gridy = 1; add(new JLabel("Display Name:"), c);
        c.gridx = 1; add(nameField, c);

        c.gridx = 0; c.gridy = 2; add(new JLabel("New Password:"), c);
        c.gridx = 1; add(passField, c);

        c.gridx = 1; c.gridy = 3; add(updateBtn, c);
        c.gridx = 1; c.gridy = 4; add(startBtn, c);

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
            passField.setText("");
            AuthService.save();
            JOptionPane.showMessageDialog(this, "Profile updated!");
        });

        startBtn.addActionListener(e -> {
            frame.addScreen("EXAM", new ExamPanel(frame));
            frame.showScreen("EXAM");
        });
    }
}
