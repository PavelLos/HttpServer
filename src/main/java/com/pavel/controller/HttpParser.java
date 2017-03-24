package com.pavel.controller;

import com.pavel.constants.ServerPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Класс для проверки входных запросов от клиента, а так же выделяющий необходимые заголовки для сервера
 */
public class HttpParser {
    /**
     * константа содержащая строку разделитель для запросов
     *
     * @value :\\s
     */
    private static final String REQUEST_HEADER_SPLIT = ":\\s";
    /**
     * константа содержащая регулярное выражение для запроса серверу от клиента
     *
     * @value ^[A-Za-z-]+:\s.*$
     */
    private static final String REQUEST_HEADER = "^[A-Za-z-]+:\\s.*$";
    /**
     * константа содержащая регулярное выражение для запроса URL
     *
     * @value ^(GET|POST|HEAD).+
     */
    private static final String REQUEST_URL = "^(GET|POST|HEAD).+";
    /**
     * константа содержащая регулярное выражение для выбора метода для запроса
     *
     * @value (GET|POST|HEAD).+
     */
    private static final String REQUEST_METHOD = "(GET|POST|HEAD).+";

    /**
     * шаблон для ругулярного выражения
     *
     * @see HttpParser#REQUEST_HEADER_SPLIT
     */
    private static final Pattern patternSplit = Pattern.compile(REQUEST_HEADER_SPLIT);
    /**
     * шаблон для ругулярного выражения
     *
     * @see HttpParser#REQUEST_HEADER
     */
    private static final Pattern patternHeader = Pattern.compile(REQUEST_HEADER);
    /**
     * шаблон для ругулярного выражения
     *
     * @see HttpParser#REQUEST_URL
     */
    private static final Pattern patternURL = Pattern.compile(REQUEST_URL);
    /**
     * шаблон для ругулярного выражения
     *
     * @see HttpParser#REQUEST_METHOD
     */
    private static final Pattern patternMETHOD = Pattern.compile(REQUEST_METHOD);

    /**
     * Метод, обрабатывающий входной запрос для получения списка headers и их значений.
     * @param request запрос клиента серверу
     * @return возвращает название всех запросов и их содержания
     */
    public static Map getSplitRequest(final String request) {
        if (patternHeader.matcher(request).matches()) {
            String[] strings = patternSplit.split(request);
            Map httpInfo = new HashMap<String, String>();
            httpInfo.put(strings[0], strings[1]);
            return httpInfo;
        }
        return null;
    }

    /**
     * Метод, получающий из запроса url.
     * @param firstStrFromRequest строка содержащая URL
     * @return если соответствует протоколу запроса HTTP, то URL. Иначе null
     */
    public static String getUrl(final String firstStrFromRequest) {
        if (patternURL.matcher(firstStrFromRequest).matches()) {
            return firstStrFromRequest.substring(firstStrFromRequest.indexOf(" ") + 1, firstStrFromRequest.indexOf(" ", 5));
        }
        return null;
    }

    /**
     * Метод, получающий из запроса http метод
     * @param request строка содержащая URL
     * @return если соответствует протоколу запроса HTTP, то возвращает соответствуюший метод. Иначе null
     */
    public static String getMethod(final String request) {
        if (patternMETHOD.matcher(request).matches()) {
            return request.substring(0, request.indexOf(" "));
        }
        return null;
    }

    /**
     * Метод, обрабатываюший полученную url и возвращаюший путь к ресурсу.
     * @param url строка URL
     * @return путь к файлу
     */
    public static String getPath(String url) {
        String path = ServerPath.PATH;
        int i = url.indexOf("?");
        if (i > 0) url = url.substring(0, i);
        i = url.indexOf("#");
        if (i > 0) url = url.substring(0, i);

        if (url.equals("/")) {
            return ServerPath.DEFAULT_PATH;
        }
        char a;
        for (i = 0; i < url.length(); i++) {
            a = url.charAt(i);
            path = path + a;
        }
        return path;
    }

    /**
     * Метод, получающий пользовательские данные из запроса.
     * @param request параметр запроса.
     * @return входные параметры
     */
    public static List<String> getValues(String request) {
        List<String> parameters = new ArrayList<>();
        String stringOfValues = request;
        if (request.contains("?")) {
            stringOfValues = request.substring(request.indexOf("?"));
        }
        String[] values = stringOfValues.split("&");
        for (String str : values) {
            parameters.add(str);
        }
        return parameters;
    }
}
