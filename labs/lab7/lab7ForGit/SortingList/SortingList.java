package ua.opu;
// изменения на строчках 130 - 162
// переписано с помощью лямбда- выражений
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Comparator;

/**
 * Данный класс наследуется от стандартного класса Application.
 * Класс Application отвечает за работы FX-приложения.
 * <p>
 * Если вы пишете программу с использованием библиотеки javaFX, то вы
 * должны создать свой класс, который наследутся от класса Application
 */
public class SortingList extends Application {

    // Список студентов.
    // Интерфейс ObservableList похож на интерфейс List, но
    // имеет еще возможность оповещать остальные объекты о том, что он изменился
    private ObservableList<Student> students;

    /**
     * Этот метод запускается, когда запускается ваше приложение
     * Stage - класс "подмостки". Считайте, что это некоторое подобие окна приложение.
     * Просто в JavaFX окно называется "подмостками", как театральные подмостки.
     * <p>
     * Самые первые "подмостки" (первое окно приложения)
     * создает за вас система и передает его вам как входной параметр
     * если вы захотите создать дополнительные "подмостки" - вы должны сделать это сами
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Заголовок окна
        primaryStage.setTitle("Список студентов");

        // Заполняем список студентов данными
        students = populateList();

        // Это вертикальный ряд с элементами
        final VBox vbox = new VBox();
        // Расстояние между элементами
        vbox.setSpacing(5);
        // Устанавливаем padding в 5 пикселей по всем направлениям
        vbox.setPadding(new Insets(5));
        vbox.setAlignment(Pos.CENTER);

        // Это виджет списка, в нем можно отображать список с данными
        // При создании виджета списка передаем ему список (students) со студентами
        // Для каждого студента из списка вызывается метод toString()
        // и выводится на экран
        final ListView<Student> listView = new ListView<>(students);
        // Предпочитаемые размеры виджета списка
        listView.setPrefSize(400, 240);

        // Настраиваем горизонтальный ряд кнопок
        final HBox hbox = setButtons();

        // Добавляем сверху виджет списка, после чего добавляем ряд с кнопками
        vbox.getChildren().addAll(listView, hbox);

        // Это класс "Сцена", который является контейнером для всех остальных виджетов
        // На "сцене" расположены кнопки, поля, переключатели и т.д.
        // В нашем случае, на сцене у нас раположен вертикальный ряд элементов
        // где сверху будет список со студентами, а снизу - ряд с кнопками
        Scene scene = new Scene(vbox);
        // Добавляем объект сцены в Stage. Сцены можно менять простым методом, что
        // позволяет очень просто менять содержимое окон
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        // Показываем "подмостки"
        primaryStage.show();
    }

    /**
     * Заполняем список данными вручную
     *
     * @return заполненный список со студентами
     */
    private ObservableList<Student> populateList() {
        Student student1 = new Student("Иван", "Иванов", 75);
        Student student2 = new Student("Петр", "Владимиров", 92);
        Student student3 = new Student("Яша", "Абрикосов", 61);
        Student student4 = new Student("Гриша", "Якорев", 88);

        // Класс ObservableArrayList очень похож на ArrayList,
        // но позволяет оповещать другие классы о том, что он
        // изменился
        return FXCollections.observableArrayList(
                student1, student2, student3, student4);
    }

    /**
     * Настраиваем кнопки. Здесь должен быть ваш код
     *
     * @return горизонтальный ряд с настроенными кнопками
     */
    private HBox setButtons() {
        // Кнопка в JavaFX имеет класс Button
        final Button sortByNameButton = new Button("Сортировать по имени");
        final Button sortByLastNameButton = new Button("Сортировать по фамилии");
        final Button sortByMarkButton = new Button("Сортировать по оценке");

        // Блок кода ниже позволяет кнопкам растягиваться в ширину, чтобы занять
        // все пространство HBox, причем кнопки будут одинакового размера
        HBox.setHgrow(sortByNameButton, Priority.ALWAYS);
        HBox.setHgrow(sortByLastNameButton, Priority.ALWAYS);
        HBox.setHgrow(sortByMarkButton, Priority.ALWAYS);
        sortByNameButton.setMaxWidth(Double.MAX_VALUE);
        sortByLastNameButton.setMaxWidth(Double.MAX_VALUE);
        sortByMarkButton.setMaxWidth(Double.MAX_VALUE);

        //массив для хранения состояния нажатия кнопки (какой вид сортировки)
        final int[] sortType = {1, 1, 1};

        // Обработка нажатия на кнопку сортировки по имени
        sortByNameButton.setOnAction(event -> {
            students.sort((o1, o2) -> {
                if (o1 != null && o2 != null) {
                    return sortType[0] * o1.getName().compareTo(o2.getName());
                }
                return 0;
            });
            sortType[0] *= (-1);
        });

        // Обработка нажатия на кнопку сортировки по фамилии
        sortByLastNameButton.setOnAction(event -> {
            students.sort((o1, o2) -> {
                if (o1 != null && o2 != null) {
                    return sortType[1] * o1.getLastName().compareTo(o2.getLastName());
                }
                return 0;

            });
            sortType[1] *= (-1);
        });

        // Обработка нажатия на кнопку сортировки по среднему балу
        sortByMarkButton.setOnAction(event -> {
            students.sort((o1, o2) -> {
                if (o1 != null && o2 != null) {
                    return sortType[2] * (int) (o1.getAvgMark() - o2.getAvgMark());
                }
                return 0;
            });
            sortType[2] *= (-1);

        });
        // Создаем горизональный ряд
        HBox hb = new HBox();
        // Расстояние между элементами ряда
        hb.setSpacing(5);
        // Добавляем в ряд элементы. В нашем случае - кнопки
        hb.getChildren().addAll(sortByNameButton, sortByLastNameButton, sortByMarkButton);
        // Говорим, что элементы в ряду должны быть выровнены по центру
        hb.setAlignment(Pos.CENTER);

        return hb;
    }

    public static void main(String[] args) {
        // Метод запускает приложение
        launch(args);
    }
}
