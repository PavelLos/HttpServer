package com.pavel.configurations;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by pasha on 18.03.2017.
 */
public class ConfigReader {

    public ConfigReader() {

    }

    public static String getPORT(String configPath) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(getConfigString(configPath));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        return jsonObj.get("PORT").toString();
    }

    public static String getPagesDirectory(String configPath) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(getConfigString(configPath));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        return jsonObj.get("PAGES_PATH").toString();
    }

    private static String getConfigString(String configPath) {
        StringBuilder string = new StringBuilder();
        String ssss = configPath;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(configPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                String str = reader.readLine();
                if (str == null || str.isEmpty()) {
                    break;
                }
                string.append(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return string.toString();
    }


}
