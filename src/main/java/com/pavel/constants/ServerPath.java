package com.pavel.constants;


import com.pavel.configurations.Configurations;

public class ServerPath {
    //public static String PATH = "/pages";
    public static String PATH = Configurations.getInstance().getPagesPath();
    public static String NOT_FOUND = PATH + "/status404.html";
    public static String DEFAULT_PATH = PATH + "/web.html";
    public static final String CONFIG_DIRECTORY = "configs";
    public static final String CONFIG_PATH = "/config.json";
    public static final String CONFIG_PATH_LOG = "/log4j.properties";
    // static String configDirectoryPath = ConfigReader.getConfigDirectoryPath();


    public ServerPath() {

    }
}
