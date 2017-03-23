package com.pavel.configurations;

import com.pavel.Main;
import com.pavel.constants.ServerPath;

/**
 * Created by pasha on 21.03.2017.
 */
public class Configurations {
    private String pagesPath;
    private String configDirectoryPath;
    private String port;
    private String logPath;
    private String configPath;

    private static Configurations ourInstance = new Configurations();

    public static Configurations getInstance() {
        return ourInstance;
    }

    private Configurations() {
        configDirectoryPath = configDirectoryPath();
        System.out.println(configDirectoryPath);
        logPath = configDirectoryPath + ServerPath.CONFIG_PATH_LOG;
        System.out.println(logPath);
        configPath = configDirectoryPath + ServerPath.CONFIG_PATH;
        pagesPath = ConfigReader.getPagesDirectory(configPath);
        port = ConfigReader.getPORT(configPath);
    }

    public String getPagesPath() {
        return pagesPath;
    }

    public String getConfigDirectoryPath() {
        return configDirectoryPath;
    }

    public String getPort() {
        return port;
    }

    public String getLogPath() {
        return logPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    private String configDirectoryPath() {
        String pathJar = Main.class.getProtectionDomain().getCodeSource().getLocation().toString();
        if(pathJar.contains("/http-server-socket-1.0-SNAPSHOT.jar")) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            pathJar = pathJar.substring(pathJar.indexOf("/") + 1, pathJar.indexOf("http-server-socket-1.0-SNAPSHOT.jar"));
            return pathJar + ServerPath.CONFIG_PATH;
        }
        return ServerPath.CONFIG_DIRECTORY;
    }
}
