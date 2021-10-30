//Задание 2 Напишите метод фильтрации массива студентов.
// фильтрация на примере предиката, который отсеивает студентов с задолженноостью

//Задание 3 , фильтрация на основе 2х условий
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {

        Student[] group = {
                new Student("Harry Potter", "Gryffindor", new int[]{75, 90, 90, 100, 60, 75, 40, 75, 75}),
                new Student("Hermione Granger", "Gryffindor", new int[]{100, 100, 100, 90, 100, 100, 100, 100, 100}),
                new Student("Draco Malfoy", "Slytherin", new int[]{75, 60, 100, 90, 60, 75, 90, 40, 75}),
                new Student("Ron Weasley", "Gryffindor", new int[]{100, 75, 90, 90, 60, 40, 40, 75, 75}),
                new Student("Cedric Diggory", "Hufflepuff", new int[]{90, 100, 90, 100, 90, 75, 90, 75, 75})
        };

        //фильтр студентов не сдавших одни и больше предметов
        Predicate<Student> isDebtor = o -> {

            for (int i : o.getMarks()) {
                if (i < 60) return false;
            }
            return true;
        };
        //фильтр студентов не из гриффиндора
        Predicate<Student> isGryff = o -> {

            if (!o.getGroup().equals("Gryffindor")) return false;

            return true;
        };

        //для вывода
        Consumer<Student> c1 = o -> System.out.println(o.getName() + " " + o.getGroup() + " " + Arrays.toString(o.getMarks()));

        Student[] res = filter(group, isDebtor);
        forEach(res, c1);

        res = filter(group, isDebtor, isGryff);
        forEach(res, c1);


    }

    public static Student[] filter(Student[] input, Predicate p) {
        Student[] result = new Student[input.length];

        int counter = 0;
        for (Student i : input) {
            if (p.test(i)) {
                result[counter] = i;
                counter++;
            }
        }

        return Arrays.copyOfRange(result, 0, counter);
    }

    public static Student[] filter(Student[] input, Predicate p1, Predicate p2) {
        Student[] result = new Student[input.length];

        int counter = 0;
        for (Student i : input) {
            if (p1.test(i) && p2.test(i)) {
                result[counter] = i;
                counter++;
            }
        }

        return Arrays.copyOfRange(result, 0, counter);
    }

    public static void forEach(Student[] input, Consumer action) {
        for (Student i : input) {
            action.accept(i);
        }
    }
}


import java.util.Arrays;
import java.util.function.Predicate;

public class Student {
    private String name;
    private String group;

    private int[] marks;

    public Student(String name, String group, int[] marks) {
        this.name = name;
        this.group = group;
        this.marks = marks;
    }


    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public int[] getMarks() {
        return marks;
    }

}

