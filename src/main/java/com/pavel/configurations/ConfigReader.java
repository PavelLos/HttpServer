package com.pavel.configurations;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Класс, читающий кофигурационный файл
 */
public class ConfigReader {
    //private static String configString = getConfigString(Configurations.getInstance().getConfigPath());

    public ConfigReader() {

    }

    /**
     * Метод, получающий порт из кофигурационного файла
     *
     * @param configPath - путь к кофигурационному файлу
     * @return порт
     */
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

    /**
     * Метод, получающий директорию, содержащую стрницы из кофигурационного файла
     *
     * @param configPath - путь к кофигурационному файлу
     * @return путь к директории
     */
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

    /**
     * Метод, получающий директорию, содержащую jar-файлы из кофигурационного файла
     *
     * @param configPath - путь к кофигурационному файлу
     * @return путь к директории
     */
    public static String getJarDirectory(String configPath) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(getConfigString(configPath));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        return jsonObj.get("JAR_PATH").toString();
    }

    /**
     * Метод, возвращающий название jar - файла, по запрашиваемому документу.
     * @param namePage имя запрашиваемого документа
     * @return название Jar файла
     */
    public static String getJarNameByPage(String namePage) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(getConfigString(Configurations.getInstance().getConfigPath()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        return jsonObj.get(namePage).toString();
    }

    /**
     * Проверка наличия jar - файла для
     * @param page
     * @return
     */
    public static boolean checkPage(String page){
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(getConfigString(Configurations.getInstance().getConfigPath()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        if (jsonObj.get(page) !=null)
            return true;
        return false;
    }

    /**
     * Метод, читающий кофигурационный файл
     *
     * @param configPath - путь к кофигурационному файлу
     * @return содержание файла
     */
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
