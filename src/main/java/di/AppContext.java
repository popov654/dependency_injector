package di;

import annotation.InjectObject;
import service.ServiceA;
import service.printer.ConsolePrinter;

public class AppContext {
    @InjectObject("service.printer.impl.DashedBlockConsolePrinter")
    private ConsolePrinter printer;

    private static AppContext instance;

    private AppContext() {}

    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
            Injector.injectServices(instance);
        }
        return instance;
    }

    public void start() {
        printer.print("Test message");
        ServiceA service = Injector.createService(ServiceA.class);
        System.out.println(service.getClass());
    }
}
