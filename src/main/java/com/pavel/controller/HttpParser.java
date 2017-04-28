package com.pavel.controller;

import com.pavel.constants.ServerPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * ����� ��� �������� ������� �������� �� �������, � ��� �� ���������� ����������� ��������� ��� �������
 */
public class HttpParser {
    /**
     * ��������� ���������� ������ ����������� ��� ��������
     */
    private static final String REQUEST_HEADER_SPLIT = ":\\s";
    /**
     * ��������� ���������� ���������� ��������� ��� ������� ������� �� �������
     */
    private static final String REQUEST_HEADER = "^[A-Za-z-]+:\\s.*$";
    /**
     * ��������� ���������� ���������� ��������� ��� ������� URL
     */
    private static final String REQUEST_URL = "^.+?\\s.+";
    /**
     * ��������� ���������� ���������� ��������� ��� ������ ������ ��� �������
     */
    private static final String REQUEST_METHOD = ".+?\\s.+";

    /**
     * ������ ��� ����������� ���������
     *
     * @see HttpParser#REQUEST_HEADER_SPLIT
     */
    private static final Pattern patternSplit = Pattern.compile(REQUEST_HEADER_SPLIT);
    /**
     * ������ ��� ����������� ���������
     *
     * @see HttpParser#REQUEST_HEADER
     */
    private static final Pattern patternHeader = Pattern.compile(REQUEST_HEADER);
    /**
     * ������ ��� ����������� ���������
     *
     * @see HttpParser#REQUEST_URL
     */
    private static final Pattern patternURL = Pattern.compile(REQUEST_URL);
    /**
     * ������ ��� ����������� ���������
     *
     * @see HttpParser#REQUEST_METHOD
     */
    private static final Pattern patternMETHOD = Pattern.compile(REQUEST_METHOD);

    /**
     * �����, �������������� ������� ������ ��� ��������� ������ headers � �� ��������.
     *
     * @param request ������ ������� �������
     * @return ���������� �������� ���� �������� � �� ����������
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
     * �����, ���������� �� ������� url.
     *
     * @param firstStrFromRequest ������ ���������� URL
     * @return url �������
     */
    public static String getUrl(final String firstStrFromRequest) {
        if (patternURL.matcher(firstStrFromRequest).matches()) {
            return firstStrFromRequest.substring(firstStrFromRequest.indexOf(" ") + 1, firstStrFromRequest.lastIndexOf(" "));
        }
        return "wrong_url";
    }

    /**
     * �����, ���������� �� ������� http �����
     *
     * @param request ������ ���������� URL
     * @return method �������
     */
    public static String getMethod(final String request) {
        if (patternMETHOD.matcher(request).matches()) {
            return request.substring(0, request.indexOf(" "));
        }
        return "wrong_method";
    }

    /**
     * �����, �������������� ���������� url � ������������ ���� � �������.
     *
     * @param url ������ URL
     * @return ���� � �����
     */
    public static String getPath(String url) {
        String path = ServerPath.PAGE_PATH;
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
     * �����, ���������� ���������������� ������ �� �������.
     *
     * @param request �������� �������.
     * @return ������� ���������
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
