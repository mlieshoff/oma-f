package org.mili.application.util;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 21.07.13
 * Time: 22:07
 * To change this template use File | Settings | File Templates.
 */
public class Log {

    public static void log(Class cls, String method, String message, Object... objects) {
        System.out.println(String.format("%s [%s]: %s", cls.getName(), method, String.format(message, objects)));
    }

    public static void log(Object o, String method, String message, Object... objects) {
        System.out.println(String.format("%s [%s]: %s", o.getClass().getName(), method, String.format(message, objects)));
    }

}
