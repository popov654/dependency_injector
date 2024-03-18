package di;

import annotation.InjectObject;
import service.printer.ConsolePrinter;

public class Main {


    public static void main(String[] args) {
        new AppContext(new Runnable() {

            @InjectObject("service.printer.impl.DashedBlockConsolePrinter")
            private ConsolePrinter printer;

            public void run() {
                printer.print("Test message");
            }
        },
        new LifecycleEventListener() {
            @Override
            public void onStart() {
                System.out.println("Build started");
            }

            @Override
            public void onFinish() {
                System.out.println("Build finished");
            }
        });
    }
}
