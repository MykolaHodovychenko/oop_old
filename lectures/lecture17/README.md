# Принцип инверсии управления и внедрение зависимостей. Использование Spring Core для внедрения зависимостей. Spring IoC-контейнер. Виды связывания (wiring). Разрешение зависимостей. Классы JavaBeans.

Любое мало-мальски серьезное приложение состоит из нескольких классов, которые взаимодействуют друг с другом, чтобы реализовывать бизнес-логику. Обычно, каждый объект отвечает за получение ссылок на другие объекты, с которыми он взаимодействует (такие другие объекты называются **зависимостями**, **dependencies**). Такой подход может привести к созданию тесно связанного кода, который тяжело тестировать.

Рассмотрим небольшой участок кода, который состоит из класса `User` и класса `Sender`.

```java
public class User {

    public void sendMessage(String message, String target) {
        Sender sender = new Sender();
        sender.send(message, target);
    }
}

public class Sender {

    public void send(String message, String target) {
        System.out.println("Tweet: " + message + " to " + target);
    }
}
```

В результате мы получим тесно связанный код – класс `User` теперь напрямую зависит от класса `Sender`. Таким образом, если мы создадим класс `EmailSender`, который будет отсылать сообщения по электронной почте, то чтобы использовать объект класса `EmailSender`, нам придется изменять код класса `User`. К тому же, тестирование метода `sendMessage()` будет затруднительным.

Безусловно, мы не можем избежать связывания вообще, т.к. объектно-ориентированное программирование подразумевает взаимодействие множества объектов различных классов, программа из одного класса не имеет смысла. С другой стороны, нам необходимо избегать **тесного связывания** (**tight coupling**) классов, так как такой код тяжело повторно использовать, тестировать и тяжело понять, как это всё вместе работает.

В противовес тесному связыванию кода существует принцип слабо связного (**loose coupling**) кода. Слабая связность означает, что изменения, вносимые в один класс, повлекут за собой небольшие изменения в другие классы, что упростит тестирование, рефакторинг, повторное использование кода. Приложение с использованием принципа слабо связного кода легче модифицируется и поддерживается.

## Инверсия управления

Одним из приемов для написания слабо связного кода является принцип **инверсии управления** (**Inversion of Control**, IoC). Он заключается в том, что жизненным циклом (созданием, вызовом методов и уничтожением) ваших объектов управляете не вы сами, а некий сторонний код. Отсюда и термин «инверсия» – не я управляю кодом, а сторонний код управляет моими классами. Он решает, когда создавать объекты моих классов, когда вызывать их методы и когда уничтожать объекты. На принципе инверсии управления базируется работа всех фреймворков.

