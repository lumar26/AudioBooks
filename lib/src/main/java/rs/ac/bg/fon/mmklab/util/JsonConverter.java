package rs.ac.bg.fon.mmklab.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
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
import java.util.ArrayList;
import java.util.List;

public class JsonConverter {

    public static <T> String toJSON(T obj){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
            System.err.println("Greska (toJSON): problem pri procesiranju json-a. Pretvaranje objekta/liste u json string");
        }

//        ovo drugacije da se resi
        return "";
    }

//    ovo ne radi kada se kao drugi parametar prosledjuje tip liste
    public static <T> T toOriginal(String json, Class<T> type)  {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);

//        veliko pitanje dal radi ovo
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            System.err.println("Greska (JsonConverter ---> toOriginal):  baca JsonProcessingException jer nece da prebaci iz json stringa u objekat ");
            e.printStackTrace();
        }
        return null;
    }


//  za dobijanje liste knjiga iz json stringa ne moze da se koristi metoda toOriginal, za sad nismo nasli nacin
    public static List<AudioBook> jsonToBookList(String jsonInput) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);

//        veliko pitanje dal radi ovo
        return mapper.readValue(jsonInput, new TypeReference<>() {
        });

    }

    public static boolean isValidListOfBooks(String bookList) throws FileNotFoundException {
//        ovde bi trebalo da se uporedi lista knjiga sa json semom
        File schemaFile = new File("lib/src/main/resources/schema/AudioBooksSchema.json");
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
