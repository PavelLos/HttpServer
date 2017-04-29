package com.pavel.controller;

import com.pavel.constants.HttpMethod;
import com.pavel.view.ServerWindow;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RequestHandler {
    /**
     * List, содержащий весь запрос
     */
    private List<String> inputRequest;
    /**
     * Map, содержащий отфармотированые данные запроса: header и его значение
     */
    private Map<String, String> httpInfo;
    /**
     * Содержит пользовательские данные, полученные из запроса
     */
    private String requestParameters;
    /**
     * Запрашиваемый url
     */
    private String url;
    /**
     * Запрашиваемый http метод
     */
    private String method;
    /**
     * true, если входной запрос корректный и false, если неверен
     */
    private boolean correctRequest;
    /**
     * @see Logger#getLogger
     */
    private static Logger log = Logger.getLogger(RequestHandler.class);

    public RequestHandler(final InputStream input) {
        inputRequest = new ArrayList<>();
        httpInfo = new HashMap<>();
        correctRequest = false;
        readRequest(input);
    }


    /**
     * Метод, читающий входящий запрос.
     *
     * @param input, содержащий поток байт входного запроса.
     */
    public void readRequest(final InputStream input) {
        getInputRequest(input);
        if (inputRequest.size() != 0) {
            url = getRequestURI();
            method = getRequestMethod();
            requestParameters = getRequestParameters();
        }
    }


    /**
     * Метод, читающий входящий запрос.
     *
     * @param input, содержащий поток байт входного запроса.
     */
    private void getInputRequest(final InputStream input) {
        int size = 0;
        try {
            size = input.available();
            byte[] inputBytes = new byte[size];
            input.read(inputBytes);
            String str = new String(inputBytes);
            log.info(str);
            ServerWindow.getInstance().printInfo(str);
            String[] strings = str.split("\\r\\n");
            for (String s : strings) {
                inputRequest.add(s);
            }
            inputRequest.remove("");
        } catch (IOException e) {
            log.error("Can't read input request");
        }
    }


    /**
     * Метод, получающий URI из запроса клиента
     *
     * @return url запроса
     */
    private String getRequestURI() {
        if (inputRequest.size() != 0) {
            String url = HttpParser.getUrl(inputRequest.get(0));
            log.info("Client request url: " + url);
            return url;
        }
        log.error("Client request url is wrong");
        return null;
    }

    /**
     * Метод, формирующий headers из запроса клиента.
     *
     * @param input - входящий массив строк запроса
     * @return список, содержащий header и его значение.
     */
    public Map<String, String> getRequestHeaders(List<String> input) {
        for (String request : input) {
            if (HttpParser.getSplitRequest(request) != null) {
                httpInfo.putAll(HttpParser.getSplitRequest(request));
            }
        }
        return httpInfo;
    }

    /**
     * Метод, получающий тип Http запроса
     *
     * @return метод http запроса
     */
    private String getRequestMethod() {
        String method = null;
        method = HttpParser.getMethod(inputRequest.get(0));
        log.info("Client request method: " + method);
        return method;
    }

    /**
     * Метод, формирующий все параметры из запрсо клиента
     *
     * @return requestParameters - пользовательские данные
     */
    private String getRequestParameters() {
        if (method.equals(HttpMethod.POST.getMethod())) {
            //requestParameters = HttpParser.getValues(inputRequest.get(inputRequest.size() - 1));
            if (!inputRequest.get(inputRequest.size() - 1).contains("Cookie"))
                return inputRequest.get(inputRequest.size() - 1);
        }
        return "";
    }

    /**
     * Метод, возвращающий параметры запроса в виде строки.
     *
     * @return parameters - пользовательские данные в виде строки
     */
    public String getRequestParametersToString() {
        /*StringBuilder parameters = new StringBuilder();
        for (int i = 0; i < requestParameters.size(); i++) {
            parameters.append(requestParameters.get(i));
            parameters.append("\r\n");
        }
        return parameters.toString();*/
        return requestParameters;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    /**
     * Метод, проверяющий корректность входящего запроса и возвращающий true или false
     *
     * @return false - если входные данные неверны, и true - если не верны.
     */
    public boolean isCorrectRequest() {
        if (correctRequest)
            return true;
        if (inputRequest != null)
            if (inputRequest.size() != 0)
                if (url != null)
                    if (method != null) {
                        correctRequest = true;
                        return true;
                    }
        return false;
    }
}
