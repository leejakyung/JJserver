package com.chatting.server.utils;

import com.google.gson.stream.JsonReader;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileReader;
import java.net.URL;

public class FileUtils {

    public static String getFilePath(String fileName){
        URL resource = FileUtils.class.getClassLoader().getResource(fileName);

        assert resource != null;

        return new File(resource.getPath()).toPath().toString();
    }

    public static File getFile(String fileName){
        URL resource = FileUtils.class.getClassLoader().getResource(fileName);

        assert resource != null;

        return new File(resource.getPath()).toPath().toFile();
    }

    public static Document getXmlDocument(String fileName){

        try{
            File file = getFile(fileName);

            return new SAXBuilder().build(file);
        } catch (Exception e){
            return null;
        }
    }

    public static JsonReader getJsonFile(String fileName){

        try{
            File file = getFile(fileName);
            JsonReader jsonReader = new JsonReader(new FileReader(file));

//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            Type type = new TypeToken<List>(){}.getType();
//            List list  = gson.fromJson(jsonReader, type);

            return jsonReader;

        } catch (Exception e){
            return null;
        }
    }
}
