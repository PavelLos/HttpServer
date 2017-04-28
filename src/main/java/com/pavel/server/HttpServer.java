package com.pavel.server;


import com.pavel.constants.HttpMethod;
import com.pavel.controller.RequestHandler;
import com.pavel.controller.ResponseHandler;
import com.pavel.view.ServerWindow;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * �����, ���������� �� ������ �������: ��������� �������� � ������������ ������
 */
public class HttpServer {
    /**
     * @see Logger#getLogger
     */
    private static Logger log = Logger.getLogger(HttpServer.class);
    /**
     * @see RequestHandler
     */
    private ResponseHandler responseHandler;
    /**
     * @see RequestHandler
     */
    private RequestHandler requestHandler;

    /**
     * ������ ���� ��� �������� ������
     */
    private byte[] response;
    /**
     * true, ���� ����� ���������� � false, ���� �������
     */
    private boolean correctResponse;

    /**
     * �������� ����������
     */
    public HttpServer() {
        correctResponse = false;
    }

    /**
     * �����, ���������� �� �������� ������ �������
     *
     * @param input - ������� ����� ������
     */
    public void httpMethod(InputStream input) {
        requestHandler = new RequestHandler(input);
        if (requestHandler.isCorrectRequest()) {
            responseHandler = new ResponseHandler();
            String method = requestHandler.getMethod();
            if (method.equals(HttpMethod.GET.getMethod())) {
                doGet();
            } else if (method.equals(HttpMethod.POST.getMethod())) {
                doPost();
            } else if (method.equals(HttpMethod.HEAD.getMethod())) {
                doHead();
            } else doNotImplemented();

        }
    }

    private void doNotImplemented() {
        response = responseHandler.notImplemented(requestHandler.getUrl());
        correctResponse = true;
    }

    /**
     * �����, ����������� ����� get �������.
     */
    private void doGet() {
        response = responseHandler.createGetResponse(requestHandler.getUrl());
        correctResponse = true;
    }

    /**
     * �����, ����������� ����� post �������.
     */
    private void doPost() {
        response = responseHandler.createPostResponse(requestHandler.getUrl(),
                requestHandler.getRequestParametersToString());
        correctResponse = true;

    }

    /**
     * �����, ����������� ����� head �������.
     */
    private void doHead() {
        response = responseHandler.createHeadResponse(requestHandler.getUrl());
        correctResponse = true;

    }

    /**
     * �����, ���������� ����� ������� �������.
     *
     * @param output - ����� ��� �������� ������
     */
    public void sendResponse(OutputStream output) {
        if (correctResponse) {
            try {
                output.write(response);
                output.flush();
                correctResponse = false;
            } catch (IOException e) {
                log.error("Response �� ���������");
                ServerWindow.getInstance().printInfo("Response �� ���������");
            }
        }
    }
}
