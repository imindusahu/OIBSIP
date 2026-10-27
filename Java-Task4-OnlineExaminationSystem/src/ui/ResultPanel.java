package ui;

import model.ExamResult;
import model.Question;
import service.StudyAdvisor;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ResultPanel extends JPanel {
    public ResultPanel(MainFrame frame) {
        ExamResult r = frame.getLastResult();
        List<Question> qs = r.getQuestions();
        int[] ans = r.getAnswers();

        int correct = r.getScore();
        int unanswered = 0;
        for (int a : ans) if (a == -1) unanswered++;
        int incorrect = r.getTotal() - correct - unanswered;
        long percent = Math.round(100.0 * correct / r.getTotal());
        int m = r.getTimeTakenSeconds() / 60;
        int s = r.getTimeTakenSeconds() % 60;

        setBackground(Theme.BG);
        setLayout(new BorderLayout(0, 14));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // ---- top: summary card ----
        Theme.CardPanel summary = new Theme.CardPanel(new BorderLayout(20, 0));

        JPanel left = new JPanel(new GridLayout(0, 1, 0, 2));
        left.setOpaque(false);
        left.add(Theme.label("Your Result", Theme.SMALL, Theme.MUTED));
        left.add(Theme.label(correct + " / " + r.getTotal(),
                new Font("SansSerif", Font.BOLD, 40), Theme.PRIMARY));
        left.add(Theme.label(percent + "%   |   Time taken " + String.format("%02d:%02d", m, s),
                Theme.BODY, Theme.MUTED));

        JPanel stats = new JPanel(new GridLayout(1, 3, 10, 0));
        stats.setOpaque(false);
        stats.add(chip("Correct", correct, Theme.SUCCESS, Theme.SUCCESS_BG));
        stats.add(chip("Incorrect", incorrect, Theme.DANGER, Theme.DANGER_BG));
        stats.add(chip("Unanswered", unanswered, Theme.MUTED, Theme.SECONDARY_BG));

        summary.add(left, BorderLayout.WEST);
        summary.add(stats, BorderLayout.EAST);

        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);
        top.add(summary, BorderLayout.NORTH);
        top.add(Theme.label("Detailed Review", Theme.HEADING, Theme.TEXT), BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // ---- center: question-wise review ----
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.add(buildAnalysis(r));
        list.add(Box.createVerticalStrut(14));

        for (int i = 0; i < qs.size(); i++) {
            Question q = qs.get(i);
            boolean isCorrect = ans[i] == q.getCorrectIndex();
            Color accent = isCorrect ? Theme.SUCCESS : Theme.DANGER;
            Color tint = isCorrect ? Theme.SUCCESS_BG : Theme.DANGER_BG;

            StringBuilder sb = new StringBuilder();
            sb.append("Q").append(i + 1).append(". ").append(q.getText()).append("\n");
            sb.append("Your answer: ")
                    .append(ans[i] == -1 ? "Not answered" : q.getOptions()[ans[i]]).append("\n");
            if (isCorrect) {
                sb.append("Result: Correct");
            } else {
                sb.append("Correct answer: ").append(q.getOptions()[q.getCorrectIndex()]).append("\n");
                sb.append("Explanation: ").append(q.getExplanation());
            }

            JTextArea area = new JTextArea(sb.toString());
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setEditable(false);
            area.setOpaque(false);
            area.setFont(Theme.BODY);
            area.setForeground(Theme.TEXT);

            JPanel item = new JPanel(new BorderLayout());
            item.setBackground(tint);
            item.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 6, 0, 0, accent),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)));
            item.add(area, BorderLayout.CENTER);

            list.add(item);
            list.add(Box.createVerticalStrut(10));
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(list, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // ---- bottom: logout ----
        RoundedButton logoutBtn = new RoundedButton("Logout", Theme.PRIMARY, Color.WHITE);
        logoutBtn.addActionListener(e -> {
            frame.setCurrentUser(null);
            frame.setLastResult(null);
            frame.addScreen("LOGIN", new LoginPanel(frame));
            frame.showScreen("LOGIN");
        });
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.add(logoutBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel buildAnalysis(ExamResult r) {
        Theme.CardPanel card = new Theme.CardPanel(new BorderLayout());
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        JLabel head = Theme.label("Topic Analysis", Theme.HEADING, Theme.TEXT);
        head.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(head);
        body.add(Box.createVerticalStrut(8));

        for (Map.Entry<String, int[]> e : r.getTopicStats().entrySet()) {
            int c = e.getValue()[0];
            int t = e.getValue()[1];
            long pct = Math.round(100.0 * c / t);
            String status;
            Color col;
            if (pct < 50)      { status = "Weak";   col = Theme.DANGER; }
            else if (pct < 80) { status = "Okay";   col = new Color(0xD97706); }
            else               { status = "Strong"; col = Theme.SUCCESS; }
            JLabel l = Theme.label(e.getKey() + ":  " + c + " / " + t + "  (" + pct + "%)  -  " + status,
                    Theme.BOLD, col);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            body.add(l);
            body.add(Box.createVerticalStrut(4));
        }

        body.add(Box.createVerticalStrut(10));
        JLabel plan = Theme.label("Study Plan", Theme.HEADING, Theme.TEXT);
        plan.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(plan);
        body.add(Box.createVerticalStrut(6));

        for (String tip : StudyAdvisor.advise(r)) {
            JTextArea ta = new JTextArea("\u2022 " + tip);
            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);
            ta.setEditable(false);
            ta.setOpaque(false);
            ta.setFont(Theme.BODY);
            ta.setForeground(Theme.TEXT);
            ta.setAlignmentX(Component.LEFT_ALIGNMENT);
            body.add(ta);
            body.add(Box.createVerticalStrut(4));
        }

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel chip(String title, int value, Color fg, Color bg) {
        JPanel p = new JPanel(new GridLayout(0, 1, 0, 2));
        p.setBackground(bg);
        p.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        JLabel v = Theme.label(String.valueOf(value), new Font("SansSerif", Font.BOLD, 24), fg);
        JLabel t = Theme.label(title, Theme.SMALL, fg);
        v.setHorizontalAlignment(SwingConstants.CENTER);
        t.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(v);
        p.add(t);
        return p;
    }
}