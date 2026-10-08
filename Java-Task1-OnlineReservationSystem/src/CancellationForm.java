import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CancellationForm extends JFrame {
    private final LoginForm.RoundedField pnrField = new LoginForm.RoundedField("Enter 6-digit PNR");
    private final JLabel messageLabel = new JLabel(" ");
    private final Theme.Card detailsCard = new Theme.Card();
    private final Theme.ColorButton cancelBtn =
            new Theme.ColorButton("CONFIRM CANCELLATION", Theme.RED1, Theme.RED2);
    private long fetchedPnr = -1;

    public CancellationForm() {
        setTitle("RailConnect - Cancel Ticket");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(620, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(new Theme.Header("Cancel Ticket", "Find your booking using the PNR number", null),
                BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(24, 30, 24, 30));

        // top: PNR + Fetch
        JPanel top = new JPanel(new BorderLayout(12, 0));
        top.setBackground(Color.WHITE);
        JPanel pnrBox = new JPanel();
        pnrBox.setLayout(new BoxLayout(pnrBox, BoxLayout.Y_AXIS));
        pnrBox.setBackground(Color.WHITE);
        pnrBox.add(Theme.fieldLabel("PNR NUMBER"));
        pnrBox.add(Box.createVerticalStrut(6));
        pnrField.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnrBox.add(pnrField);
        Theme.ColorButton fetchBtn = new Theme.ColorButton("FETCH", Theme.INDIGO, Theme.VIOLET);
        fetchBtn.setPreferredSize(new Dimension(110, 44));
        JPanel fetchWrap = new JPanel(new BorderLayout());
        fetchWrap.setBackground(Color.WHITE);
        fetchWrap.setBorder(new EmptyBorder(23, 0, 0, 0));
        fetchWrap.add(fetchBtn);
        top.add(pnrBox, BorderLayout.CENTER);
        top.add(fetchWrap, BorderLayout.EAST);

        // middle: details card
        detailsCard.setLayout(new GridLayout(0, 2, 10, 14));
        showPlaceholder();

        // bottom: message + buttons
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(Color.WHITE);
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottom.add(messageLabel);
        bottom.add(Box.createVerticalStrut(10));
        JPanel btns = new JPanel(new GridLayout(1, 2, 12, 0));
        btns.setBackground(Color.WHITE);
        btns.setAlignmentX(Component.LEFT_ALIGNMENT);
        cancelBtn.setEnabled(false);
        Theme.OutlineButton closeBtn = new Theme.OutlineButton("Close");
        btns.add(cancelBtn);
        btns.add(closeBtn);
        bottom.add(btns);

        body.add(top, BorderLayout.NORTH);
        body.add(detailsCard, BorderLayout.CENTER);
        body.add(bottom, BorderLayout.SOUTH);
        add(body, BorderLayout.CENTER);

        fetchBtn.addActionListener(e -> fetch());
        cancelBtn.addActionListener(e -> cancel());
        closeBtn.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(fetchBtn);
    }

    private void showPlaceholder() {
        detailsCard.removeAll();
        detailsCard.setLayout(new BorderLayout());
        JLabel l = new JLabel("Enter a PNR and press Fetch to see booking details.",
                SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(Theme.TEXT_GRAY);
        detailsCard.add(l);
        detailsCard.revalidate();
        detailsCard.repaint();
    }

    private void showDetails(String[] b) {
        detailsCard.removeAll();
        detailsCard.setLayout(new GridLayout(0, 2, 10, 14));
        String[][] rows = {
                {"PNR Number", b[0]},
                {"Passenger", b[1]},
                {"Train", b[2] + " - " + b[3]},
                {"Class", b[4]},
                {"Date", b[5]},
                {"From", b[6]},
                {"To", b[7]}
        };
        for (String[] r : rows) {
            JLabel k = new JLabel(r[0]);
            k.setFont(new Font("SansSerif", Font.BOLD, 12));
            k.setForeground(Theme.TEXT_GRAY);
            JLabel v = new JLabel(r[1]);
            v.setFont(new Font("SansSerif", Font.BOLD, 13));
            v.setForeground(r[0].equals("PNR Number") ? Theme.INDIGO : Theme.TEXT_DARK);
            detailsCard.add(k);
            detailsCard.add(v);
        }
        detailsCard.revalidate();
        detailsCard.repaint();
    }

    private void message(String text, boolean ok) {
        messageLabel.setForeground(ok ? new Color(22, 163, 74) : new Color(220, 38, 38));
        messageLabel.setText(text);
    }

    private void fetch() {
        String text = pnrField.getText().trim();
        cancelBtn.setEnabled(false);
        fetchedPnr = -1;
        showPlaceholder();

        if (text.isEmpty()) {
            message("Please enter a PNR number.", false);
            return;
        }
        if (!text.matches("\\d+")) {
            message("PNR must be numeric.", false);
            return;
        }

        long pnr;
        try {
            pnr = Long.parseLong(text);
        } catch (NumberFormatException ex) {
            message("PNR number is too long.", false);
            return;
        }

        String[] b = DatabaseHelper.getBooking(pnr);
        if (b == null) {
            message("No booking found for this PNR.", false);
            return;
        }

        showDetails(b);
        fetchedPnr = pnr;
        cancelBtn.setEnabled(true);
        message(" ", true);
    }

    private void cancel() {
        if (fetchedPnr == -1) return;

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel PNR " + fetchedPnr + "?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            long pnr = fetchedPnr;
            if (DatabaseHelper.cancelBooking(pnr)) {
                showPlaceholder();
                pnrField.setText("");
                cancelBtn.setEnabled(false);
                fetchedPnr = -1;
                message("Booking (PNR " + pnr + ") has been cancelled successfully.", true);
            } else {
                message("Cancellation failed. Please try again.", false);
            }
        }
    }
}