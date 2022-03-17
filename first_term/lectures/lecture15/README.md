# Некоторые аспекты современного программирования на Java

## Класс Optional и проблема NullPointerException

Исключения NullPointerException - головная боль любого разработчика на ОО-языке.

Британский специалист Тони Хоар, который создал пустые ссылки в 1965 году, считал что это самый удобный способ смоделировать **отсутствие значения**.

Допустим, есть вложенная структура объектов для владельца автомобиля, купившего автостраховку

```java
class Person {
    private Car car;

    public Car getCar() {
        return car;
    }
}

class Car {
    private Insurance insurance;

    public Insurance getInsurance() {
        return insurance;
    }
}

class Insurance {
    private String name;

    public String getName() {
        return name;
    }
}
```

Получим поле `name` для страховки автомобиля определенного человека

```java
public String getCarInsuranceName(Person person) {
    return person.getCar().getInsurance().getName();
}
```

Если у `person` не будет автомобиля, то какой будет результат вызова метода `getCar()`? Как правило, этот метод вернет `null`, чтобы указать на отсутствие значения (в данном случае, отсутствие машины). В результате вызова метода `getInsurance()` вернет страховку пустой ссылки, что приведет к генерации `NullPointerException` во время выполнения и остановке работы программы. А что если объект person был равен `null`? Что, если метод `getInsurance()` тоже вернул `null`?

Как избежать неожиданных `NullPointerException`? Обычно можно добавить проверки на `null` везде, где нужно. Попробуем переписать метод `getCarInsuranceName()`

```java
public String getCarInsuranceName(Person person) {

    if (person != null) {
        Car car = person.getCar();
        if (car != null) {
            Insurance insurance = car.getInsurance();
            if (insurance != null) {
                return insurance.getName();
            }
        }
    }
    return "unknown";
}
```

Мы не проверяем значение name на null, потому что **знаем**, что у нее должно быть имя (в данном  случае, это имя страховой компании, которая выдала страховку). Такая методика плохо масштабируется и снижает удобочитаемость.

Можно избежать глубокого вложения блоков `if` следующим образом

```java
public String getCarInsuranceName(Person person) {

    if (person == null)
        return "unknown";
    
    Car car = person.getCar();
    if (car == null)
        return "unknown";
    
    Insurance insurance = car.getInsurance();
    if (insurance == null)
        return "unknown";
    
    return insurance.getName();
}
```

но это не спасает ситуацию. Что, если разработчик забудет проверить, равно ли `null` одно из свойств?

Таким образом, использование `null` в Java приводит к следующим проблемам:

- служит источником ошибок - `NullPointerException` является наиболее распространенным исключением в Java
- "раздувает код" - проверки на `null` ухудшают читабельность кода
- использование `null` бессмысленно, потому что `null` не несет семантический смысл
- нарушает идеологию языка Java - язык Java скрывает от разработчиков указатели за исключением нулевого указателя
- создает дыру в системе типов - `null` не включает никакого типа или другой информации, так что его можно присвоить любому типу ссылки