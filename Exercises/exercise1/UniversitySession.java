package Exercises.exercise1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//base class
//a class hierarchy with base class as Student 
abstract class Student {
    protected String name;
    protected List<Integer> quizScores;
    protected static final Random rand = new Random();
    protected static final int NUM_QUIZZES = 15;

    public Student(String name) {
        this.name = name;
        this.quizScores = new ArrayList<>();
        generateQuizScores();
    }

    private void generateQuizScores() {
        for(int i = 0; i < NUM_QUIZZES; i++) {
            quizScores.add(rand.nextInt(101)); // 0-100
        }
    }

    public double calculateAverageQuizScore() {
        return quizScores.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    public List<Integer> getQuizScores() {
        return new ArrayList<>(quizScores);
    }
    
    public String getName() {
        return name;
    }

}

//derived class
//part-time student
class PartTimeStudent extends Student {
    public PartTimeStudent(String name) {
        super(name); // Calls base class constructor here
    }
}

//full-time student
class FullTimeStudent extends Student {
    private List<Integer> examScores;
    private static final int NUM_EXAMS = 2;

    public FullTimeStudent(String name) {
        super(name);
        this.examScores = new ArrayList<>();
        generateExamScores();
    }

    private void generateExamScores() {
        for(int i = 0; i < NUM_EXAMS; i++) {
            examScores.add(rand.nextInt(101));
        }
    }

    public List<Integer> getExamScores() {
        return new ArrayList<>(examScores);
    }
}

//Session Class
class Session {
    //Use data structure to hold 20 students in a Session, some are part-time, some are full-time
    private List<Student> students;
    private static final int TOTAL_STUDENTS = 20;

    public Session() {
        students = new ArrayList<>();
        populateStudents();
    }

    private void populateStudents() {
        for(int i = 1; i <= TOTAL_STUDENTS; i++) {
            if(i <= TOTAL_STUDENTS/2) {
                students.add(new PartTimeStudent("Part-Time Student " + i));
            } else {
                students.add(new FullTimeStudent("Full-Time Student " + i));
            }
        }
    }

    //Create public methods to calculate average quiz scores per student for the whole class
    public void printAverageQuizScores() {
        System.out.println("\nAverage Quiz Scores:");
        for(Student s : students) {
            System.out.printf("%s: %.2f%n", 
                s.getName(), s.calculateAverageQuizScore());
        }
    }

    //Create public method to print the list of quiz scores in ascending order for one session
    public void printSortedQuizScores() {
        System.out.println("\nSorted Quiz Scores:");
        for(Student s : students) {
            List<Integer> sorted = new ArrayList<>(s.getQuizScores());
            Collections.sort(sorted);
            System.out.println(s.getName() + ": " + sorted);
        }
    }

    //print part time student names
    public void printPartTimeStudents() {
        System.out.println("\nPart-Time Students:");
        students.stream()
                .filter(s -> s instanceof PartTimeStudent)
                .forEach(s -> System.out.println(s.getName()));
    }

    //print full time student's exam scores
    public void printFullTimeExamScores() {
        System.out.println("\nFull-Time Student Exam Scores:");
        students.stream()
                .filter(s -> s instanceof FullTimeStudent)
                .map(s -> (FullTimeStudent)s)
                .forEach(s -> System.out.println(
                    s.getName() + ": " + s.getExamScores()));
    }
}

//Main class
public class UniversitySession {
    public static void main(String[] args) {
        Session session = new Session();
        
        session.printAverageQuizScores();
        session.printSortedQuizScores();
        session.printPartTimeStudents();
        session.printFullTimeExamScores();
    }
}