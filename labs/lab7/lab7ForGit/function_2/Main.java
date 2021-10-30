//Задание 7 метод stringify(),принимает  целых чисел от 0 до 9 и Function.
//Напишите Function, на вход целое число от 0 до 9 и возвращает  строку 
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {

        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Function<Integer, String> function = o -> {
            switch (o) {
                case 0:
                    return "Zero";
                case 1:
                    return "One";
                case 2:
                    return "Two";
                case 3:
                    return "Three";

                case 4:
                    return "Four";

                case 5:
                    return "Five";

                case 6:
                    return "Six";

                case 7:
                    return "Seven";

                case 8:
                    return "Eight";

                case 9:
                    return "Nine";

                default:
                    return "";
            }
        };
        System.out.println(stringify(array, function));

    }

    public static String stringify(int[] input, Function function) {
        String res = "";
        for (int i : input) {
            res = res.concat((String) function.apply(input[i]) + " ");
        }
        return res;
    }
}
