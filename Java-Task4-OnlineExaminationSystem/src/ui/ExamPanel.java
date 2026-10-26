package ui;

import model.ExamResult;
import model.Question;
import service.QuestionBank;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ExamPanel extends JPanel {
    private static final int TOTAL_SECONDS = 60;

    private final MainFrame frame;
    private final List<Question> questions = QuestionBank.getQuestions();
    private final int[] answers = new int[questions.size()];
    private int current = 0;
    private int remainingSeconds = TOTAL_SECONDS;
    private final Timer timer;

    private final JLabel numberLabel = new JLabel();
    private final JLabel timerLabel = new JLabel();
    private final JTextArea questionText = new JTextArea(3, 40);
    private final JRadioButton[] optionBtns = new JRadioButton[4];
    private final ButtonGroup group = new ButtonGroup();
    private final JButton prevBtn = new JButton("Previous");
    private final JButton nextBtn = new JButton("Next");
    private final JButton submitBtn = new JButton("Submit Exam");

    public ExamPanel(MainFrame frame) {
        this.frame = frame;
        Arrays.fill(answers, -1);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new BorderLayout());
        timerLabel.setFont(timerLabel.getFont().deriveFont(Font.BOLD, 18f));
        top.add(numberLabel, BorderLayout.WEST);
        top.add(timerLabel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // center
        JPanel center = new JPanel(new GridLayout(5, 1, 5, 5));
        questionText.setLineWrap(true);
        questionText.setWrapStyleWord(true);
        questionText.setEditable(false);
        questionText.setOpaque(false);
        questionText.setFont(questionText.getFont().deriveFont(16f));
        center.add(questionText);
        for (int i = 0; i < 4; i++) {
            optionBtns[i] = new JRadioButton();
            group.add(optionBtns[i]);
            center.add(optionBtns[i]);
        }
        add(center, BorderLayout.CENTER);

        // bottom: navigation
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        bottom.add(prevBtn);
        bottom.add(nextBtn);
        bottom.add(submitBtn);
        add(bottom, BorderLayout.SOUTH);

        prevBtn.addActionListener(e -> move(-1));
        nextBtn.addActionListener(e -> move(1));
        submitBtn.addActionListener(e -> onSubmitClicked());


        // countdown timer
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
        timerLabel.setText(String.format("Time Left: %02d:%02d", m, s));
        // last 10 second mein red
        timerLabel.setForeground(remainingSeconds <= 10 ? Color.RED : Color.BLACK);
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

    // manual aur auto dono yahin aate hain
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
        questionText.setText(q.getText());
        group.clearSelection();
        for (int i = 0; i < 4; i++) {
            optionBtns[i].setText(q.getOptions()[i]);
        }
        if (answers[current] >= 0) optionBtns[answers[current]].setSelected(true);

        prevBtn.setEnabled(current > 0);
        nextBtn.setEnabled(current < questions.size() - 1);
    }
}
