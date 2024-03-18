package service.printer.impl;

import service.printer.ConsolePrinter;

public class BasicConsolePrinter implements ConsolePrinter {

    public void print(String value) {
        System.out.println(value);
    }

}
