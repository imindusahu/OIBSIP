import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ReservationForm extends JFrame {
    private final LoginForm.RoundedField nameField = new LoginForm.RoundedField("Full name as per ID");
    private final LoginForm.RoundedField trainNoField = new LoginForm.RoundedField("e.g. 12301");
    private final LoginForm.RoundedField trainNameField = new LoginForm.RoundedField("Auto-filled from train number");
    private final JComboBox<String> classBox = new JComboBox<>(
            new String[]{"Sleeper (SL)", "AC 3 Tier (3A)", "AC 2 Tier (2A)", "AC First (1A)", "Chair Car (CC)"});
    private final LoginForm.RoundedField dateField = new LoginForm.RoundedField("yyyy-MM-dd");
    private final LoginForm.RoundedField sourceField = new LoginForm.RoundedField("e.g. Lucknow");
    private final LoginForm.RoundedField destField = new LoginForm.RoundedField("e.g. Mumbai");
    private final JLabel errorLabel = Theme.errorLabel();

    public ReservationForm(String username) {
        setTitle("RailConnect - Ticket Reservation");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(690, 690);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        trainNameField.setEditable(false);
        trainNameField.setForeground(Theme.TEXT_GRAY);
        classBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        classBox.setPreferredSize(new Dimension(100, 44));
        classBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        add(new Theme.Header("Ticket Reservation", "Fill in the details to book your seat",
                "Logged in as " + username), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);

        trainNoField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { fillTrainName(); }
            public void removeUpdate(DocumentEvent e) { fillTrainName(); }
            public void changedUpdate(DocumentEvent e) { fillTrainName(); }
        });
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(10, 22, 14, 22));

        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.NORTH;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.insets = new Insets(0, 8, 16, 8);

        g.gridy = 0; g.gridx = 0; g.gridwidth = 2;
        form.add(cell("PASSENGER NAME", nameField), g);

        g.gridwidth = 1;
        g.gridy = 1; g.gridx = 0; form.add(cell("TRAIN NUMBER", trainNoField), g);
        g.gridx = 1; form.add(cell("TRAIN NAME", trainNameField), g);

        g.gridy = 2; g.gridx = 0; form.add(cell("CLASS TYPE", classBox), g);
        g.gridx = 1; form.add(cell("DATE OF JOURNEY", dateField), g);

        g.gridy = 3; g.gridx = 0; form.add(cell("SOURCE STATION", sourceField), g);
        g.gridx = 1; form.add(cell("DESTINATION STATION", destField), g);

        g.gridy = 4; g.gridx = 0; g.gridwidth = 2; g.insets = new Insets(0, 8, 8, 8);
        form.add(errorLabel, g);

        Theme.ColorButton bookBtn = new Theme.ColorButton("BOOK TICKET", Theme.INDIGO, Theme.VIOLET);
        Theme.OutlineButton cancelBtn = new Theme.OutlineButton("Cancel a Ticket");
        Theme.OutlineButton logoutBtn = new Theme.OutlineButton("Logout");
        JPanel btns = new JPanel(new GridLayout(1, 3, 12, 0));
        btns.setBackground(Color.WHITE);
        btns.add(bookBtn);
        btns.add(cancelBtn);
        btns.add(logoutBtn);
        g.gridy = 5; g.insets = new Insets(0, 8, 0, 8);
        form.add(btns, g);

        bookBtn.addActionListener(e -> book());
        cancelBtn.addActionListener(e -> new CancellationForm().setVisible(true));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
        return form;
    }

    private JPanel cell(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        JLabel l = Theme.fieldLabel(label);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Box.createVerticalStrut(6));
        p.add(field);
        return p;
    }

    private void fillTrainName() {
        String text = trainNoField.getText().trim();
        if (text.matches("\\d{1,9}")) {
            String name = DatabaseHelper.getTrainName(Integer.parseInt(text));
            trainNameField.setText(name != null ? name : "");
        } else {
            trainNameField.setText("");
        }
    }

    private void book() {
        String name = nameField.getText().trim();
        String trainNoText = trainNoField.getText().trim();
        String trainName = trainNameField.getText().trim();
        String dateText = dateField.getText().trim();
        String src = sourceField.getText().trim();
        String dest = destField.getText().trim();
        String classType = (String) classBox.getSelectedItem();

        if (name.isEmpty() || trainNoText.isEmpty() || dateText.isEmpty()
                || src.isEmpty() || dest.isEmpty()) {
            error("All required fields must be filled.");
            return;
        }
        if (!trainNoText.matches("\\d{1,9}")) {
            error("Train number must be numeric.");
            return;
        }
        if (trainName.isEmpty()) {
            error("Train not found. Try: 12301, 12951, 12002, 12260, 12009, 22416");
            return;
        }
        LocalDate date;
        try {
            date = LocalDate.parse(dateText);
        } catch (DateTimeParseException ex) {
            error("Invalid date format. Use yyyy-MM-dd (e.g. 2026-11-20).");
            return;
        }
        if (date.isBefore(LocalDate.now())) {
            error("Journey date cannot be in the past.");
            return;
        }
        if (src.equalsIgnoreCase(dest)) {
            error("Source and destination must be different.");
            return;
        }

        long pnr = DatabaseHelper.bookTicket(name, Integer.parseInt(trainNoText), trainName,
                classType, dateText, src, dest);
        if (pnr == -1) {
            error("Booking could not be saved. Please try again.");
            return;
        }

        errorLabel.setText(" ");
        Theme.showTicket(this, "Booking Confirmed", new String[][]{
                {"PNR Number", String.valueOf(pnr)},
                {"Passenger", name},
                {"Train", trainNoText + " - " + trainName},
                {"Class", classType},
                {"Date", dateText},
                {"From", src},
                {"To", dest}
        });
        clearForm();
    }

    private void clearForm() {
        nameField.setText("");
        trainNoField.setText("");
        trainNameField.setText("");
        classBox.setSelectedIndex(0);
        dateField.setText("");
        sourceField.setText("");
        destField.setText("");
    }

    private void error(String m) {
        errorLabel.setText(m);
    }
}