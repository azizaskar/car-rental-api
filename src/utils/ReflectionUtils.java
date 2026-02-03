package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static void inspectClass(Object obj) {
        Class<?> clazz = obj.getClass();
        System.out.println("\n>>> Reflection Inspection: " + clazz.getSimpleName());

        // fields шығару
        System.out.println("Fields:");
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true); // private болса да оқу үшін
            try {
                System.out.println(" - " + field.getName() + ": " + field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // methods шығару
        System.out.println("Methods:");
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println(" - " + method.getName() + "()");
        }
        System.out.println("------------------------------------");
    }
}