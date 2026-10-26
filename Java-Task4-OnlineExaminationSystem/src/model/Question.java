package model;

public class Question {
    private final String  text;
    private final String[] options;
    private final int correctIndex;
    private final String topic;
    private final int difficulty;   // 1 = easy, 2 = medium, 3 = hard
    private final String explanation;

    public Question(String text, String[] options, int correctIndex, String topic, int difficulty, String explanation) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
        this.topic = topic;
        this.difficulty = difficulty;
        this.explanation = explanation;
    }
    public String getText() { return text; }
    public String[] getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
    public String getTopic() { return topic; }
    public int getDifficulty() { return difficulty; }
    public String getExplanation() { return explanation; }
}
