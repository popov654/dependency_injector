package di;

import annotation.InjectObject;
import service.ServiceA;
import service.printer.ConsolePrinter;

public class AppContext {

    public AppContext(Runnable runnable) {
        init(runnable);
    }

    public AppContext(Runnable runnable, LifecycleEventListener listener) {
        listener.onStart();
        init(runnable);
        listener.onFinish();
    }

    private void init(Runnable runnable) {
        Injector.injectServices(runnable);
        runnable.run();
    }

}
