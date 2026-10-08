import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {

    private static final Color INDIGO = new Color(79, 70, 229);
    private static final Color VIOLET = new Color(124, 58, 237);
    private static final Color TEXT_DARK = new Color(30, 41, 59);
    private static final Color TEXT_GRAY = new Color(107, 114, 128);

    private final RoundedField userField = new RoundedField("Enter your username");
    private final RoundedPassField passField = new RoundedPassField("Enter your password");
    private final JLabel errorLabel = new JLabel(" ");

    public LoginForm() {
        setTitle("RailConnect - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 480);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildLeftPanel(), BorderLayout.WEST);
        add(buildRightPanel(), BorderLayout.CENTER);
    }

    // ---------- LEFT: gradient + train drawing ----------
    private JPanel buildLeftPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // background gradient
                g2.setPaint(new GradientPaint(0, 0, new Color(67, 56, 202), w, h, new Color(6, 182, 212)));
                g2.fillRect(0, 0, w, h);

                // decorative circles
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillOval(-70, -70, 230, 230);
                g2.fillOval(w - 130, h - 170, 280, 280);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillOval(w - 110, 40, 120, 120);

                // rail line
                g2.setColor(new Color(255, 255, 255, 120));
                g2.fillRoundRect(40, 250, w - 80, 4, 4, 4);
                g2.setColor(new Color(255, 255, 255, 60));
                for (int x = 50; x < w - 50; x += 28) g2.fillRect(x, 256, 10, 4);

                // train body
                int tx = 70, ty = 140;
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(tx, ty, 220, 90, 40, 40);
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(tx + 150, ty - 18, 50, 24, 10, 10); // roof piece

                // windows
                g2.setColor(new Color(67, 56, 202));
                g2.fillRoundRect(tx + 18, ty + 18, 42, 32, 10, 10);
                g2.fillRoundRect(tx + 70, ty + 18, 42, 32, 10, 10);
                g2.fillRoundRect(tx + 122, ty + 18, 42, 32, 10, 10);
                g2.setColor(new Color(6, 182, 212));
                g2.fillRoundRect(tx + 174, ty + 18, 30, 32, 12, 12);

                // stripe
                g2.setColor(new Color(124, 58, 237));
                g2.fillRect(tx + 14, ty + 62, 190, 6);

                // headlight
                g2.setColor(new Color(253, 224, 71));
                g2.fillOval(tx + 205, ty + 68, 12, 12);

                // wheels
                g2.setColor(new Color(30, 27, 75));
                for (int i = 0; i < 4; i++) g2.fillOval(tx + 22 + i * 52, ty + 76, 26, 26);
                g2.setColor(Color.WHITE);
                for (int i = 0; i < 4; i++) g2.fillOval(tx + 30 + i * 52, ty + 84, 10, 10);

                // text
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 30));
                drawCentered(g2, "RailConnect", w, 330);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
                g2.setColor(new Color(255, 255, 255, 220));
                drawCentered(g2, "Book. Travel. Repeat.", w, 358);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.setColor(new Color(255, 255, 255, 160));
                drawCentered(g2, "Online Reservation System", w, h - 25);

                g2.dispose();
            }
        };
        p.setPreferredSize(new Dimension(360, 480));
        return p;
    }

    private static void drawCentered(Graphics2D g2, String text, int width, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, (width - fm.stringWidth(text)) / 2, y);
    }

    // ---------- RIGHT: login form ----------
    private JPanel buildRightPanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(40, 50, 30, 50));

        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Book your journey!");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(TEXT_GRAY);
        sub.setAlignmentX(LEFT_ALIGNMENT);

        JCheckBox showPass = new JCheckBox("Show password");
        showPass.setFont(new Font("SansSerif", Font.PLAIN, 12));
        showPass.setForeground(TEXT_GRAY);
        showPass.setBackground(Color.WHITE);
        showPass.setFocusPainted(false);
        showPass.setAlignmentX(LEFT_ALIGNMENT);
        final char defaultEcho = passField.getEchoChar();
        showPass.addActionListener(e ->
                passField.setEchoChar(showPass.isSelected() ? (char) 0 : defaultEcho));

        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        errorLabel.setForeground(new Color(220, 38, 38));
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);

        GradientButton loginBtn = new GradientButton("LOGIN");
        loginBtn.setAlignmentX(LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        loginBtn.setPreferredSize(new Dimension(100, 46));
        loginBtn.addActionListener(e -> doLogin());

        JLabel hint = new JLabel("Demo login: admin / admin123");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hint.setForeground(new Color(156, 163, 175));
        hint.setAlignmentX(LEFT_ALIGNMENT);

        p.add(title);
        p.add(Box.createVerticalStrut(4));
        p.add(sub);
        p.add(Box.createVerticalStrut(28));
        p.add(fieldLabel("USERNAME"));
        p.add(Box.createVerticalStrut(6));
        p.add(userField);
        p.add(Box.createVerticalStrut(16));
        p.add(fieldLabel("PASSWORD"));
        p.add(Box.createVerticalStrut(6));
        p.add(passField);
        p.add(Box.createVerticalStrut(6));
        p.add(showPass);
        p.add(Box.createVerticalStrut(8));
        p.add(errorLabel);
        p.add(Box.createVerticalStrut(10));
        p.add(loginBtn);
        p.add(Box.createVerticalStrut(14));
        p.add(hint);
        p.add(Box.createVerticalGlue());

        getRootPane().setDefaultButton(loginBtn);
        return p;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(TEXT_GRAY);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    // ---------- Login logic ----------
    private void doLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            return;
        }

        if (DatabaseHelper.validateLogin(user, pass)) {
            dispose();
            new ReservationForm(user).setVisible(true);
        } else {
            errorLabel.setText("Access Denied: Invalid username or password.");
            passField.setText("");
            shake();
        }
    }

    private void shake() {
        Point origin = getLocation();
        int[] offsets = {-10, 10, -8, 8, -5, 5, -2, 2, 0};
        int[] i = {0};
        Timer t = new Timer(35, null);
        t.addActionListener(e -> {
            if (i[0] >= offsets.length) {
                t.stop();
                setLocation(origin);
                return;
            }
            setLocation(origin.x + offsets[i[0]++], origin.y);
        });
        t.start();
    }

    // ---------- Custom components ----------
    private static void paintFieldBackground(JComponent c, Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(243, 244, 248));
        g2.fillRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 14, 14);
        if (c.hasFocus()) {
            g2.setColor(INDIGO);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, c.getWidth() - 3, c.getHeight() - 3, 14, 14);
        }
        g2.dispose();
    }

    private static void paintHint(JTextComponent c, Graphics g, String hint) {
        if (c.getDocument().getLength() > 0) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(new Color(156, 163, 175));
        g2.setFont(c.getFont());
        FontMetrics fm = g2.getFontMetrics();
        Insets in = c.getInsets();
        int y = (c.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(hint, in.left, y);
        g2.dispose();
    }

    private static void styleField(JTextComponent c) {
        c.setOpaque(false);
        c.setBorder(new EmptyBorder(8, 14, 8, 14));
        c.setFont(new Font("SansSerif", Font.PLAIN, 14));
        c.setForeground(TEXT_DARK);
        c.setCaretColor(INDIGO);
        c.setAlignmentX(LEFT_ALIGNMENT);
        c.setPreferredSize(new Dimension(100, 44));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        c.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { c.repaint(); }
            public void focusLost(FocusEvent e) { c.repaint(); }
        });
    }

    static class RoundedField extends JTextField {
        private final String hint;
        RoundedField(String hint) {
            this.hint = hint;
            styleField(this);
        }
        @Override
        protected void paintComponent(Graphics g) {
            paintFieldBackground(this, g);
            super.paintComponent(g);
            paintHint(this, g, hint);
        }
    }

    static class RoundedPassField extends JPasswordField {
        private final String hint;
        RoundedPassField(String hint) {
            this.hint = hint;
            styleField(this);
        }
        @Override
        protected void paintComponent(Graphics g) {
            paintFieldBackground(this, g);
            super.paintComponent(g);
            paintHint(this, g, hint);
        }
    }

    static class GradientButton extends JButton {
        private boolean hover = false;

        GradientButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Color c1 = hover ? INDIGO.darker() : INDIGO;
            Color c2 = hover ? VIOLET.darker() : VIOLET;
            g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), 0, c2));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

            g2.setColor(Color.WHITE);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }
}