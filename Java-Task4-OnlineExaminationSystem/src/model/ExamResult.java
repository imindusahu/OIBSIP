package model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExamResult {
    private final List<Question> questions;
    private final int[] answers;
    private final int timeTakenSeconds;

    public ExamResult(List<Question> questions, int[] answers, int timeTakenSeconds) {
        this.questions = questions;
        this.answers = answers;
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public List<Question> getQuestions() { return questions; }
    public int[] getAnswers() { return answers; }
    public int getTimeTakenSeconds() { return timeTakenSeconds; }
    public int getTotal() { return questions.size(); }

    public int getScore() {
        int score = 0;
        for(int i = 0; i < questions.size(); i++) {
            if(answers[i] == questions.get(i).getCorrectIndex()) score++;
        }
        return score;
    }

    public int getUnanswered() {
        int n = 0;
        for(int i = 0; i < questions.size(); i++) if(answers[i] == -1) n++;
        return n;
    }

    // topic -> {correct, total}
    public Map<String, int[]> getTopicStats() {
        Map<String, int[]> stats = new LinkedHashMap<>();
        for(int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            int[] s = stats.computeIfAbsent(q.getTopic(), k -> new int[2]);
            s[1]++;
            if(answers[i] == q.getCorrectIndex()) s[0]++;
        }
        return stats;
    }
}
