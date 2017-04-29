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
     * List, ���������� ���� ������
     */
    private List<String> inputRequest;
    /**
     * Map, ���������� ���������������� ������ �������: header � ��� ��������
     */
    private Map<String, String> httpInfo;
    /**
     * �������� ���������������� ������, ���������� �� �������
     */
    private String requestParameters;
    /**
     * ������������� url
     */
    private String url;
    /**
     * ������������� http �����
     */
    private String method;
    /**
     * true, ���� ������� ������ ���������� � false, ���� �������
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
     * �����, �������� �������� ������.
     *
     * @param input, ���������� ����� ���� �������� �������.
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
     * �����, �������� �������� ������.
     *
     * @param input, ���������� ����� ���� �������� �������.
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
     * �����, ���������� URI �� ������� �������
     *
     * @return url �������
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
     * �����, ����������� headers �� ������� �������.
     *
     * @param input - �������� ������ ����� �������
     * @return ������, ���������� header � ��� ��������.
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
     * �����, ���������� ��� Http �������
     *
     * @return ����� http �������
     */
    private String getRequestMethod() {
        String method = null;
        method = HttpParser.getMethod(inputRequest.get(0));
        log.info("Client request method: " + method);
        return method;
    }

    /**
     * �����, ����������� ��� ��������� �� ������ �������
     *
     * @return requestParameters - ���������������� ������
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
     * �����, ������������ ��������� ������� � ���� ������.
     *
     * @return parameters - ���������������� ������ � ���� ������
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
     * �����, ����������� ������������ ��������� ������� � ������������ true ��� false
     *
     * @return false - ���� ������� ������ �������, � true - ���� �� �����.
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
