//Задание 1 Напишите предикат, который возвращает true, если число простое
import java.util.Arrays;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {
        final int ARRAY_SIZE = 1000;

        int[] array = new int[ARRAY_SIZE];

        //fill array with random numbers (from 1 to 100)
        for (int i = 0; i < ARRAY_SIZE; ++i) {
            array[i] = (int) (Math.random() * 10000) + 1;
        }


        System.out.println(Arrays.toString(array));


        //we pass the block lambda expression of the predicate to the method
        int[] res = filter(array, o -> {
            int value = (int) o;
            for (int i = 2; i <= Math.sqrt(value); i++) {
                if (value % i == 0)
                    return false;
            }
            return true;
        });

        System.out.println(Arrays.toString(res));

    }

    public static int[] filter(int[] input, Predicate p) {
        int[] result = new int[input.length];

        int counter = 0;
        for (int i : input) {
            if (p.test(i)) {
                result[counter] = i;
                counter++;
            }
        }

        return Arrays.copyOfRange(result, 0, counter);
    }
}
