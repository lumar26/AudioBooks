package rs.ac.bg.fon.mmklab.book;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonSerialize
public class BookInfo implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookInfo)) return false;
        BookInfo bookInfo = (BookInfo) o;
        return getName().equals(bookInfo.getName()) && getAuthor().equals(bookInfo.getAuthor());
    }

}
