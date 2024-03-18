package di;

import java.util.HashMap;
import java.util.Map;

public class ServiceContainer {

    private static ServiceContainer instance;

    private final Map<Class<?>, Object> services = new HashMap<Class<?>, Object>();

    private ServiceContainer() {}

    public static ServiceContainer getInstance() {
        if (instance == null) {
            instance = new ServiceContainer();
        }
        return instance;
    }

    public void put(Class<?> type, Object object) {
        services.put(type, object);
    }

    public Object get(Class<?> type) {
        return services.get(type);
    }

}
