package vn.vnpt.ssdc.utils;

import java.util.Collection;
import java.util.Map;

import static org.springframework.util.ObjectUtils.toObjectArray;

/**
 * Created by vietnq on 10/25/16.
 */
public class ObjectUtils {
    public static Object[] wrapToArray(Object obj) {
        if (obj == null) {
            return new Object[0];
        }
        if (obj instanceof Object[]) {
            return (Object[]) obj;
        }
        if (obj.getClass().isArray()) {
            return toObjectArray(obj);
        }
        return new Object[]{obj};
    }

    public static boolean empty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return "".equals(((String) object).trim());
        } else if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map) object).isEmpty();
        } else if (object instanceof StringBuilder) {
            return ((StringBuilder) object).length() == 0;
        } else if (object instanceof StringBuffer) {
            return ((StringBuffer) object).length() == 0;
        }
        return false;
    }

    public static String validateObjectParent(String path) {
        return path.endsWith(".") ? path.substring(0, path.length() - 1) : path;
    }

    public static String getNameOfPath(String fullPath) {
        if (fullPath.contains(".")) {
            String arr[] = fullPath.split("\\.");
            fullPath = arr[arr.length - 1];
        }
        return fullPath;
    }
}
