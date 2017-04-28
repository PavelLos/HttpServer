package com.pavel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pasha on 25.03.2017.
 */
public class Main {

    public static void main(String[] args) {
        List<String> params = readParams(args);
        try {
            PostExample.getInstance().createDocument(params.get(0), params.get(1));
        }catch (ArrayIndexOutOfBoundsException e){
            PostExample.getInstance().createExeption(e.toString());
        }
    }

    private static List<String> readParams(String... strings) {
        List params = new ArrayList<String>();
        for (String param : strings) {
            params.add(param);
        }
        params.add("D:/bsuir/program/java/HttpServer/pages/post_show.html");
        params.add("olololol");
        /*params.add("message1=privet");
        params.add("message2=poka");
        params.add("message3=ups");*/

        return params;
    }
}
