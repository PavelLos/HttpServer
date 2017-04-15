package com.pavel.controller;

import com.pavel.configurations.ConfigReader;
import com.pavel.constants.HttpStatus;
import com.pavel.constants.ServerPath;
import com.pavel.view.ServerWindow;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class ResponseHandler {
    /**
     * @see Logger#getLogger
     */
    private static Logger log = Logger.getLogger(ResponseHandler.class);


    /**
     * Метод, формирующий ответ GET запроса
     *
     * @param url - запрашиваемый ресурс
     * @return сформированный ответ
     */
    public byte[] createGetResponse(String url) {
        String path = HttpParser.getPath(url);
        path = checkConfigPage(path);
        byte[] document;
        byte[] headers;
        if (checkPath(path)) {
            if (!checkJarPath(path)) {
                log.info("Request success: " + HttpStatus.STATUS_200);
                ServerWindow.getInstance().printInfo("Request success: " + HttpStatus.STATUS_200);
                document = createDocument(path);
                headers = createHeaders(HttpStatus.STATUS_200.getConstant(),
                        document.length,
                        getContentType(path));
            } else {
                return notAllowed(url);
            }
        } else {
            return notFound(url);
        }
        return getResponseByte(headers, document);
    }

    /**
     * Метод, формирующий ответ POST запроса
     *
     * @param url          - запрашиваемый ресурс
     * @param documentText - содержание документа
     * @return массив байт для ответа на запрос клиента
     */
    public byte[] createPostResponse(String url, String documentText) {
        String path = HttpParser.getPath(url);
        path = checkConfigPage(path);
        byte[] document;
        byte[] headers;
        if (checkPath(path)) {
            if (checkJarPath(path)) {
                log.info("Request success: " + HttpStatus.STATUS_200);
                ServerWindow.getInstance().printInfo("Request success: " + HttpStatus.STATUS_200);
                document = StartJarFile.getInstance().getDocument(path, documentText);
                headers = createHeaders(HttpStatus.STATUS_200.getConstant(),
                        document.length,
                        getContentType(path));
            } else {
                return notAllowed(url);
            }
        } else {
            return notFound(url);
        }
        return getResponseByte(headers, document);
    }

    /**
     * Метод, формирующий ответ HEAD запроса
     *
     * @param url - запрашиваемый ресурс
     * @return сформированный ответ
     */
    public byte[] createHeadResponse(String url) {
        String path = HttpParser.getPath(url);
        path = checkConfigPage(path);
        byte[] document;
        byte[] headers;
        if (checkPath(path)) {
            if (!checkJarPath(path)) {
                document = createDocument(path);
                headers = createHeaders(HttpStatus.STATUS_200.getConstant(),
                        document.length,
                        getContentType(path));
                log.info("Request success: " + HttpStatus.STATUS_200);
                ServerWindow.getInstance().printInfo("Request success: " + HttpStatus.STATUS_200);
            } else {
                return notAllowed(url);
            }
        } else {
            return notFound(url);
        }
        return headers;
    }

    private byte[] badRequest(String url) {
        String path = HttpParser.getPath(url);
        byte[] document;
        byte[] headers;
        log.info("Request Error: " + HttpStatus.STATUS_400);
        ServerWindow.getInstance().printInfo("Request Error: " + HttpStatus.STATUS_400);
        document = createDocument(ServerPath.BAD_REQUEST);
        headers = createHeaders(HttpStatus.STATUS_400.getConstant(),
                document.length,
                getContentType(path));
        return getResponseByte(headers, document);
    }

    public byte[] notImplemented(String url) {
        String path = HttpParser.getPath(url);
        byte[] document;
        byte[] headers;
        log.info("Request Error: " + HttpStatus.STATUS_501);
        ServerWindow.getInstance().printInfo("Request Error: " + HttpStatus.STATUS_501);
        document = createDocument(ServerPath.NOT_IMPLEMENTED);
        headers = createHeaders(HttpStatus.STATUS_501.getConstant(),
                document.length,
                getContentType(path));
        return getResponseByte(headers, document);
    }

    public byte[] notAllowed(String url) {
        String path = HttpParser.getPath(url);
        byte[] document;
        byte[] headers;
        log.info("Request Error: " + HttpStatus.STATUS_405);
        ServerWindow.getInstance().printInfo("Request Error: " + HttpStatus.STATUS_405);
        document = createDocument(ServerPath.NOT_ALLOWED_METHOD);
        headers = createHeaders(HttpStatus.STATUS_405.getConstant(),
                document.length,
                getContentType(path));
        return getResponseByte(headers, document);
    }

    private byte[] notFound(String url) {
        String path = HttpParser.getPath(url);
        byte[] document;
        byte[] headers;
        document = createDocument(ServerPath.NOT_FOUND);
        headers = createHeaders(HttpStatus.STATUS_404.getConstant(),
                document.length,
                getContentType(path));
        log.info("Request Error: " + HttpStatus.STATUS_404);
        ServerWindow.getInstance().printInfo("Request Error: " + HttpStatus.STATUS_404);
        return getResponseByte(headers, document);
    }

    /**
     * Метод, формирующий заголовки для ответа сервера
     *
     * @param status      - статус ответа
     * @param length      - длина отправляемого ресурса
     * @param contentType - тип отправляемого ресурса
     * @return список сформированных заголовков
     */
    private byte[] createHeaders(String status, int length, String contentType) {
        String responseHeader =
                "HTTP/1.1 " + status + "\r\n" +
                        "Date: " + createDate() + "\r\n" +
                        "Server: Http Server\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + length + "\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Last-modified: Mon, 15 Jun 2017 21:53:08 GMT\r\n" +
                        "\r\n";
        log.info(responseHeader);
        ServerWindow.getInstance().printInfo(responseHeader);
        return responseHeader.getBytes();
    }

    /**
     * Формирование содержания документа для ответа
     *
     * @param path - путь к объекту
     * @return содержание для ответа
     */
    private byte[] createDocument(String path) {
        InputStream file = null;
        byte[] fileContent = null;
        try {
            file = new FileInputStream(path);
            fileContent = new byte[file.available()];
            file.read(fileContent);
        } catch (IOException e) {
            log.error("Bad document creation");
        }
        return fileContent;
    }

    /**
     * Формирование даты для заголовка ответа сервера
     *
     * @return дата
     */

    private String createDate() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = dateFormatLocal.parse(dateFormatGmt.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(date);
    }

    /**
     * Формирование массива байт для ответа
     *
     * @param headers  - заголовки ответа сервера
     * @param document - содержание документа для ответа
     * @return response - массив байт для отправки клиенту
     */
    private byte[] getResponseByte(byte[] headers, byte[] document) {
        int hLength = headers.length;
        int dLength = document.length;
        byte[] response = new byte[hLength + dLength];
        System.arraycopy(headers, 0, response, 0, hLength);
        System.arraycopy(document, 0, response, hLength, dLength);
        return response;
    }

    /**
     * Проверка наличия документа по указываемому пути
     *
     * @param path - путь к файлу
     * @return true - если искомый документ существует, иначе false
     */
    private boolean checkPath(String path) {
        if (ConfigReader.checkPage(path.substring(path.lastIndexOf("/") + 1)))
            return true;
        try (InputStream inputStream = new FileInputStream(path)) {
            if (inputStream != null)
                return true;
            return false;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private String checkConfigPage(String path) {
        String pagePath = path.substring(0, path.lastIndexOf("/") + 1);
        if (ConfigReader.checkPage(path.substring(path.lastIndexOf("/") + 1))) {
            if (!ConfigReader.getJarNameByPage(path.substring(path.lastIndexOf("/") + 1)).contains(".jar"))
                return pagePath + ConfigReader.getJarNameByPage(path.substring(path.lastIndexOf("/") + 1));
        }
        return path;
    }

    private boolean checkJarPath(String path) {
        String page = path.substring(path.lastIndexOf("/") + 1);
        if (page.contains("."))
            page = page.substring(0, page.indexOf("."));
        if (ConfigReader.checkPage(page)) {
            if (ConfigReader.getJarNameByPage(page).contains(".jar"))
                return true;
        }
        return false;
    }

    /**
     * Формирование заголовка Content-Type для ответа сервера
     *
     * @param path - путь к запрашиваемому объекту
     * @return значение заголовка Content-Type
     */
    private String getContentType(String path) {
        int point = path.lastIndexOf(".");
        String contentType = "text/html";
        if (point > 0) {
            String ext = path.substring(point + 1);
            if (ext.equalsIgnoreCase("html"))
                contentType = "text/html";
            else if (ext.equalsIgnoreCase("css"))
                contentType = "text/css";
            else if (ext.equalsIgnoreCase("gif"))
                contentType = "image/gif";
            else if (ext.equalsIgnoreCase("jpg"))
                contentType = "image/jpeg";
            else if (ext.equalsIgnoreCase("jpeg"))
                contentType = "image/jpeg";
            else if (ext.equalsIgnoreCase("bmp"))
                contentType = "image/x-xbitmap";
            else if (ext.equalsIgnoreCase("ico"))
                contentType = "image/ico";
        }
        return contentType;
    }
}
