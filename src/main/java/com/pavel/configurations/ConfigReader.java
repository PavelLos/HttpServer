package com.pavel.configurations;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * �����, �������� ��������������� ����
 */
public class ConfigReader {
    //private static String configString = getConfigString(Configurations.getInstance().getConfigPath());

    public ConfigReader() {

    }

    /**
     * �����, ���������� ���� �� ���������������� �����
     *
     * @param configPath - ���� � ���������������� �����
     * @return ����
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
     * �����, ���������� ����������, ���������� ������� �� ���������������� �����
     *
     * @param configPath - ���� � ���������������� �����
     * @return ���� � ����������
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
     * �����, ���������� ����������, ���������� jar-����� �� ���������������� �����
     *
     * @param configPath - ���� � ���������������� �����
     * @return ���� � ����������
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
     * �����, ������������ �������� jar - �����, �� �������������� ���������.
     * @param namePage ��� �������������� ���������
     * @return �������� Jar �����
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
     * �������� ������� jar - ����� ���
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
     * �����, �������� ��������������� ����
     *
     * @param configPath - ���� � ���������������� �����
     * @return ���������� �����
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
