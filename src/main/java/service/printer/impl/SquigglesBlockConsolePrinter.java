package service.printer.impl;

import service.printer.ConsolePrinter;

public class SquigglesBlockConsolePrinter implements ConsolePrinter {

    public void print(String value) {
        System.out.println(">>");
        System.out.println(value);
        System.out.println("<<");
    }

}
