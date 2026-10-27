package service;

import model.ExamResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudyAdvisor {

    public static List<String> advise(ExamResult r) {
        List<String> tips = new ArrayList<>();

        for(Map.Entry<String, int[]> e : r.getTopicStats().entrySet()) {
            int correct = e.getValue()[0];
            int total = e.getValue()[1];
            int pct = (int) Math.round(100.0 * correct / total);
            if(pct < 50) {
                tips.add("Revise " + e.getKey() + " first: you got " + correct + " of " + total + " correct. Read the explanations above and practise the easy questions again.");
            } else if(pct < 80) {
                tips.add("Practise more " + e.getKey() + ": " + correct + " of " + total + " correct. You know the basics, now work on the harder questions.");
            }
        }

        if(r.getUnanswered() > 0) {
            tips.add("You left " + r.getUnanswered() + " question(s) unanswered. " + "Attempt every question; skip the hard ones first and come back later.");
        }

        double avgSeconds = r.getTimeTakenSeconds() / (double) r.getTotal();
        if(avgSeconds < 10 && r.getScore() * 2 < r.getTotal()) {
            tips.add("You answered very fast (about " + Math.round(avgSeconds) + " sec per question) and many answers were wrong. Read every option fully before choosing.");
        }

        if(tips.isEmpty()) {
            tips.add("Great job! All topics are strong. Try the Adaptive exam to face harder questions.");
        }
        return tips;
    }
}
