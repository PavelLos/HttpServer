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
 * Класс, отвечающий за работу сервера: обработка запросов и формирование ответа
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
     * Массив байт для отправки ответа
     */
    private byte[] response;
    /**
     * true, если ответ корректный и false, если неверен
     */
    private boolean correctResponse;

    /**
     * Создание экземпляра
     */
    public HttpServer() {
        correctResponse = false;
    }

    /**
     * Метод, отвечающий за создание ответа сервера
     *
     * @param input - входной поток данных
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
     * Метод, формирующий ответ get запроса.
     */
    private void doGet() {
        response = responseHandler.createGetResponse(requestHandler.getUrl());
        correctResponse = true;
    }

    /**
     * Метод, формирующий ответ post запроса.
     */
    private void doPost() {
        response = responseHandler.createPostResponse(requestHandler.getUrl(),
                requestHandler.getRequestParametersToString());
        correctResponse = true;

    }

    /**
     * Метод, формирующий ответ head запроса.
     */
    private void doHead() {
        response = responseHandler.createHeadResponse(requestHandler.getUrl());
        correctResponse = true;

    }

    /**
     * Метод, отсылающий ответ сервера клиенту.
     *
     * @param output - поток для отправки данных
     */
    public void sendResponse(OutputStream output) {
        if (correctResponse) {
            try {
                output.write(response);
                output.flush();
                correctResponse = false;
            } catch (IOException e) {
                log.error("Response не отправлен");
                ServerWindow.getInstance().printInfo("Response не отправлен");
            }
        }
    }
}
