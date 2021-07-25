package rs.ac.bg.fon.mmklab.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import rs.ac.bg.fon.mmklab.book.AudioBook;
import rs.ac.bg.fon.mmklab.book.BookInfo;

import java.io.*;
import java.util.List;

public class JsonConverter {
    public static String bookListToJSON(List<AudioBook> bookList) {
        ObjectMapper mapper = new ObjectMapper();
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

    public static String bookInfoToJson(BookInfo bookInfo){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            System.out.println("BookInfo koji saljemo posiljaocu: " + mapper.writeValueAsString(bookInfo));
            return mapper.writeValueAsString(bookInfo);
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
            System.err.println("Greska (bookInfoToJson): problem pri procesiranju json-a. Pretvaranje BookInfo u json string");
        }

//        ovo drugacije da se resi
        return "";
    }


    public static List<AudioBook> jsonToBookList(String jsonInput) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);

//        veliko pitanje dal radi ovo
        return mapper.readValue(jsonInput, new TypeReference<>() {
        });

    }

    public static BookInfo jsonToBookInfo(String jsonInput) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);

//        veliko pitanje dal radi ovo
        return mapper.readValue(jsonInput, new TypeReference<>() {
        });
    }

    public static boolean isValidListOfBooks(String bookList) throws FileNotFoundException {
//        ovde bi trebalo da se uporedi lista knjiga sa json semom
        File schemaFile = new File("/home/lumar26/git/AudioBooks/lib/src/main/java/rs/ac/bg/fon/mmklab/util/schema/AudioBooksSchema.json");
        JSONTokener schemaData = new JSONTokener(new FileInputStream(schemaFile));
        JSONObject schema = new JSONObject(schemaData);
        JSONArray test = null;
        try {
//        ono sto validiramo, u pitanju je niz objekata pa mora da bude JSONArray
            test = new JSONArray(bookList);
        } catch (JSONException e) {
            System.err.println("Nesto lose sa pravljenjem json niza od liste knjiga");
        }
        Schema schemaValidator = SchemaLoader.load(schema);

        try {
            schemaValidator.validate(test); //ako json nije validan onda baca exception
        } catch (ValidationException e) {
            System.err.println("Greska (JsonConverter -> isValidListOfBooks): json string ne odgovoara datoj semi");
            return false;
        }

        return true;
    }

}
