package di;

public class AppRunner {

    public AppRunner(Runnable runnable) {
        init(runnable);
    }

    public AppRunner(Runnable runnable, LifecycleEventListener listener) {
        listener.onStart();
        init(runnable);
        listener.onFinish();
        runnable.run();
    }

    private void init(Runnable runnable) {
        Injector.injectServices(runnable);
    }

}
