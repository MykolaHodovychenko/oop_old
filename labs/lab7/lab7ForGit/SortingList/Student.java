package ua.opu;

//класс Student для SortingList

public class Student {
    private String name;
    private String lastName;
    private double avgMark; // Средний балл

    public Student(String name, String lastName, double avgMark) {
        this.name = name;
        this.lastName = lastName;
        this.avgMark = avgMark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getAvgMark() {
        return avgMark;
    }

    public void setAvgMark(double avgMark) {
        this.avgMark = avgMark;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(lastName);
        builder.append(" ");
        builder.append(name);
        builder.append(", средний балл: ").append(avgMark);

        return builder.toString();
    }
}
