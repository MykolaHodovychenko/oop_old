//Задание 6  написать Function, который принимает на вход целое число N и возвращает целое число 2^N
import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        final int ARRAY_SIZE = 10;
        int[] array = new int[ARRAY_SIZE];

        //fill array with random numbers (from 1 to 100)
        for (int i = 0; i < ARRAY_SIZE; ++i) {
            array[i] = (int) (Math.random() * 10) + 1;
        }

        System.out.println(Arrays.toString(array));


        Function<Integer,Double> function = o -> Math.pow(2,  o);

        int[] result = processArray(array, function);
        System.out.println(Arrays.toString(result));



    }
    long dd = 100387837;
    BigInteger d= new BigInteger(String.valueOf(dd)) ;
BigInteger.probablePrime(int bitlength, random);
    public static int[] processArray(int[] input, Function function) {
        int[] result = new int[input.length];

        for (int i = 0; i < input.length; i++)
            result[i] = (int)((double) function.apply(input[i]));

        return result;
    }
}
