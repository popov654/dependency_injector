package di;

import annotation.InjectObject;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Injector {

    private static final Class<? extends Annotation> ANNOTATION_CLASS = (Class<? extends Annotation>) InjectObject.class;

    @SneakyThrows
    public static void injectServices(Object object) {
        for (Field field: object.getClass().getDeclaredFields()) {
            Class<?> t = field.getType();
            Injector.injectObject(object, field, field.getType());
        }
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private static <T> void injectObject(Object object, Field field, Class<T> interfaceClass) {
        Annotation annotation = field.getAnnotation(Injector.ANNOTATION_CLASS);
        if (annotation == null) return;

        Class<? extends T> impl = null;
        Object t;
        if (annotation.getClass().getMethod("value").getReturnType() == String.class) {
            String value = (String) annotation.getClass().getMethod("value").invoke(annotation);
            if (!value.isEmpty()) {
                impl = (Class<? extends T>) ClassLoader.getSystemClassLoader().loadClass(value);
            }
        } else {
            Set<Class<? extends T>> classes = (new Reflections("services")).getSubTypesOf(interfaceClass);
            if (!classes.isEmpty()) {
                impl = classes.iterator().next();
            }
        }

        if (impl == null) return;

        if (ServiceContainer.getInstance().get(impl) != null) {
            t = ServiceContainer.getInstance().get(impl);
        } else {
            t = impl.getDeclaredConstructor().newInstance();
            ServiceContainer.getInstance().put(impl, t);
        }

        field.setAccessible(true);
        field.set(object, t);
    }

    public static <T> T createService(Class<T> impl) {
        return createService(impl, null);
    }

    @SneakyThrows
    private static <T> T createService(Class<T> impl, List<Class<?>> stack) {
        if (stack == null) {
            stack = new ArrayList<Class<?>>();
        }
        if (impl.isInterface()) {
            Set<Class<? extends T>> classes = (new Reflections("services")).getSubTypesOf(impl);
            if (!classes.isEmpty()) {
                impl = (Class<T>) classes.iterator().next();
            }
        }

        T t;
        if (ServiceContainer.getInstance().get(impl) != null) {
            t = (T) ServiceContainer.getInstance().get(impl);
        } else {
            Constructor<T>[] constructors = (Constructor<T>[]) impl.getConstructors();
            if (constructors.length == 0) {
                return null;
            }
            Constructor<T> constructor = constructors[0];
            Class<?>[] args = constructor.getParameterTypes();
            Object[] params = new Object[args.length];

            if (!stack.contains(impl)) {
                stack.add(impl);
            } else {
                throw new RuntimeException("Cyclic dependency in class " + impl);
            }

            int i = 0;
            for (Class<?> arg: args) {
                params[i++] = createService(arg, stack);
            }

            stack.remove(impl);

            t = constructor.newInstance(params);
            ServiceContainer.getInstance().put(impl, t);
        }

        return t;
    }

}
