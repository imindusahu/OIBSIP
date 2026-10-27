package ui;

import javax.swing.*;
import java.awt.*;

public class Theme {
    public static final Color BG = new Color(0xF4F6FB);
    public static final Color CARD = Color.WHITE;
    public static final Color PRIMARY = new Color(0xE58B46);
    public static final Color PRIMARY_TINT = new Color(0xEEF2FF);
    public static final Color TEXT = new Color(0x1F2937);
    public static final Color MUTED = new Color(0x6B7280);
    public static final Color BORDER = new Color(0xE5E7EB);
    public static final Color SECONDARY_BG = new Color(0xE5E7EB);
    public static final Color SUCCESS = new Color(0x16A34A);
    public static final Color SUCCESS_BG = new Color(0xDCFCE7);
    public static final Color DANGER = new Color(0xDC2626);
    public static final Color DANGER_BG = new Color(0xFEE2E2);

    public static final Font TITLE = new Font("SansSerif", Font.BOLD, 24);
    public static final Font HEADING = new Font("SansSerif", Font.BOLD, 18);
    public static final Font BODY = new Font("SansSerif", Font.PLAIN, 15);
    public static final Font BOLD = new Font("SansSerif", Font.BOLD, 15);
    public static final Font SMALL = new Font("SansSerif", Font.PLAIN, 13);

    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static void styleField(JTextField f) {
        f.setFont(BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
    }

    public static class CardPanel extends JPanel {
        public CardPanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CARD);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.setColor(BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
