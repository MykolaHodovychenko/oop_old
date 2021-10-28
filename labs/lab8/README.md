# Лабораторна робота 8. Узагальнене програмування

## Цілі лабораторної роботи

- вивчити принципи та призначення механізму узагальненого програмування;
- навчитися створювати узагальнені класи та інтерфейси;
- Розібратися зі створенням узагальнених методів.

## Завдання на лабораторну роботу

### Завдання 1

Даний узагальнений інтерфейс

```java
interface Stack<T> {
    void push(T element);
    T pop();
    T peek();
    boolean isEmpty();
    void clear();
}
```

Для створення масиву узагальненого типу використовуйте таку конструкцію

```java
T[] array = (T[])new Object[INITIAL_ARRAY_LENGTH];
```

1. Реалізуйте інтерфейс за допомогою узагальненого класу (візьміть реалізацію стека з лабораторної 3 та покращить код виходячи з набутого досвіду розробки)

2. Реалізуйте інтерфейс за допомогою конкретного класу, вказавши клас `String` як типізовану змінну

### Завдання 2

Даний клас `BookData`, який моделює інформацію про книгу в інтернет-магазині. Поле `reviews` зберігає загальну кількість оцінок користувачів, а `total` зберігає загальну суму оцінок. Рейтинг книги вираховується як `total/reviews`

```java
class BookData {

    private String title;
    private String author;
    private int reviews;
    private double total;
}
```

Необхідно зробити так, щоб клас підтримував узагальнений інтерфейс `Comparable<>`. Книга з вищим рейтингом має бути "меншою", ніж книга з меншим рейтингом. За рівності рейтингів книги порівнюються за алфавітом заголовка.

### Завдання 3

Дано наступний код

```java
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {
        Printer myPrinter = New Printer();
        Integer[] intArray = {1, 2, 3};
        String[] stringArray = {"Hello", "World"};
        myPrinter.printArray(intArray);
        myPrinter.printArray(stringArray);
    }
}

class Printer {

}
```

У нас є масив цілих чисел та масив рядків. Напишіть у класі `Printer` узагальнений метод `printArray()`, який може роздрукувати всі елементи як масиву цілих чисел, так і масиву рядків (дивіться код методу `main()`).

При правильному розв'язанні задачі, код у методі `main()` повинен працювати коректно та роздруковувати елементи масивів. **Код методу `main()` міняти не можна!**

### Завдання 4

Наведено приклад методу `filter()` з лабораторної роботи 7.

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

Напишіть узагальнену версію методу `filter()`, який бы міг приймати на вхід масив об'єктів типу `T` і узагальнений предикат типу `T`.

Для створення масиву узагальненого типу використовуйте таку конструкцію

```java
T[] array = (T[])new Object[INITIAL_ARRAY_LENGTH];
```

### Завдання 5

Даний метод `contains()`, який перевіряє входження рядка до масиву рядків.

```java
boolean contains(String[] array, String element) {

    for (String str : array)
        if (str.equals(element))
            return true;

    return false;
}
```

Напишіть узагальнену версію методу `contains()`. Узагальнена версія методу приймає на вхід масив типу `T` (тип `T` повинен реалізовувати узагальнений інтерфейс `Comparable`) та об'єкт типу `V` (який має бути або `Т` або його підкласом).

### Завдання 6

Однією з важливих речей, якої не вистачає в Java - це можливість повертати декілька об'єктів в результаті роботи методу. Оператор `return` дозволяє вказати лише один об'єкт, але ми можемо створити об'єкт, який зберігає в собі численні об'єкти, які ви хочете повернути.

Така концепція називається кортеж (tuple) і є групою об'єктів, "загорнуту" в один об'єкт. Одержувач об'єкта може читати елементи, але не може додавати чи змінювати існуючі.

Кортежі можуть бути різної довжини, але кожен об'єкт кортежу може бути різного типу. Для забезпечення типобезпеки такого кортежу можна використовувати механізм узагальненого програмування.

Нижче наведено приклад кортежу для двох конкретних типів

```java
class ConcreteTwoTuple {

    public final String first;
    public final Integer second;

    public ConcreteTwoTuple(String first, Integer second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public String toString() {
        return"("+ first + "," + second + ')';
    }
}
```

Нижче наведено приклад використання такого кортежу

```java
StudentRatingTuple tuple = getStudentWithRating("Сидорів Петро Петрович");
System.out.println("Студент: "+tuple.first+", рейтинг: "+tuple.second);

...

public StudentRatingTuple getStudentWithRating(String fullName) {
    Student student = New Student(fullName);
    int rating = student.calculateRating();

    return new StudentRatingTuple(student, rating);
}

...

class StudentRatingTuple {

    public final Student first;
    public final Integer second;

    Public StudentRatingTuple(Student first, Integer second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ')';
    }
}
```

Такий підхід не дає нам достатньо гнучкості, так як для кожного типу `first` та `second` ми повинні вигадувати свій окремий клас.

Ми можемо скористатися типом `Object`

```java
class ObjectTwoTuple {

    public final Object first;
    public final Object second;

    Public ObjectTwoTuple(Object first, Object second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ')';
    }
}
```

але такий підхід не зберігає інформацію про типи полів `first` та `second`.

Ваше завдання – виправити цю проблему за допомогою механізму узагальненого програмування.

1. Напишіть узагальнену версію класу `ConcreteTwoTuple` під назвою `GenericTwoTuple`, в якій поле `first` буде типом `T`, а поле `second` - типом `V`.
2. Створіть клас-спадкоємець класу `GenericTwoTuple` під назвою `GenericThreeTuple`, в якому з'явиться поле `three` типу `S`.
3. Продемонструйте роботу класів `GenericTwoTuple` та `GenericThreeTuple` на прикладах, які придумаєте самостійно.
