package com.pavel.controller;

import com.pavel.configurations.ConfigReader;
import com.pavel.configurations.Configurations;
import com.pavel.constants.ServerPath;
import com.pavel.view.ServerWindow;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * �����, ���������� �� ����� �����������
 */
public class StartJarFile {
    /**
     * @see Logger#getLogger
     */
    private static Logger log = Logger.getLogger(StartJarFile.class);
    /**
     * @see InputStream#InputStream()
     */
    private InputStream inputStream;
    /**
     * @see Process#Process()
     */
    private Process process;
    /**
     * @see StartJarFile#StartJarFile()
     */
    private static StartJarFile instance = new StartJarFile();

    /**
     * ��������� �������
     *
     * @return ������ ������ StartJarFile
     */
    public static StartJarFile getInstance() {
        return instance;
    }

    /**
     * �������� �������
     */
    private StartJarFile() {
        process = null;
    }

    /**
     * ������ ��������� java-���������
     *
     * @param path         - ���� � �������������� ���������
     * @param documentText - ����������� ������ ��� ����������� �� ��������
     */
    private void startProcess(String path, String documentText) {
        try {
            process = createProcess(path, documentText).start();
        } catch (IOException e) {
            log.error("������ jar �� ������");
            ServerWindow.getInstance().printInfo("������ jar �� ������");
        }
        inputStream = process.getInputStream();
    }

    /**
     * �������� �������� ��� ������� ��������� java-���������
     *
     * @param path         - ���� � �������������� ���������
     * @param documentText - ����������� ������ ��� ����������� �� ��������
     * @return processBuilder - �������, ������� ��� �������
     */
    private ProcessBuilder createProcess(String path, String documentText) {
        String page = path.substring(path.lastIndexOf("/") + 1);
        if (page.contains("."))
            page = page.substring(0, page.indexOf("."));
        else
            path = path + ".html";
        String[] command = {"java", "-jar", ConfigReader.getJarNameByPage(page),
                path, documentText};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(new File(Configurations.getInstance().getPathToServer() + ServerPath.JAR_PATH));
        return processBuilder;
    }

    /**
     * ������ ��������� �������.
     *
     * @return document - ���������� ��������� ��� �������� �������
     */
    private String readCreatedFile() {
        String line = "";
        StringBuilder document = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((line = reader.readLine()) != null) {
                if (line != null)
                    document.append(line);
            }
        } catch (IOException e) {
            log.error("������ ������ ���������");
            ServerWindow.getInstance().printInfo("������ ������ ���������");
        }
        return document.toString();
    }

    /**
     * ��������� ������������� ��������� ��� �������� �������
     *
     * @param path         - ������������� ������
     * @param documentText - ���������� ���������
     * @return �������� � ���� ������� ����
     */
    public String getDocument(String path, String documentText) {
        startProcess(Configurations.getInstance().getPathToServer() + path, documentText);
        return readCreatedFile();
    }
}
