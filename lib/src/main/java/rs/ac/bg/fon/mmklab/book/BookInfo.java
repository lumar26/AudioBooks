package rs.ac.bg.fon.mmklab.book;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
public class BookInfo {
    /*Ovde cemo da cuvamo neke opste informacije koje su manje vise potrebne samo za neki prikaz*/
    private String name;
    private String author;

    public BookInfo() {
    }

    public BookInfo(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    @Override
    public String toString() {
        return "\nBookInfo{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
