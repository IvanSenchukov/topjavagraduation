package com.github.ivansenchukov.topjavagraduation.util;

import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.model.HasId;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, Class objectClass, int id) {
        return checkNotFound(object, objectClass, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean found, Class objectClass, int id) {
        checkNotFound(found, objectClass, "id=" + id);
    }

    public static <T> T checkNotFound(T object, Class objectClass, String msg) {
        checkNotFound(object != null,objectClass, msg);
        return object;
    }

    public static void checkNotFound(boolean found, Class objectClass, String msg) {
        if (!found) {
            throw new NotFoundException(String.format("Not found entity with type=|%s| %s", objectClass.getSimpleName(), msg));
        }
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalArgumentException(bean + " must be new (id=null)");
        }
    }


    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalArgumentException(bean + " must be with id=" + id);
        }
    }

}
