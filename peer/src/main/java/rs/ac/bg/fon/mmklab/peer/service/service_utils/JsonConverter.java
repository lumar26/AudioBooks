package rs.ac.bg.fon.mmklab.peer.service.service_utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import rs.ac.bg.fon.mmklab.book.AudioBook;

import java.util.List;

public class JsonConverter {
    public static String bookListToJSON(List<AudioBook> bookList){
        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            return mapper.writeValueAsString(bookList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.err.println("Greska (bookListToJSON): problem pri procesiranju json-a. Pretvaranje liste u json string");
        }

//        ovo drugacije da se resi
        return "";
    }
}
