package com.pavel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pasha on 25.03.2017.
 */
public class Main {

    public static void main(String[] args) {
        List<String> params = readParams(args);
        PostExample.getInstance().createDocument(params.get(0), params.get(1));
    }

    private static List<String> readParams(String... strings) {
        List params = new ArrayList<String>();
        for (String param : strings) {
            params.add(param);
        }
        return params;
    }
}
