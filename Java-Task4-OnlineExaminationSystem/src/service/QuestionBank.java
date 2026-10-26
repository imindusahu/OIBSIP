package service;

import model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    public static List<Question> getQuestions() {
        List<Question> list = new ArrayList<>();
        list.add(new Question("Which keyword is used to inherit a class in Java?", new String[]{"implements", "extends", "inherits", "super"}, 1, "OOP", 1, "'extends' is used for class inheritance; 'implements' is for interfaces."));
        list.add(new Question("Which of these is NOT a primitive type?", new String[]{"int", "boolean", "String", "char"}, 2, "Basics", 1, "String is a class (object), not a primitive."));
        list.add(new Question("What is method overloading?", new String[]{"Same name, different parameters", "Same name, same parameters", "Different name, same parameters", "Redefining in subclass"}, 0, "OOP", 2, "Overloading = same method name with different parameter lists in the same class."));
        list.add(new Question("Which collection does NOT allow duplicates?", new String[]{"ArrayList", "LinkedList", "HashSet", "Vector"}, 2, "Collections", 2, "Set implementations such as HashSet store unique elements only."));
        list.add(new Question("Which Swing class is used to run code safely on the UI thread?", new String[]{"SwingUtilities.invokeLater", "Thread.run", "Timer.stop", "JFrame.run"}, 0, "Swing", 3, "Swing is not thread-safe, so UI work goes through invokeLater / the EDT."));
        list.add(new Question("What does the 'final' keyword on a class mean?", new String[]{"Cannot be instantiated", "Cannot be inherited", "Cannot have methods", "Must be abstract"}, 1, "OOP", 3, "A final class cannot be extended."));
        return list;
    }
}
