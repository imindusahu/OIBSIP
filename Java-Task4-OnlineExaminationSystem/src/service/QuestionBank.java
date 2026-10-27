package service;

import model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    public static List<Question> getQuestions() {
        List<Question> list = new ArrayList<>();
        list.add(new Question("Which keyword is used to inherit a class in Java?",
                new String[]{"implements", "extends", "inherits", "super"}, 1,
                "OOP", 1, "'extends' is used for class inheritance; 'implements' is for interfaces."));
        list.add(new Question("Which of these is NOT a primitive type?",
                new String[]{"int", "boolean", "String", "char"}, 2,
                "Basics", 1, "String is a class (object), not a primitive."));
        list.add(new Question("What is method overloading?",
                new String[]{"Same name, different parameters", "Same name, same parameters", "Different name, same parameters", "Redefining in subclass"}, 0,
                "OOP", 2, "Overloading = same method name with different parameter lists in the same class."));
        list.add(new Question("Which collection does NOT allow duplicates?",
                new String[]{"ArrayList", "LinkedList", "HashSet", "Vector"}, 2,
                "Collections", 2, "Set implementations such as HashSet store unique elements only."));
        list.add(new Question("Which Swing class is used to run code safely on the UI thread?",
                new String[]{"SwingUtilities.invokeLater", "Thread.run", "Timer.stop", "JFrame.run"}, 0,
                "Swing", 3, "Swing is not thread-safe, so UI work goes through invokeLater / the EDT."));
        list.add(new Question("What does the 'final' keyword on a class mean?",
                new String[]{"Cannot be instantiated", "Cannot be inherited", "Cannot have methods", "Must be abstract"}, 1,
                "OOP", 3, "A final class cannot be extended."));
        list.add(new Question("Which exception is thrown when dividing an int by zero?",
                new String[]{"NullPointerException", "ArithmeticException", "IOException", "NumberFormatException"}, 1,
                "Exceptions", 1, "Integer division by zero throws ArithmeticException."));
        list.add(new Question("Which block always runs, whether or not an exception occurs?",
                new String[]{"try", "catch", "finally", "throw"}, 2,
                "Exceptions", 2, "'finally' executes after try/catch, even if an exception is thrown (unless the JVM exits)."));
        list.add(new Question("Which of these is a checked exception?",
                new String[]{"IOException", "NullPointerException", "ArithmeticException", "ArrayIndexOutOfBoundsException"}, 0,
                "Exceptions", 3, "IOException must be handled or declared; the others are unchecked (RuntimeException subclasses)."));
        list.add(new Question("What is the default value of an int instance variable?",
                new String[]{"null", "1", "0", "undefined"}, 2,
                "Basics", 1, "Numeric instance variables default to 0; object references default to null."));
        list.add(new Question("Which operator compares two values for equality?",
                new String[]{"=", "==", "=>", ":="}, 1,
                "Basics", 1, "'=' assigns a value; '==' compares."));
        list.add(new Question("Which interface lets you store key-value pairs?",
                new String[]{"List", "Set", "Map", "Queue"}, 2,
                "Collections", 1, "Map stores key-value pairs, e.g. HashMap."));
        list.add(new Question("Which collection keeps insertion order and allows duplicates?",
                new String[]{"HashSet", "ArrayList", "TreeSet", "HashMap"}, 1,
                "Collections", 3, "ArrayList preserves insertion order and allows duplicates."));
        list.add(new Question("Which layout manager shows only one component at a time?",
                new String[]{"BorderLayout", "GridLayout", "FlowLayout", "CardLayout"}, 3,
                "Swing", 1, "CardLayout stacks components and shows one at a time, like our screens."));
        list.add(new Question("Which method adds a click handler to a JButton?",
                new String[]{"addActionListener", "addClickEvent", "onClick", "setHandler"}, 0,
                "Swing", 2, "JButton uses addActionListener with an ActionListener."));
        list.add(new Question("What does 'abstract' on a method mean?",
                new String[]{"It has no body and must be implemented by subclasses", "It cannot be called",
                        "It runs faster", "It is private"}, 0,
                "OOP", 2, "An abstract method has no body; concrete subclasses must override it."));
        list.add(new Question("What is the output of `System.out.println(5 + 5 + \"Java\" + 5 + 5);` in Java?",
                new String[]{"10Java1010", "10Java55", "55Java55", "10Java10"}, 0,
                "Basics", 2, "Addition happens from left to right: 5+5=10, then concatenated with \"Java\" to become \"10Java\", then concatenated with subsequent numbers as strings."));

        list.add(new Question("Which of the following is true regarding wrapper classes and object caching (e.g., Integer caching) in Java?",
                new String[]{"All Integer objects created using `Integer.valueOf()` are always stored in a new memory location.", "Integer values between -128 and 127 are cached by default, meaning `Integer.valueOf(100) == Integer.valueOf(100)` evaluates to true.", "The cache range for Integer cannot be modified using JVM arguments.", "Wrapper class instances are mutable once created."}, 1,
                "Basics", 2, "Java caches Integer objects in the range of -128 to 127 for memory optimization, making `==` comparison return true for values within this range."));

        list.add(new Question("What is the memory and execution behavior of a `static` inner class compared to a non-static inner class (nested class) in Java?",
                new String[]{"A static inner class holds an implicit reference to the outer class instance, leading to potential memory leaks.", "A non-static inner class can be instantiated without an instance of the outer class.", "A static inner class does not require an instance of the outer class to be instantiated and cannot access non-static members of the outer class directly.", "Both static and non-static inner classes behave identically regarding outer class instance dependency."}, 2,
                "Basics", 3, "Static nested classes do not have a reference to an enclosing outer class instance, whereas non-static inner classes require an outer class instance and implicitly hold a reference to it."));

        list.add(new Question("What is the result of `10 == 10.0` and `\"Hello\" == \"Hello\"` in Java?",
                new String[]{"true and false", "true and true (usually true due to string pooling)", "false and true", "false and false"}, 1,
                "Basics", 2, "Primitive numeric comparison (10 == 10.0) compares values and returns true. String literals are pooled by the compiler, so literal string comparison with `==` also evaluates to true in this case (though `.equals()` is standard for content comparison)."));

        return list;
    }
}
