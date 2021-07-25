package rs.ac.bg.fon.mmklab.util;

import org.junit.jupiter.api.Test;
import rs.ac.bg.fon.mmklab.book.BookInfo;

import static org.junit.jupiter.api.Assertions.*;

class JsonConverterTest {

    @Test
    void bookInfoToJson() {
        BookInfo t = new BookInfo("Misao", "Vladislav Petković Dis");
        System.out.println("Knjiga: " + t.toString() + "  ---  json: " + JsonConverter.bookInfoToJson(t));
    }
}