> Подробно читайте про инверсию контроля [в оригинале](http://martinfowler.com/bliki/InversionOfControl.html) или [в переводе](https://habr.com/post/116232/).

Отличие библиотеки от фреймворка состоит в том, что библиотека – это по существу набор функций, организованных в классы, которые вы можете вызывать по мере надобности. Каждый вызов выполняет некоторую работу и возвращает управление обратно пользователю.

С другой стороны, фреймворк воплощает в себе некоторый абстрактный дизайн приложения со своим поведением. Для того, чтобы использовать его, вы должны добавить свой код в различные места фреймворка, либо через наследование, либо подключив свой собственный класс. Код фреймворка впоследствии будет вызывать ваш код.

## Внедрение зависимостей

Одной из реализаций принципа инверсии управления является **внедрение зависимостей** (**Dependency Injection**, DI). Это принцип заключается в том, что зависимости класса не создаются или ищутся в самом классе, а **внедряются** (**inject**) извне некоторым другим внешним источником (например, каким-то другим объектом).

В статье Мартина Фаулера «Inversion of Control Containers and the Dependency Injection pattern» этот объект называется **сборщиком** (**assembler**), а сейчас его обычно называют **контейнером** (**container**) или **IoC-контейнером** (**IoC-container**).

> Статья Мартина Фаулера - [оригинал](https://www.martinfowler.com/articles/injection.html), [перевод первой части](http://yugeon-dev.blogspot.com/2010/07/inversion-of-control-containers-and_21.html) и [перевод второй части](http://yugeon-dev.blogspot.com/2010/07/blog-post.html).

В общем случае, IoC-контейнер – это некоторый программный код (фреймворк, отдельный класс), который осуществляет внедрение зависимостей в приложении и, насколько это возможно, упрощает данный процесс.

Как правило, внедрение зависимости осуществляется через:

- конструктор класса (constructor injection);
- поле класса (field injection);
- входной аргумент метода (method injection), то есть через сеттер.

Внедрение через статические поля и методы не рекомендуется.
Фреймворк Spring, прежде чем стать многофункциональной платформой, изначально разрабатывался как IoC-контейнер для упрощения разработки JavaEE-приложений.

<p align="center">
  <img src="img/img1.png" />
</p>

В приложениях на основе фреймворка Spring прикладные объекты располагаются внутри контейнера Spring. Как показано на рисунке, контейнер создает объекты, связывает их друг с другом, конфигурирует и управляет их полным жизненным циклом, от зарождения до самой их смерти (или от оператора new до вызова метода `finalize()`).

Классы, которыми управляет Spring-контейнер, называются бинами (bean) или компонентами. Контейнер создает, связывает между собой, а также уничтожает бины.

Фреймворк Spring имеет не один контейнер. В его состав входят несколько реализаций контейнера, которые подразделяются на два разных типа.
Фабрики компонентов (bean factories) (определяются интерфейсом org.springframework.beans.factory.BeanFactory) – самые простые из контейнеров, обеспечивающие базовую поддержку DI.

Контекст приложений (application contexts) (определяется интерфейсом org.springframework.context.ApplicationContext) основан на понятии фабрик компонентов и реализует прикладные службы фреймворка, такие как возможность приема текстовых сообщений из файлов свойств и возможность подписывать другие программные компоненты на события, возникающие в приложении.

С фреймворком Spring можно работать, используя и фабрики компонентов, и контексты приложений, но для большинства приложений фабрики компонентов часто оказываются слишком низкоуровневым инструментом. Поэтому контексты приложений выглядят более предпочтительно, чем фабрики компонентов.

В составе Spring имеется несколько разновидностей контекстов приложений. Три из них используются наиболее часто:

- ClassPathXmlApplicationContext – загружает определение контекста из XML-файла, расположенного в библиотеке классов (classpath), и обрабатывает файлы с определениями контекстов как ресурсы;
- FileSystemXmlApplicationContext – загружает определение контекста из XML-файла в файловой системе;
- XmlWebApplicationContext – загружает определение контекста из XML-файла, содержащегося внутри веб-приложения.

Давайте перепишем наш код, чтобы подготовить его к использованию IoC-контейнера Spring. Руководствуясь принципом Dependency Inversion (не путать с Dependency Injection, это разные принципы), создадим интерфейс Sender, чтобы не привязываться к конкретной реализации отправителя сообщений.

```java
public interface Sender {
    void sendMessage(String message, String target);
}
```

 Создадим класс `TwitterSender`, который реализует данный интерфейс.

 ```java
public class TwitterSender implements Sender {

    public void sendMessage(String message, String target) {
        System.out.println("Tweet: " + message + " is sending to " + target);
    }
}
 ```

Модифицируем класс `User`

```java
public class User {

    private Sender sender;

    public User(Sender sender) {
        this.sender = sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public void send(String message, String target) throws NullPointerException {
        if (sender != null) {
            sender.sendMessage(message, target);
        } else {
            throw new NullPointerException("Sender object is null");
        }
    }
}
```
Обратите внимание на разницу – мы теперь не сами создаем объект зависимости, а получаем его «извне» с помощью аргумента конструктора либо с помощью сеттера. Использование интерфейса позволяет легко использовать разные реализации отправщик сообщений. Еще одним бонусом является удобство проведения тестирования методов класса User, так как вместо настоящего отправщика сообщений мы можем подставить специальный мок-объект (mock object), который будет имитировать работу настоящего отправщика.

### Внедрение зависимости (wiring)

Итак, у нас есть соответствующие классы, теперь необходимо связывать это все воедино с помощью IoC-контейнера. Каким образом передать объект TwitterSender объекту User?
Процесс создания связи между компонентами приложения обычно называют wiring (в русской версии книги Spring in Action этот термин переводят как связывание, не путайте с сильным и слабым связыванием, которое переводится как tight coupling и loose coupling).
Подключим библиотеки Spring, которые нужны для связывания компонентов. Если вы используете Maven в качестве сборщика, от откройте pom-файл и добавьте следующие зависимости (на момент проведения занятия актуальная версия библиотек была 5.0.7, в вашем случае актуальная версия может быть другой)

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.0.7.RELEASE</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>5.0.7.RELEASE</version>
</dependency>
```

Важное отступление. Вы можете не добавлять библиотеку spring-core в pom-файл явно, код все равно будет работать. Это связано с тем, что spring-context не может работать без spring-core и Maven автоматически загрузит spring-core в любом случае, укажете ли вы ее в pom-файле или нет. В этом случае библиотека spring-core называется транзитивной зависимостью.

> Транзитивная зависимость - это зависимость, которая требуется для работы вашей прямой зависимости.

Такой механизм позволяет избежать ручного добавления в pom-файл всего графа зависимостей - вы просто указываете прямые зависимости, а Maven сделает все остальное.
Итак, вернемся к связыванию компонентов.

Важный момент, который необходимо запомнить при работе с контейнером - любой контейнер необходимо сконфигурировать. То есть, на плечи разработчика ложится обязанность указать контейнеру, какие компоненты создать и как их связать вместе.

Spring предлагает три способа связывания компонентов:

- явная конфигурация с помощью XML-файлов;
- явная конфигурация с помощью классов Java;
- неявное обнаружение бинов и автоматическое связывание.

В данном случае нет "самого лучшего" способа связывания, все три способа имеют право на жизнь. В данном занятии мы рассмотрим конфигурацию с помощью классов Java и автоматическое связывание.

### Конфигурация с помощью классов Java

Для начала создадим класс, в котором будет осуществляться конфигурация. Создадим пакет config и класс AppConfig. Так как в Spring может использоваться несколько способов связывания компонентов, то желательно пометить класс аннотацией @Configuration - такая аннотация говорит контейнеру, что этот класс является классом конфигурации.

```java
@Configuration
public class AppConfig {...}
```

Конфигурация в классе осуществляется с помощью методов и аннотаций. Добавим в класс следующий метод

```java
@Configuration
public class AppConfig {

    @Bean
    public TwitterSender twitterSender() {
        return new TwitterSender();
    }
}
```

Пометив метод аннотацией @Bean, мы говорим что данный метод возвращает объект, который который должен быть зарегистрирован как бин в контексте приложения Spring (то есть, в нашем IoC-контейнере). Таким образом, мы фактически объявляем бин в нашем контейнере. Название бина будет совпадать с названием метода, в нашем случа бин будет называться twitterSender.

Теперь добавим еще один метод

```java
@Configuration
public class AppConfig {

    @Bean
    public User user() {
        return new User(twitterSender());
    }

    @Bean
    public TwitterSender twitterSender() {
        return new TwitterSender();
    }
}
```

Объявляем еще один бин User и в методе осуществляем связывание бинов. В нашем случае мы осуществляем связывание через конструктор (constructor injection).

Таким образом, мы объявили два бина - twitterSender и user, после чего связали их с помощью constructor injection.

Теперь модифицируем класс Main, создадим контейнер и попробуем использовать класс User.

```java
public class Main {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(AppConfig.class);

        User user = context.getBean(User.class);
        user.send("Hello!", "Nick");

    }
}
```

Итак, сначала мы создали объект контейнера. В качестве реализации мы используем класc AnnotationConfigApplicationContext, который является реализацией интерфейса ApplicationContext, которая позволяет регистрировать аннотированные классы конфигурации. В нашем случае классом конфигурации является класс AppConfig, объявленный с помощью аннотации @Configuration. После того как вы зарегистрируете указанный класс, также регистрируются все типы bean-компонентов, возвращаемые с помощью методов, которые аннотируются с помощью @Bean.

После создания контейнера и загрузки конфигурации, используем класс User. Обратите внимание, что мы не сами создаем объект класса User и внедряем зависимости, а мы просто получаем объект из контейнера, с помощью метода getBean(). После того, как мы получили ссылку на объект, вызываем метод send() и получаем работающий класс User. Проверим работу приложения.

```plain
июл 01, 2018 2:37:09 PM org.springframework.context.support.AbstractApplicationContext prepareRefresh
INFO: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@4534b60d: startup date [Sun Jul 01 14:37:09 EEST 2018]; root of context hierarchy

Tweet: Hello! is sending to Nick
```

> Неплохой материал по поводу конфигурации с помощью классов можно почитать [здесь](https://www.tutorialspoint.com/spring/spring_java_based_configuration.htm) и [здесь](https://www.ibm.com/developerworks/ru/library/ws-springjava/index.html).

Таким образом мы реализовали связывание бинов с помощью контейнера Spring и конфигурации с помощью Java-классов. Теперь давайте рассмотрим автоматическое связывание.

### Автоматическое связывание

Способ автоматического связывания является наиболее простым в использовании.
Автоматическое связывание в Spring реализуется с помощью двух механизмов:

- сканирование компонентов (component scanning) – механизм, с помощью которого Spring обнаруживает и создает экземпляры компонентов;
- автосвязывание (autowiring) – механизм, с помощью которого Spring автоматически «удовлетворяет» зависимости компонентов (to satisfy a dependency).

Совместная работа этих механизмов обеспечивает минимальное явное конфигурирование контейнера.

Перепишем наш код для использования автоматического связывания. Для того, чтобы механизм сканирования компонентов обнаружил наши классы-бины, необходимо пометить их с помощью аннотации @Component.

```java
@Component
public class TwitterSender implements Sender {...}

@Component
public class User {...}
```

Тот участок кода, где контейнеру необходимо осуществить внедрение зависимости, аннотируется с помощью аннотации @Autowired. В рамках данного примера мы решили, что внедрение зависимости происходит в методе (method injection). Обратите внимание, что это не обязательно должен быть сеттер, хотя это крайне желательно

```java
@Component
public class User {

    private Sender sender;

    @Autowired
    public void setSender(Sender sender) {
        this.sender = sender;
    }
}
```

Когда мы осуществляли конфигурацию с помощью Java-класса, мы явно указывали классы компонентов и явно создавали объекты бинов.

Однако Spring способен автоматически отсканировать пакеты проекта, обнаружить бины и создать их экземпляры. Этот механизм называется сканирование компонентов (component scanning). По умолчанию, механизм сканирования компонентов отключен. Чтобы его включить, вернемся в конфигурационный класс AppConfig и укажем аннотацию @ComponentScan перед объявлением класса.

```java
@Configuration
@ComponentScan("app")
public class AppConfig {}
```

Прежде всего, удалим из класса AppConfig написанные ранее методы - они теперь не нужны.

Также обратите внимание, что в скобках я указал базовый пакет, где необходимо осуществить сканирование. Механизм сканирования компонентов будет искать компоненты в этом и в дочернем пакетах. Также вы можете указать параметр basePackages и перечислить пакеты для сканирования.

Запустим приложение и убедимся, что автоматическое связывание работает корректно.

```plain
июл 01, 2018 4:13:21 PM org.springframework.context.support.AbstractApplicationContext prepareRefresh
INFO: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@2d8e6db6: startup date [Sun Jul 01 16:13:21 EEST 2018]; root of context hierarchy

Tweet: Hello! is sending to Nick
```

### Разрешение зависимости (Dependency Resolution)

Использование автоматического связывания (связывание компонентов реализуется с помощью механизмов Spring) может привести к ситуации, когда будет существовать несколько бинов, которые могут быть использованы для связывания.

Пока что у нас был только один класс, который реализовывал интерфейс Sender. А что, если их будет два? Создадим класс EmailSender

```java
@Component
public class EmailSender implements Sender {

    public void sendMessage(String message, String target) {
        System.out.println("Email: " + message + " to: " + target);
    }
}
```

Запустим приложение

```plain
	at app.Main.main(Main.java:28)
Caused by: org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'app.model.Sender' available: expected single matching bean but found 2: emailSender,twitterSender
```

Сообщение при исключении четко описывает проблему: есть два бина, которые можно внедрить в класс User и Spring не знает, какой из них следует внедрить и закрывается с исключением.

Чтобы избавиться от данной проблемы, можно дать указания контейнеру, какой из компонентов следует выбрать в том или ином случае (ищите информацию по аннотации @Qualifier).

В нашем примере воспользуемся аннотацией @Conditional, чтобы решить проблему нескольких кандидатов на связывание.

Аннотация @Conditional перед объявлением класса бина означает, что бин будет доступен для регистрации в контейнере только, когда будет удовлетворено некоторое условие. В нашем случае, для каждого кандидата мы создадим отдельный класс - реализацию интерфейса Condition, в котором реализуем специальный метод. Если метод вернет true, значит условие выполнено и компонент можно зарегистрировать.
Прежде всего воспользуемся механизмом properties в Java. Создадим ресурс app.properties с содержимым

```plain
sender.type = email
```

В классе Main создадим объект Properties и загрузим файл

```java
public class Main {

    public static final Properties config = new Properties();

    static {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try (InputStream resourceStream = loader.getResourceAsStream("app.properties")) {
            config.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

Теперь у нас есть публичное статическое поле config, в котором хранятся свойства.

Создадим классы условий

```java
public class TwitterSenderCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return Main.config.getProperty("sender.type").matches("twitter");
    }
}

public class EmailSenderCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return Main.config.getProperty("sender.type").matches("email");
    }
}
```

 В классах компонентов укажем аннотацию @Conditional и класс условия

```java
@Component
@Conditional(value = TwitterSenderCondition.class)
public class TwitterSender implements Sender {...}

@Component
@Conditional(value = EmailSenderCondition.class)
public class EmailSender implements Sender {...}
```

Теперь запустим приложение

```plain
июл 01, 2018 4:57:53 PM org.springframework.context.support.AbstractApplicationContext prepareRefresh
INFO: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@2d8e6db6: startup date [Sun Jul 01 16:57:53 EEST 2018]; root of context hierarchy

Email: Hello! to: Nick
```

Если мы изменим в app.properties значение с email на twitter и снова запустим приложение, то получим

```plain
июл 01, 2018 4:58:56 PM org.springframework.context.support.AbstractApplicationContext prepareRefresh
INFO: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@2d8e6db6: startup date [Sun Jul 01 16:58:56 EEST 2018]; root of context hierarchy

Tweet: Hello! is sending to Nick
```

Таким образом, проблема нескольких кандидатов решена.