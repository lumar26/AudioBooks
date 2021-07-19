package rs.ac.bg.fon.mmklab.book;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Objects;

@JsonSerialize
public class AudioBook implements Serializable {
    private AudioDescription audioDescription;
    private BookInfo bookInfo;
    private BookOwner bookOwner;

    public AudioBook() {
    }

    public AudioBook(AudioDescription audioDescription, BookInfo bookInfo, BookOwner bookOwner) {
        this.audioDescription = audioDescription;
        this.bookInfo = bookInfo;
        this.bookOwner = bookOwner;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public BookOwner getBookOwner() {
        return bookOwner;
    }

    public void setBookOwner(BookOwner bookOwner) {
        this.bookOwner = bookOwner;
    }

    public AudioDescription getAudioDescription() {
        return audioDescription;
    }

    public void setAudioDescription(AudioDescription audioDescription) {
        this.audioDescription = audioDescription;
    }

    @Override
    public String toString() {
        return "\nAudioBook:" + this.audioDescription.toString() + this.bookInfo.toString() + this.bookOwner.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AudioBook)) return false;
        AudioBook audioBook = (AudioBook) o;
        return getAudioDescription().equals(audioBook.getAudioDescription()) && getBookInfo().equals(audioBook.getBookInfo()) && getBookOwner().equals(audioBook.getBookOwner());
    }

}
