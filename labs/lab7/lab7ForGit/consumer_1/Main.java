//Задание 4 Consumer. Вывести ФАМИЛИЯ+ИМЯ массива студентов
//иcпользуя метод forEach
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        Student[] group = {
                new Student("Harry", "Potter"),
                new Student("Hermione", "Granger"),
                new Student("Draco", "Malfoy"),
                new Student("Ron", "Weasley"),
                new Student("Cedric", "Diggory")
        };

        Consumer<Student> c1 = o -> System.out.println(o.getSurname() + " " + o.getName());

        forEach(group,c1);
    }

    public static void forEach(Student[] input, Consumer action) {
        for (Student i : input) {
            action.accept(i);
        }
    }
}


public class Student {
    String name;
    String surname;

    public Student(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }


}

