package com.pavel.controller;

import com.pavel.configurations.Configurations;
import com.pavel.constants.ServerPath;
import com.pavel.view.ServerWindow;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Класс, отвечающий за вызов подпрограмм
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
     * Получение объекта
     *
     * @return объект класса StartJarFile
     */
    public static StartJarFile getInstance() {
        return instance;
    }

    /**
     * Создание объекта
     */
    private StartJarFile() {
        process = null;
    }

    /**
     * Запуск сторонней java-программы
     *
     * @param path         - путь к запрашиваемому документу
     * @param documentText - необходимые данные для отображения на странице
     */
    private void startProcess(String path, String documentText) {
        try {
            process = createProcess(path, documentText).start();
        } catch (IOException e) {
            log.error("Запуск jar не удался");
            ServerWindow.getInstance().printInfo("Запуск jar не удался");
        }
        inputStream = process.getInputStream();
    }

    /**
     * Создание процесса для запуска сторонней java-программы
     *
     * @param path         - путь к запрашиваемому документу
     * @param documentText - необходимые данные для отображения на странице
     * @return processBuilder - процесс, готовый для запуска
     */
    private ProcessBuilder createProcess(String path, String documentText) {
        String[] command = {"java", "-jar", checkFile(path), path, documentText};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(new File(Configurations.getInstance().getPathToServer() + ServerPath.JAR_PATH));
        return processBuilder;
    }

    /**
     * Чтение созданной сраницы.
     *
     * @return document - содержание документа для отправки клиенту
     */
    private byte[] readCreatedFile() {
        String line = "";
        StringBuilder document = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((line = reader.readLine()) != null) {
                if (line != null)
                    document.append(line);
            }
        } catch (IOException e) {
            log.error("Ошибкачтения документа");
            ServerWindow.getInstance().printInfo("Ошибкачтения документа");
        }
        return document.toString().getBytes();
    }

    /**
     * Получение запршиваемого документа для отправки клиенту
     *
     * @param path         - запрашиваемый ресурс
     * @param documentText - содержание документа
     * @return документ в виде массива байт
     */
    public byte[] getDocument(String path, String documentText) {
        startProcess(Configurations.getInstance().getPathToServer() + path, documentText);
        return readCreatedFile();
    }

    /**
     * Проверка наличия java программы для соответствующего запроса
     *
     * @param path - путь к запрашиваемому документу
     * @return jarName - необходимая программа
     */
    private String checkFile(String path) {
        String jarName = "";
        if (path.contains("post_show.html")) {
            jarName = "post_example-1.0-SNAPSHOT.jar";
        }
        return jarName;
    }
}
