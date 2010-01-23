package stubrn.handling;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 */
class Visibilities {
    static boolean isNotAccessible(Field f) {
        int modifier = f.getModifiers();
        return isNotAccessible(modifier);
    }

    private static boolean isNotAccessible(int modifier) {
        return Modifier.isProtected(modifier) || Modifier.isPrivate(modifier);
    }

    static boolean isNotAccessible(Method m) {
        int modifier = m.getModifiers();
        return isNotAccessible(modifier);
    }
}
