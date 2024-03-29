# Лабораторная работа 7. Лямбда-выражения. Параметризация поведения

## Цели лабораторной работы

- изучить процесс создания и сценарии использования анонимных объектов;
- научиться создавать анонимные классы;
- разобраться с созданием одиночных и блочных лямбда-выражения.

## Теоретический материал

### 1. Функциональный интерфейс `Predicate`

`Predicate` - встроенный функциональный интерфейс, добавленный в Java 8 в пакет `java.util.function`. Интерфейс описывает один метод `test()`, который принимает на входе значение, проверяет его состояние и возвращает `boolean` в качестве результата. Интерфейс `Predicate` подтверждает какое-то значение как `true` или `false`.

Интерфейс выглядит следующим образом

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}
```

> Пока не обращайте внимания на запись `<T>` и `(T t)`, считайте что там `Object`.

Функциональный дескриптор интерфейса

```java
T -> boolean
```

Следующий код

```java
Predicate negative = i -> (int)i < 0;

System.out.println(negative.test(2));
System.out.println(negative.test(-2));
System.out.println(negative.test(0));
```

Выведет в консоль

```
false
true
false
```

Рассмотрим реальный пример, в котором нам может понадобиться функциональный интерфейс `Predicate`.

Представим, что нам надо реализовать метод для фильтрации массива целых чисел. Если число удовлетворяет условию, то оно попадает в итоговый массив,
а числа, не прошедшие фильтр, отсеиваются. Код такого метода может выглядеть следующим образом

```java
public int[] filter(int[] input) {
    int[] result = new int[input.length];

    int counter = 0;
    for (int i : input) {
        if ( [проверка_условия] ) {
            result[counter] = i;
            counter++;
        }
    }

    return Arrays.copyOfRange(result, 0, counter);
}
```

Изюминка данной задачи состоит в том, что нам крайне желательно описать только один метод, который бы мог по-разному фильтровать данные при том или ином вызове.
Мы можем реализовать это с помощью полиморфизма и механизма переопределения метода, но это будет слишком громоздко и не обеспечит должной гибкости кода.

Нам было бы крайне желательно передать в метод логику, по которой необходимо фильтровать входящий массив. Так как логика определяется блоком кода, то нам, фактически, надо в качестве одного из входных аргументов метода фильтрации, передать код для определения - проходит ли элемент массива фильтр или нет.

#### ⚠️**Механизм функциональных интерфейсов и лямбда-выражений позволяет легко передавать поведение как аргумент метода. С помощью передачи поведения, мы можем писать гибкие методы, работа которых может меняться в зависимости от передаваемого в метод поведения.:warning:**

Такую задачу можно реализовать с помощью функционального интерфейса `Predicate`. Добавим аргумент типа `Predicate` в качестве входного аргумента функции и будем вызывать у объекта типа `Predicate` метод `test()`, передавая ему очередной элемент для фильтрации. Если метод вернет `true`, то элемент проходит фильтр и попадает в результирующий массив.

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

Теперь мы можем передавать логику сравнения с помощью лямбда-выражения

```java
int[] test = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

// Находим четные числа
Predicate pr1 = o -> {
    int value = (int) o;
    return value % 2 == 0;
};

int[] res = filter(test, pr1);
System.out.println(Arrays.toString(res));

// Находим числа, которые делятся на 3 без остатка
Predicate pr2 = o -> {
    int value = (int) o;
    return value % 3 == 0;
};

res = filter(test, pr2);
System.out.println(Arrays.toString(res));
```
Программа выведет в консоль следующее
```
[2, 4, 6, 8, 10]
[3, 6, 9]
```

### 2. Функциональный интерфейс `Consumer`

`Consumer` - встроенный функциональный интерфейс, добавленный в Java SE 8 в пакет `java.util.function`. Принимает значение в качестве аргумента и ничего не возвращает

```java
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}
```
Функциональный дескриптор интерфейса

```java
T -> void
```

`Consumer` интерфейс используется в случае, если необходимо передать объект на вход и произвести над ним некоторые операции не возвращая результат. Самый частый случай использования этого интерфейса - это вывод на консоль.

```java

int[] test = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

Consumer consumer = (i) -> System.out.println(i);
forEach(test, consumer);

...

void forEach(int[] input, Consumer action) {
    for (int i : input) {
        action.accept(i);
    }
}
```

### 3. Функциональный интерфейс `Function`

`Function` - это встроенный функциональный интерфейс, добавленный в Java SE 8 в пакет `java.util.function`. Принимает значение в качестве аргумента одного типа и возвращает другое значение. 

```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
}
```
Функциональный дескриптор интерфейса

```java
T -> R
```

Данный интерфейс часто используется для преобразования одного значения в другое. В интерфейсе описан один абстрактный метод `apply()`, который принимает на вход объект и возвращает другой объект (он может быть этого или любого другого типа).

```java

int[] test = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

Function function = o -> {
    int value = (int) o;
    return value * 10;
};

int[] result = processArray(test, function);
System.out.println(Arrays.toString(result));

...

int[] processArray(int[] input, Function function) {
    int[] result = new int[input.length];

    for (int i = 0; i < input.length; i++)
        result[i] = (int) function.apply(input[i]);

    return result;
}
```

## Задания на лабораторную работу

#### Задание 1

1. Напишите предикат, который возвращает `true`, если число простое. Сгенерируйте массив из 1000 случайных целых чисел и проверьте работу фильтра.

#### Задание 2

1. Дан следующий класс `Student`. В случае необходимости добавьте нужные геттеры, сеттеры и конструкторы.

```java
class Student {
    private String name;
    private String group;

    private int[] marks;
}
```

2. Напишите метод фильтрации массива студентов.
3. Проверьте работу метода фильтрации на примере предиката, который отсеивает студентов, которые имеют 1 и более задолженности (оценка меньше 60).

#### Задание 3

1. Напишите метод фильтрации по двум условиям (два предиката). Элемент проходит через фильтр, если удовлетворяет оба условия.

#### Задание 4

1. Напишите `Consumer`, который принимает на вход объект типа `Student` и выводит в консоль строку вида `ФАМИЛИЯ + ИМЯ`. Создайте массив из нескольких студентов и проверьте работу функции `forEach()`

#### Задание 5

1. Напишите метод, который принимает `Predicate` и `Consumer`. Действие в `Consumer` выполняется только, если условие в `Predicate` выполняется. Создайте массив из целых чисел, придумайте два лямбда-выражения и проверьте работу этого метода.

#### Задание 6

1. Напишите `Function`, который принимает на вход целое число `N` и возвращает целое число `2^N`. Введите массив из 10 целых чисел и проверьте работу метода.

#### Задание 7

1. Напишите метод `stringify()`, который принимает на вход массив целых чисел от 0 до 9 и `Function`. Напишите `Function`, который принимает на вход целое число от 0 до 9 и возвращает его значение в виде строки ("ноль", "один", "два", "три" и так далее). Введите массив из 10 целых чисел и проверьте работу метода.

#### Дополнительное задание

Перепишите выполненное приложение **SortingList** из 6 лабораторной работы, чтобы оно использовало механизм лямбда-выражений.
