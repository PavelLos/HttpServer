package com.pavel;

import java.io.PrintStream;

/**
 * Created by pasha on 29.04.2017.
 */
public class ErrorMain {
    public static void main(String[] args){
        PrintStream printStream = new PrintStream(System.out);
        try {
            getAreaValue(-1, 100);
        }catch (IllegalArgumentException e){
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

    public static int getAreaValue(int x, int y){
        if(x < 0 || y < 0) throw new IllegalArgumentException("value of 'x' or 'y' is negative: x="+x+", y="+y);
        return x*y;
    }
}
