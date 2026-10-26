package ui;

import model.ExamResult;
import model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel {
    public ResultPanel(MainFrame frame) {
        ExamResult r = frame.getLastResult();
        List<Question> qs = r.getQuestions();
        int[] ans = r.getAnswers();

        int correct = r.getScore();
        int unanswered = 0;
        for(int a : ans) if(a == -1) unanswered++;
        int incorrect = r.getTotal() - correct - unanswered;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // top: summary
        JPanel top = new JPanel(new GridLayout(0, 1, 3, 3));
        JLabel score = new JLabel("Score: " + correct + " out of " + r.getTotal());
        score.setFont(score.getFont().deriveFont(Font.BOLD, 22f));
        int m = r.getTimeTakenSeconds() / 60;
        int s = r.getTimeTakenSeconds() % 60;
        top.add(score);
        top.add(new JLabel(String.format("Time taken: %02d:%02d", m, s)));
        top.add(new JLabel("Correct: " + correct + " Incorrect: " + incorrect + " Unanswered: " + unanswered));
        add(top, BorderLayout.NORTH);

        // center: question-wise breakdown
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        for(int i = 0; i < qs.size(); i++) {
            Question q = qs.get(i);
            boolean isCorrect = ans[i] == q.getCorrectIndex();

            JTextArea area = new JTextArea();
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setEditable(false);
            area.setOpaque(false);

            StringBuilder sb = new StringBuilder();
            sb.append("Q").append(i + 1).append(". ").append(q.getText()).append("\n");
            sb.append("Your answer: ").append(ans[i] == -1 ? "Not answered" : q.getOptions()[ans[i]]).append("\n");

            if(isCorrect) {
                sb.append("Result: Correct");
            } else {
                sb.append("Correct answer: ").append(q.getOptions()[q.getCorrectIndex()]).append("\n");
                sb.append("Explanation: ").append(q.getExplanation()); // AI feature
            }
            area.setText(sb.toString());
            area.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(isCorrect ? new Color(0,140, 0) : Color.RED, 2),
                    BorderFactory.createEmptyBorder(6, 6, 6, 6)));

            list.add(area);
            list.add(Box.createVerticalStrut(8));
        }
        add(new JScrollPane(list), BorderLayout.CENTER);

        // bottom: logout
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            frame.setCurrentUser(null);
            frame.setLastResult(null);
            frame.addScreen("LOGIN", new LoginPanel(frame)); // fresh login screen
            frame.showScreen("LOGIN");
        });
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(logoutBtn);
        add(bottom, BorderLayout.SOUTH);
    }
}
