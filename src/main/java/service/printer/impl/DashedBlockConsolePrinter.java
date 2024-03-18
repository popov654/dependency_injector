package service.printer.impl;

import service.printer.ConsolePrinter;

public class DashedBlockConsolePrinter implements ConsolePrinter {

    public void print(String value) {
        System.out.println("------------------------");
        System.out.println(value);
        System.out.println("------------------------");
    }

}
