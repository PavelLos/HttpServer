package com.pavel;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pasha on 25.03.2017.
 */
public class Main {
    private static PrintStream printStream;
    //private PrintStream exeptionStream;
    private static PrintStream exeptionStream = new PrintStream(System.err);

    public static void main(String[] args) {
        printStream = new PrintStream(System.out);
        try {
            List<String> params = readParams(args);
            StringBuilder document = PostExample.getInstance().createDocument(params.get(0), params.get(1));
            printStream.print(document);
        }catch (ArrayIndexOutOfBoundsException e){
            printStream.print("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Show Message</title>\n" +
                    "</head>\n" +
                    "<body>\n");
            e.printStackTrace(printStream);
            printStream.print("</body>\n" +
                    "</html>");
        }
    }

    private static List<String> readParams(String... strings) {
        List params = new ArrayList<String>();
        for (String param : strings) {
            params.add(param);
        }
        return params;
    }
}
