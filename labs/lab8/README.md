# Лабораторная работа 8. Обобщенное программирование

## Цели лабораторной работы

- изучить принципы и предназначение механизма обобщенного программирования;
- научиться создавать обобщенные классы и интерфейсы;
- разобраться с созданием обобщенных методов.

## Задания на лабораторную работу

### Задание 1

Дан обобщенный интерфейс

```java
interface Stack<T> {
    void push(T element);
    T pop();
    T peek();
    boolean isEmpty();
    void clear();
}
```

Для создания массива обобщенного типа используйте следующую конструкцию

```java
T[] array = (T[])new Object[INITIAL_ARRAY_LENGTH];
```

1. Реализуйте интерфейс с помощью обобщенного класса (возьмите реализацию стека из лабораторной 3 и улучшите код исходя из приобретенного опыта разработки)

2. Реализуйте интерфейс с помощью конкретного класса, указав класс `String` в качестве типизированной переменной

### Задание 2

Дан класс `BookData`, который моделирует информацию о книге в интернет-магазине. Поле `reviews` хранит общее количество оценок пользователей, а `total` хранит общую сумму оценок. Рейтинг книги высчитывается как `total / reviews`

```java
class BookData {

    private String title;
    private String author;
    private int reviews;
    private double total;
}
```

Необходимо сделать так, чтобы класс поддерживал обобщенный интерфейс `Comparable<>`. Книга с более высоким рейтингом должна быть "меньше", чем книга с меньшим рейтингом. При равенстве рейтингов, книги сравниваются по алфавиту заголовка.

### Задание 3

Дан следующий код

```java
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {
        Printer myPrinter = new Printer();
        Integer[] intArray = {1, 2, 3};
        String[] stringArray = {"Hello", "World"};
        myPrinter.printArray(intArray);
        myPrinter.printArray(stringArray);
    }
}

class Printer {

}
```

У нас есть массив целых чисел и массив строк. Напишите в классе `Printer` обобщенный метод `printArray()`, который может распечатать все элементы как массива целых чисел, так и массива строк (смотрите код метода `main()`).

При правильном решении задачи, код в методе `main()` должен работать корректно и распечатывать элементы массивов. **Код метода `main()` менять нельзя!**

### Задание 4

Дан пример метода `filter()` из лабораторной работы 7.

```java
public int[] filter(int[] input, Predicate p) {
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
```

Напишите обобщенную версию метода `filter()`, который бы мог принимать на вход массив объектов типа `T` и обобщенный предикат типа `T`.

Для создания массива обобщенного типа используйте следующую конструкцию

```java
T[] array = (T[])new Object[INITIAL_ARRAY_LENGTH];
```

### Задание 5

Дан метод `contains()`, который проверяет вхождение строки в массив строк.

```java
boolean contains(String[] array, String element) {

    for (String str : array)
        if (str.equals(element))
            return true;

    return false;
}
```

Напишите обобщенную версию метода `contains()`. Обобщенная версия метода принимает на вход массив типа `T` (тип `T` должен реализовывать обобщенный интерфейс `Comparable`) и объект типа `V` (который должен быть или `Т` или его подклассом).

### Задание 6

Одной из важных вещей, которой не хватает в языке Java - это возможность возвращать множество объектов в результате работы метода. Оператор `return` позволяет указать только один объект, но мы можем создать объект, который хранит в себе множественные объекты, которые вы хотите вернуть.

Такая концепция называется кортеж (tuple) и представляет собой группу объектов, "завернутую" в один объект. Получатель объекта может читать элементы, но не может добавлять или изменять существующие.

Кортежи могут быть разной длины, но каждый объект кортежа может быть объектом разного типа. Чтобы обеспечить типобезопасность такого кортежа, можно использовать механизм обобщенного программирования.

Ниже представлен пример кортежа для двух конкретных типов

```java
class ConcreteTwoTuple {

    public final String first;
    public final Integer second;

    public ConcreteTuple(String first, Integer second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public String toString() {
        return "(" + first + "," + second + ')';
    }
}
```

Ниже представлен пример использования такого кортежа

```java
StudentRatingTuple tuple = getStudentWithRating("Сидоров Петр Петрович");
System.out.println("Студент: " + tuple.first + ", рейтинг: " + tuple.second);

...

public StudentRatingTuple getStudentWithRating(String fullName) {
    Student student = new Student(fullName);
    int rating = student.calculateRating();

    return new StudentRatingTuple(student, rating);
}

...

class StudentRatingTuple {

    public final Student first;
    public final Integer second;

    public ConcreteTuple(Student first, Integer second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + "," + second + ')';
    }
}
```

Такой подход не дает нам достаточно гибкости, так как для каждого типа `first` и `second` мы должны придумывать свой отдельный класс.

Мы можем воспользоваться типом `Object`

```java
class ObjectTwoTuple {

    public final Object first;
    public final Object second;

    public ObjectTwoTuple(Object first, Object second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + "," + second + ')';
    }
}
```

но такой подход не сохраняет информацию о типах полей `first` и `second`.

Ваша задача - исправить эту проблему с помощью механизма обобщенного программирования.

1. Напишите обобщенную версию класса `ConcreteTwoTuple` под названием `GenericTwoTuple`, в которой поле `first` будет типом `T`, а поле `second` - типом `V`.
2. Создайте класс-наследник класса `GenericTwoTuple` под названием `GenericThreeTuple`, в котором появится поле `three` типа `S`.
3. Продемонстрируйте работу классов `GenericTwoTuple` и `GenericThreeTuple` на примерах, которые придумаете самостоятельно.
