package com.pavel;


import com.pavel.configurations.Configurations;
import com.pavel.view.ServerWindow;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        PropertyConfigurator.configure(Configurations.getInstance().getLogPath());
        ServerWindow.getInstance();
    }
}
