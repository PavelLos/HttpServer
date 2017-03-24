package com.pavel.controller;

import com.pavel.constants.HttpStatus;
import com.pavel.constants.ServerPath;
import com.pavel.view.ServerWindow;
import org.apache.log4j.Logger;

import java.io.*;
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
     * @param url - запрашиваемый ресурс
     * @return сформированный ответ
     * @throws IOException
     */
    public byte[] createGetResponse(String url) throws IOException {
        String path = HttpParser.getPath(url);
        byte[] document;
        byte[] headers;
        if (checkPath(path)) {
            log.info("Request success: " + HttpStatus.STATUS_200);
            document = createDocument(path);
            headers = createHeaders(HttpStatus.STATUS_200.getConstant(),
                    document.length,
                    getContentType(path));
        } else {
            log.info("Request Error: " + HttpStatus.STATUS_404);
            document = createDocument(ServerPath.NOT_FOUND);
            headers = createHeaders(HttpStatus.STATUS_404.getConstant(),
                    document.length,
                    getContentType(path));
        }
        return getResponseByte(headers, document);
    }

    /**
     * Метод, формирующий ответ POST запроса
     * @param url - запрашиваемый ресурс
     * @return сформированный ответ
     * @throws IOException
     */
    public byte[] createPostResponse(String url, String documentText) throws IOException {
        String path = HttpParser.getPath(url);
        byte[] document;
        byte[] headers;
        if (checkPath(path)) {
            log.info("Request success: " + HttpStatus.STATUS_200);
            document = createDocument(path, documentText);
            headers = createHeaders(HttpStatus.STATUS_200.getConstant(),
                    document.length,
                    getContentType(path));
        } else {
            log.info("Request Error: " + HttpStatus.STATUS_404);
            document = createDocument(ServerPath.NOT_FOUND);
            headers = createHeaders(HttpStatus.STATUS_404.getConstant(),
                    document.length,
                    getContentType(path));
        }
        return getResponseByte(headers, document);
    }

    /**
     * Метод, формирующий ответ HEAD запроса
     * @param url - запрашиваемый ресурс
     * @return сформированный ответ
     * @throws IOException
     */
    public byte[] createHeadResponse(String url) throws IOException {
        String path = HttpParser.getPath(url);
        byte[] document;
        byte[] headers;
        if (checkPath(path)) {
            document = createDocument(path);
            headers = createHeaders(HttpStatus.STATUS_200.getConstant(),
                    document.length,
                    getContentType(path));
            log.info("Request success: " + HttpStatus.STATUS_200);
        } else {
            document = createDocument(ServerPath.NOT_FOUND);
            headers = createHeaders(HttpStatus.STATUS_404.getConstant(),
                    document.length,
                    getContentType(path));
            log.info("Request Error: " + HttpStatus.STATUS_404);
        }

        return headers;
    }

    /**
     * Метод, формирующий заголовки для ответа сервера
     * @param status - статус ответа
     * @param length - длина отправляемого ресурса
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
        ServerWindow.getInstance().printInfo(responseHeader);
        return responseHeader.getBytes();
    }

    /**
     * Формирование содержания документа для ответа
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
     * Формирование содержания документа для ответа
     * @param path - путь к документу
     * @param documentText - содержание для ответа
     * @return содержание для ответа
     */
    private byte[] createDocument(String path, String documentText) {
        StringBuilder document = new StringBuilder();
        byte[] fileContent = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String fileString;
        while (true) {
            try {
                fileString = reader.readLine();
                if (fileString == null || fileString.isEmpty())
                    break;
                document.append(fileString);
                if (fileString.equals("<body>"))
                    document.append(documentText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fileContent = document.toString().getBytes();
        return fileContent;
    }

    /**
     * Формирование даты для заголовка ответа сервера
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
     * @param headers - заголовки ответа сервера
     * @param document - содержание документа для ответа
     * @return
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
     * @param path
     * @return
     */
    private boolean checkPath(String path) {
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

    /**
     * Формирование заголовка Content-Type для ответа сервера
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
