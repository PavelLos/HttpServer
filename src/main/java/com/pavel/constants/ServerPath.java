package com.pavel.constants;


import com.pavel.configurations.Configurations;

/**
 * Перечесление путей по умолчанию
 */
public class ServerPath {
    public static String PAGE_PATH = Configurations.getInstance().getPagesPath();
    public static String JAR_PATH = Configurations.getInstance().getJarPath();
    public static String NOT_FOUND = PAGE_PATH + "/status404.html";
    public static String BAD_REQUEST = PAGE_PATH + "/status400.html";
    public static String NOT_ALLOWED_METHOD = PAGE_PATH + "/status405.html";
    public static String NOT_IMPLEMENTED = PAGE_PATH + "/status501.html";
    public static String DEFAULT_PATH = PAGE_PATH + "/web.html";
    public static final String CONFIG_DIRECTORY = "configs";
    public static final String CONFIG_PATH = "/config.json";
    public static final String CONFIG_PATH_LOG = "/log4j.properties";

    public ServerPath() {

    }
}
