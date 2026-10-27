package ui;

import model.ExamResult;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel container = new JPanel(cards);
    private final Map<String, Component> screens = new HashMap<>();
    private ExamResult lastResult;
    private User currentUser;

    public  MainFrame() {
        setTitle("Online Examination System");
        setSize(820, 660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
                if(choice == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });

        add(container);
        addScreen("LOGIN", new LoginPanel(this));
        showScreen("LOGIN");
    }

    public void addScreen(String name, JPanel panel) {
        Component old = screens.get(name);
        if(old != null) container.remove(old);
        screens.put(name, panel);
        container.add(panel, name);
    }

    public void showScreen(String name) { cards.show(container, name); }

    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }

    public ExamResult getLastResult() { return lastResult; }
    public void setLastResult(ExamResult r) { this.lastResult = r; }
}
