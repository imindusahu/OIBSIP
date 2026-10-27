package ui;

import model.ExamResult;
import model.Question;
import service.QuestionBank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

public class ExamPanel extends JPanel {
    private static final int TOTAL_SECONDS = 5 * 60; // final submission mein 30 * 60

    private final MainFrame frame;
    private final List<Question> questions = QuestionBank.getQuestions();
    private final int[] answers = new int[questions.size()];
    private int current = 0;
    private int remainingSeconds = TOTAL_SECONDS;
    private final Timer timer;

    private final JLabel numberLabel = Theme.label("", Theme.HEADING, Theme.TEXT);
    private final JLabel timerLabel = new JLabel("", SwingConstants.CENTER);
    private final ProgressStrip progress = new ProgressStrip();
    private final JTextArea questionText = new JTextArea(3, 40);
    private final JPanel[] optionPanels = new JPanel[4];
    private final JRadioButton[] optionBtns = new JRadioButton[4];
    private final JTextArea[] optionTexts = new JTextArea[4];
    private final ButtonGroup group = new ButtonGroup();
    private final RoundedButton prevBtn = new RoundedButton("Previous", Theme.SECONDARY_BG, Theme.TEXT);
    private final RoundedButton nextBtn = new RoundedButton("Next", Theme.PRIMARY, Color.WHITE);
    private final RoundedButton submitBtn = new RoundedButton("Submit Exam", Theme.SUCCESS, Color.WHITE);

    public ExamPanel(MainFrame frame) {
        this.frame = frame;
        Arrays.fill(answers, -1);
        setBackground(Theme.BG);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // ---- top: question number, timer, progress ----
        timerLabel.setFont(Theme.BOLD);
        timerLabel.setOpaque(true);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        headerRow.add(numberLabel, BorderLayout.WEST);
        headerRow.add(timerLabel, BorderLayout.EAST);

        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);
        top.add(headerRow, BorderLayout.NORTH);
        top.add(progress, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        // ---- center: question card ----
        questionText.setLineWrap(true);
        questionText.setWrapStyleWord(true);
        questionText.setEditable(false);
        questionText.setOpaque(false);
        questionText.setFont(Theme.HEADING);
        questionText.setForeground(Theme.TEXT);
        questionText.setRows(3);

        JPanel options = new JPanel(new GridLayout(4, 1, 0, 10));
        options.setOpaque(false);

        for (int i = 0; i < 4; i++) {
            JRadioButton rb = new JRadioButton();
            rb.setOpaque(false);
            rb.setFocusPainted(false);
            group.add(rb);
            optionBtns[i] = rb;

            JTextArea txt = new JTextArea();
            txt.setLineWrap(true);
            txt.setWrapStyleWord(true);
            txt.setEditable(false);
            txt.setFocusable(false);
            txt.setOpaque(false);
            txt.setFont(Theme.BODY);
            txt.setForeground(Theme.TEXT);
            optionTexts[i] = txt;

            JPanel p = new JPanel(new BorderLayout(8, 0));
            p.add(rb, BorderLayout.WEST);
            p.add(txt, BorderLayout.CENTER);
            p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            optionPanels[i] = p;

            rb.addActionListener(e -> refreshOptionStyles());
            MouseAdapter click = new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { rb.doClick(); }
            };
            p.addMouseListener(click);
            txt.addMouseListener(click);
            options.add(p);
        }

        Theme.CardPanel card = new Theme.CardPanel(new BorderLayout(0, 16));
        card.add(questionText, BorderLayout.NORTH);
        card.add(options, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        // ---- bottom: navigation ----
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        right.add(nextBtn);
        right.add(submitBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(prevBtn, BorderLayout.WEST);
        bottom.add(right, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        prevBtn.addActionListener(e -> move(-1));
        nextBtn.addActionListener(e -> move(1));
        submitBtn.addActionListener(e -> onSubmitClicked());

        // ---- countdown timer ----
        updateTimerLabel();
        timer = new Timer(1000, e -> {
            remainingSeconds--;
            updateTimerLabel();
            if (remainingSeconds <= 0) {
                finishExam(true);
            }
        });
        timer.start();

        showQuestion();
    }

    private void updateTimerLabel() {
        int m = remainingSeconds / 60;
        int s = remainingSeconds % 60;
        timerLabel.setText(String.format("Time Left  %02d:%02d", m, s));
        boolean low = remainingSeconds <= 30;
        timerLabel.setBackground(low ? Theme.DANGER_BG : Theme.PRIMARY_TINT);
        timerLabel.setForeground(low ? Theme.DANGER : Theme.PRIMARY);
    }


    private void refreshOptionStyles() {
        for (int i = 0; i < 4; i++) {
            boolean sel = optionBtns[i].isSelected();
            JPanel p = optionPanels[i];
            p.setOpaque(true);
            p.setBackground(sel ? Theme.PRIMARY_TINT : Color.WHITE);
            int pad = sel ? 11 : 12; // border 2px vs 1px, size same rakhne ke liye
            p.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(sel ? Theme.PRIMARY : Theme.BORDER, sel ? 2 : 1),
                    BorderFactory.createEmptyBorder(pad, pad, pad, pad)));
        }
    }

    private void onSubmitClicked() {
        saveAnswer();
        int unanswered = 0;
        for (int a : answers) if (a == -1) unanswered++;

        String msg = unanswered > 0
                ? "You have " + unanswered + " unanswered question(s).\nSubmit anyway?"
                : "Are you sure you want to submit the exam?";
        int choice = JOptionPane.showConfirmDialog(this, msg, "Confirm Submit",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) finishExam(false);
    }

    private void finishExam(boolean autoSubmitted) {
        timer.stop();
        saveAnswer();
        int timeTaken = TOTAL_SECONDS - remainingSeconds;
        frame.setLastResult(new ExamResult(questions, answers, timeTaken));

        if (autoSubmitted) {
            JOptionPane.showMessageDialog(this, "Time is up! Your exam was submitted automatically.");
        }
        frame.addScreen("RESULT", new ResultPanel(frame));
        frame.showScreen("RESULT");
    }

    private void saveAnswer() {
        for (int i = 0; i < 4; i++) {
            if (optionBtns[i].isSelected()) answers[current] = i;
        }
    }

    private void move(int step) {
        saveAnswer();
        current += step;
        showQuestion();
    }

    private void showQuestion() {
        Question q = questions.get(current);
        numberLabel.setText("Question " + (current + 1) + " of " + questions.size());
        progress.setFraction((current + 1) / (double) questions.size());
        questionText.setText(q.getText());
        group.clearSelection();
        for (int i = 0; i < 4; i++) {
            optionTexts[i].setText(q.getOptions()[i]);
        }
        if (answers[current] >= 0) optionBtns[answers[current]].setSelected(true);
        refreshOptionStyles();

        prevBtn.setEnabled(current > 0);
        nextBtn.setEnabled(current < questions.size() - 1);
    }


    private static class ProgressStrip extends JComponent {
        private double fraction = 0;

        ProgressStrip() { setPreferredSize(new Dimension(10, 8)); }

        void setFraction(double f) { fraction = f; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Theme.BORDER);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.setColor(Theme.PRIMARY);
            g2.fillRoundRect(0, 0, (int) (getWidth() * fraction), getHeight(), 8, 8);
            g2.dispose();
        }
    }
}