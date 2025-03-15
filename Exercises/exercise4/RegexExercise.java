package Exercises.exercise4;
import java.util.regex.*;

public class RegexExercise {
    public static void main(String[] args) {
        // my example 1: Phone number
        String phoneRegex = "^\\d{3}-\\d{3}-\\d{4}$";
        testRegex(phoneRegex, "408-456-7890", "20343-84567");

        // my example 2: zipcode
        String zipRegex = "^\\d{5}$";
        testRegex(zipRegex, "95125", "1234");

        // my example 3: date
        String dateRegex = "^\\d{2}/\\d{2}/\\d{4}$";
        testRegex(dateRegex, "12/31/1999", "12/31/99");

        // my example 4: address
        String addressRegex = "^\\d+\\s[A-Za-z]+\\s[A-Za-z]+";
        testRegex(addressRegex, "4 North St", "123 Main");

        // my example 5: email
        String emailRegex = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+$";
        testRegex(emailRegex, "test@example.com", "invalid-email-address");
        
    }

    public static void testRegex(String regex, String positiveCase, String negativeCase) {
        Pattern pattern = Pattern.compile(regex);

        // test positive case
        Matcher positiveMatcher = pattern.matcher(positiveCase);
        boolean positiveMatch = positiveMatcher.matches();
   

        // test negative case
        Matcher negativeMatcher = pattern.matcher(negativeCase);
        boolean negativeMatch = negativeMatcher.matches();

        //test here & print out
        System.out.println("Testing regex: " + regex);
        System.out.println("Positive case: '" + positiveCase + "' -> " + positiveMatch);
        System.out.println("Negative case: '" + negativeCase + "' -> " + negativeMatch);
        System.out.println(); //leave a blank space to separate different tests prinouts

    }
}