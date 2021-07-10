package util;

import java.util.List;

public class BookInfo {
    /*Ovde cemo da cuvamo neke opste informacije koje su manje vise potrebne samo za neki prikaz*/
    private String name;
    private Author author;
    private String releaseDate;
    private String genre;
    private String literarMovement;
    List<String> awards;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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
}
