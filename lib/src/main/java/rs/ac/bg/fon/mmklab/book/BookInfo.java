package rs.ac.bg.fon.mmklab.book;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize
public class BookInfo implements Serializable {
    /*Ovde cemo da cuvamo neke opste informacije koje su manje vise potrebne samo za neki prikaz*/
    private String title;
    private String author;

    public BookInfo() {
    }

    public BookInfo(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
                "name='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookInfo)) return false;
        BookInfo bookInfo = (BookInfo) o;
        return getTitle().equals(bookInfo.getTitle()) && getAuthor().equals(bookInfo.getAuthor());
    }

}
