package com.pavel.configurations;

import com.pavel.Main;
import com.pavel.constants.ServerPath;

/**
 * �����, ���������� �� ��������� ������, ����������� � ��������������� �����.
 */
public class Configurations {
    /**
     * ���� � �������� �������.
     */
    private String pagesPath;
    /**
     * ���� � �������� jar-������.
     */
    private String jarPath;
    /**
     * ���� � ���������������� ��������
     */
    private String configDirectoryPath;
    /**
     * ����
     */
    private int port;
    /**
     * ���� � ����������������� log-�����
     */
    private String logPath;
    /**
     * ���� � � ���������������� �����
     */
    private String configPath;
    /**
     * ���� � � �������
     */
    private String pathToServer;

    private static Configurations ourInstance = new Configurations();

    public static Configurations getInstance() {
        return ourInstance;
    }

    /**
     * �������� ���������� ������
     *
     * @see Configurations
     */
    private Configurations() {
        configDirectoryPath = configDirectoryPath();
        logPath = configDirectoryPath + ServerPath.CONFIG_PATH_LOG;
        configPath = configDirectoryPath + ServerPath.CONFIG_PATH;
        pagesPath = ConfigReader.getPagesDirectory(configPath);
        jarPath = ConfigReader.getJarDirectory(configPath);
        port = Integer.parseInt(ConfigReader.getPORT(configPath));
    }

    public String getPagesPath() {
        return pagesPath;
    }

    public String getJarPath() {
        return jarPath;
    }

    public String getConfigDirectoryPath() {
        return configDirectoryPath;
    }

    public String getPathToServer() {
        return pathToServer;
    }

    public int getPort() {
        return port;
    }

    public String getLogPath() {
        return logPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    /**
     * ���������� ���� � �������������� ����������
     *
     * @return ���� ��������������� ����������
     */
    private String configDirectoryPath() {
        String pathJar = Main.class.getProtectionDomain().getCodeSource().getLocation().toString();
        if (pathJar.contains("http-server-1.0-SNAPSHOT")) {
            pathJar = pathJar.substring(pathJar.indexOf("/") + 1, pathJar.indexOf("http-server-1.0-SNAPSHOT"));
            pathToServer = pathJar;
            return pathJar + ServerPath.CONFIG_DIRECTORY;
        } else if (pathJar.contains("HttpServer")) {
            pathToServer = pathJar.substring(pathJar.indexOf("/") + 1, pathJar.indexOf("target"));
        }
        return ServerPath.CONFIG_DIRECTORY;
    }
}
