package service;

import model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdaptiveEngine {
    private final List<Question> pool;
    private final List<Question> asked = new ArrayList<>();
    private int level = 2;

    public AdaptiveEngine(List<Question> allQuestions) {
        this.pool = new ArrayList<>(allQuestions);
        Collections.shuffle(this.pool);
    }

    public void recordAnswer(boolean wasCorrect) {
        if (wasCorrect) level = Math.min(3, level + 1);
        else level = Math.max(1, level - 1);
    }

    public Question nextQuestion() {
        int[] order = {level, level - 1, level + 1, level - 2, level + 2};
        for (int lv : order) {
            for (Question q : pool) {
                if (q.getDifficulty() == lv && !asked.contains(q)) {
                    asked.add(q);
                    return q;
                }
            }
        }
        return null;
    }
    public int getLevel() { return level; }
}
