package com.pavel;

import java.io.*;

public class PostExample {
    private static PostExample instance = new PostExample();
    private PrintStream printStream;

    private PostExample() {
        printStream = new PrintStream(System.out);
    }

    public static PostExample getInstance() {
        return instance;
    }

    public void createDocument(String path, String documentText) {
        StringBuilder document = new StringBuilder();
        String fileString = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            while (true) {
                try {
                    fileString = reader.readLine();
                    if (fileString == null)
                        break;
                    document.append(fileString);
                    if (fileString.equals("<body>"))
                        document.append(documentText);
                } catch (IOException e) {
                }
            }
        } catch (FileNotFoundException e) {

        }
        sendDocument(document);
    }

    private void sendDocument(StringBuilder document) {
        printStream.print(document);
    }

}
