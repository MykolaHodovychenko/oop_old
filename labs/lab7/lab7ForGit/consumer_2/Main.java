//Задание 5  написать метод, который принимает Predicate и Consumer.
//Действие в Consumer выполняется только, если условие в Predicate истино
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {
        final int ARRAY_SIZE = 10;
        int[] array = new int[ARRAY_SIZE];

        //fill array with random numbers (from 1 to 100)
        for (int i = 0; i < ARRAY_SIZE; ++i) {
            array[i] = (int) (Math.random() * 100) + 1;
        }

        System.out.println("Before: " + Arrays.toString(array));

        //filter array from odd
        System.out.print("Filter: ");
        printIf(array, o -> {
            int value = (int) o;
            return value % 2 == 0;
        }, (i) -> System.out.print(i + " "));
    }

    public static void printIf(int[] input, Predicate p, Consumer c) {
        for (int i : input) {
            if (p.test(i)) {
                c.accept(i);
            }
        }
    }

}
