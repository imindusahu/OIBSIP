package model;

import java.util.List;

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
}
