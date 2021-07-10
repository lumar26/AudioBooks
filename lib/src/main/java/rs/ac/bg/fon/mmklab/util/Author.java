package rs.ac.bg.fon.mmklab.util;

import java.util.List;

public class Author {
    private String name;
    private int yearOfBirth;
    private int yearOfDeath;
    private String literarMovement;
    private List<String> awards;
    private List<String> books;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public int getYearOfDeath() {
        return yearOfDeath;
    }

    public void setYearOfDeath(int yearOfDeath) {
        this.yearOfDeath = yearOfDeath;
    }

    public String getLiterarMovement() {
        return literarMovement;
    }

    public void setLiterarMovement(String literarMovement) {
        this.literarMovement = literarMovement;
    }

    public List<String> getAwards() {
        return awards;
    }

    public void setAwards(List<String> awards) {
        this.awards = awards;
    }

    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }
}
