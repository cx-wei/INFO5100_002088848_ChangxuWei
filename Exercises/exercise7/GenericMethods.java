package Exercises.exercise7;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class GenericMethods {

    // (a) Generic method to count elements with a specific property
    public static <T> int countElementsWithProperty(Collection<T> collection, Predicate<T> property) {
        int count = 0;
        for (T element : collection) {
            if (property.test(element)) {
                count++;
            }
        }
        return count;
    }

    // (b) Generic method to exchange positions of two elements in an array
    public static <T> void swapElements(T[] array, int index1, int index2) {
        if (index1 < 0 || index1 >= array.length || index2 < 0 || index2 >= array.length) {
            throw new IndexOutOfBoundsException("Invalid indices for array swap");
        }
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    // (c) Generic method to find maximal element in range [begin, end) of a list
    public static <T extends Comparable<? super T>> T findMaxInRange(List<T> list, int begin, int end) {
        if (begin < 0 || end > list.size() || begin >= end) {
            throw new IllegalArgumentException("Invalid range parameters");
        }
        
        T max = list.get(begin);
        for (int i = begin + 1; i < end; i++) {
            T current = list.get(i);
            if (current.compareTo(max) > 0) {
                max = current;
            }
        }
        return max;
    }

    // Testing the results here:
    public static void main(String[] args) {
        // Test for part (a)
        List<Integer> numbers = List.of(1, 2, 3, 11, 5, 90, 7, 801, 9, 10);
        int oddCount = countElementsWithProperty(numbers, n -> n % 2 != 0);
        System.out.println("Number of odd integers: " + oddCount);

        // Test for part (b)
        String[] words = {"summer", "banana", "cherry", "winter"};
        System.out.println("Before swap: " + java.util.Arrays.toString(words));
        swapElements(words, 1, 2);
        System.out.println("After swap: " + java.util.Arrays.toString(words));

        // Test for part (c)
        List<Double> doubles = List.of(1.1, 7.7, 3.3, 8.8, 2.2, 9.9, 4.4);
        Double maxInRange = findMaxInRange(doubles, 2, 5);
        System.out.println("Max in range [2,5): " + maxInRange);
    }
}