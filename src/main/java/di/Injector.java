package di;

import annotation.InjectObject;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

public class Injector {

    private static final Class<? extends Annotation> ANNOTATION_CLASS = (Class<? extends Annotation>) InjectObject.class;

    @SneakyThrows
    public static void injectServices(AppContext context) {
        for (Field field: context.getClass().getDeclaredFields()) {
            Class<?> t = field.getType();
            Injector.injectObject(context, field, field.getType());
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

}
