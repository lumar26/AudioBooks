package rs.ac.bg.fon.mmklab.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import rs.ac.bg.fon.mmklab.book.AudioBook;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class JsonConverter {
    public static String bookListToJSON(List<AudioBook> bookList){
        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            return mapper.writeValueAsString(bookList);
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
            System.err.println("Greska (bookListToJSON): problem pri procesiranju json-a. Pretvaranje liste u json string");
        }

//        ovo drugacije da se resi
        return "";
    }

    public static String requestForBooksToJSON(String requestMessage){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            return mapper.writeValueAsString(requestMessage);
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
            System.err.println("Greska (requestForBooksToJSON): problem pri procesiranju json-a. Pretvaranje stringa u json string");

        }

        return ""; // i ovo drugacije da se odradi, ne bi smelo da ostane ovako
    }

    public static List<AudioBook> jsonToBookList(String jsonInput) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);

//        veliko pitanje dal radi ovo
        return mapper.readValue(jsonInput, new TypeReference<>() {
        });

    }

    public static  boolean isValidListOfBooks(String bookList) throws FileNotFoundException {
//        ovde bi trebalo da se uporedi lista knjiga sa json semom
        File schemaFile = new File("/home/lumar26/git/AudioBooks/lib/src/main/java/rs/ac/bg/fon/mmklab/util/schema/AudioBooksSchema.json");
        JSONTokener schemaData = new JSONTokener(new FileInputStream(schemaFile));
        JSONObject schema = new JSONObject(schemaData);

//        ono sto validiramo, u pitanju je niz objekata pa mora da bude JSONArray
        JSONArray test = new JSONArray(bookList);

        Schema schemaValidator = SchemaLoader.load(schema);

        try{
            schemaValidator.validate(test); //ako json nije validan onda baca exception
        }catch (ValidationException e){
            return false;
        }

        return true;
    }
}
