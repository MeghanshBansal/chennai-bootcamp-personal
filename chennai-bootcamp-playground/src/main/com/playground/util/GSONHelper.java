package main.com.playground.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;

public class GSONHelper {
    public static Gson instance = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .setPrettyPrinting()
            .setLenient()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls().disableHtmlEscaping().create();

    public static <T> T deserializeFromResource(String resourcePath, Class<T> classType) throws FileNotFoundException {
        return instance.fromJson(new FileReader(new File(GSONHelper.class.getClassLoader().getResource(resourcePath).getFile())), classType);
    }

    public static <T> T deserializeFromResource(String resourcePath, Type type) throws FileNotFoundException {
        return instance.fromJson(new FileReader(new File(GSONHelper.class.getClassLoader().getResource(resourcePath).getFile())), type);
    }


    public static <T> T deserialize(String content, Class<T> classType) {
        return instance.fromJson(content, classType);
    }

    public static String serialize(Object object) {
        return instance.toJson(object);
    }
}