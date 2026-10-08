import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class Theme {
    static final Color INDIGO = new Color(79, 70, 229);
    static final Color VIOLET = new Color(124, 58, 237);
    static final Color CYAN = new Color(6, 182, 212);
    static final Color RED1 = new Color(239, 68, 68);
    static final Color RED2 = new Color(190, 18, 60);
    static final Color TEXT_DARK = new Color(30, 41, 59);
    static final Color TEXT_GRAY = new Color(107, 114, 128);
    static final Color SOFT_BG = new Color(243, 244, 248);

    static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(TEXT_GRAY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    static JLabel errorLabel() {
        JLabel l = new JLabel(" ");
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(new Color(220, 38, 38));
        return l;
    }

    // ---------- Gradient header ----------
    static class Header extends JPanel {
        Header(String title, String subtitle, String rightText) {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(18, 28, 18, 28));
            setPreferredSize(new Dimension(100, 95));

            JPanel left = new JPanel();
            left.setOpaque(false);
            left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
            JLabel t = new JLabel(title);
            t.setFont(new Font("SansSerif", Font.BOLD, 24));
            t.setForeground(Color.WHITE);
            JLabel s = new JLabel(subtitle);
            s.setFont(new Font("SansSerif", Font.PLAIN, 13));
            s.setForeground(new Color(255, 255, 255, 215));
            left.add(Box.createVerticalGlue());
            left.add(t);
            left.add(Box.createVerticalStrut(3));
            left.add(s);
            left.add(Box.createVerticalGlue());
            add(left, BorderLayout.CENTER);

            if (rightText != null) {
                JLabel r = new JLabel(rightText);
                r.setFont(new Font("SansSerif", Font.BOLD, 12));
                r.setForeground(Color.WHITE);
                add(r, BorderLayout.EAST);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, new Color(67, 56, 202), getWidth(), getHeight(), CYAN));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(255, 255, 255, 25));
            g2.fillOval(getWidth() - 140, -60, 200, 200);
            g2.fillOval(getWidth() - 260, 30, 120, 120);
            g2.dispose();
        }
    }

    // ---------- Rounded soft card ----------
    static class Card extends JPanel {
        Card() {
            setOpaque(false);
            setBorder(new EmptyBorder(16, 18, 16, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(SOFT_BG);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ---------- Buttons ----------
    static class ColorButton extends JButton {
        private final Color c1, c2;
        private boolean hover = false;

        ColorButton(String text, Color c1, Color c2) {
            super(text);
            this.c1 = c1;
            this.c2 = c2;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(100, 46));
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
            if (!isEnabled()) {
                g2.setColor(new Color(209, 213, 219));
            } else {
                Color a = hover ? c1.darker() : c1;
                Color b = hover ? c2.darker() : c2;
                g2.setPaint(new GradientPaint(0, 0, a, getWidth(), 0, b));
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            g2.setColor(Color.WHITE);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    static class OutlineButton extends JButton {
        private boolean hover = false;

        OutlineButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(100, 46));
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
            if (hover) {
                g2.setColor(new Color(238, 237, 253));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            }
            g2.setColor(INDIGO);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    // ---------- Ticket style popup ----------
    static void showTicket(Component parent, String heading, String[][] rows) {
        Window owner = SwingUtilities.getWindowAncestor(parent);
        JDialog d = new JDialog(owner, heading, Dialog.ModalityType.APPLICATION_MODAL);
        d.setLayout(new BorderLayout());

        d.add(new Header(heading, "Please save your PNR number", null), BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(rows.length, 2, 10, 12));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(22, 30, 14, 30));
        for (String[] r : rows) {
            JLabel k = new JLabel(r[0]);
            k.setFont(new Font("SansSerif", Font.BOLD, 12));
            k.setForeground(TEXT_GRAY);
            JLabel v = new JLabel(r[1]);
            boolean pnr = r[0].equalsIgnoreCase("PNR Number");
            v.setFont(new Font("SansSerif", Font.BOLD, pnr ? 18 : 13));
            v.setForeground(pnr ? INDIGO : TEXT_DARK);
            body.add(k);
            body.add(v);
        }
        d.add(body, BorderLayout.CENTER);

        ColorButton done = new ColorButton("DONE", INDIGO, VIOLET);
        done.addActionListener(e -> d.dispose());
        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(Color.WHITE);
        south.setBorder(new EmptyBorder(6, 30, 24, 30));
        south.add(done);
        d.add(south, BorderLayout.SOUTH);

        d.setSize(520, 120 + 95 + rows.length * 34);
        d.setResizable(false);
        d.setLocationRelativeTo(parent);
        d.setVisible(true);
    }
}