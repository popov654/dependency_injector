package di;

import java.util.HashMap;
import java.util.Map;

public class ComponentStore {

    private static ComponentStore instance;

    private final Map<Class<?>, Object> components = new HashMap<>();

    private ComponentStore() {}

    public static ComponentStore getInstance() {
        if (instance == null) {
            instance = new ComponentStore();
        }
        return instance;
    }

    public void put(Class<?> type, Object object) {
        components.put(type, object);
    }

    public Object get(Class<?> type) {
        return components.get(type);
    }

}
