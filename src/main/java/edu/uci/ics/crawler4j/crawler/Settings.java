package edu.uci.ics.crawler4j.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Settings 
{
    public final static String DEFAULT = System.getProperty( "user.dir" ) + "/crawler4j.properties";
    private static Properties properties;

    public Settings() throws IOException {
        properties = new Properties();
        load();
    }

    public static void load() throws IOException{
        load(DEFAULT);
    }
    
    public static void load(String filename) throws IOException{
        properties.load(new FileInputStream(new File(filename)));
    }
    
    public String getProperty (String key){
        return properties.get(key).toString();
    }
}
