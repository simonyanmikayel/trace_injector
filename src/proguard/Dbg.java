package proguard;

public class Dbg {
    static boolean dbg = true;
    static String tag = "fff. ";
    static void log() {
        if (!dbg)
            return;
        Logger.out_println(logPos());
    }
    static void println(Object o) {
        if (!dbg)
            return;
        Logger.out_println(logPos());
        Logger.out_println(o);
    }
    static void print(Object o) {
        if (!dbg)
            return;
        Logger.out_print(o);
    }
    static void printStrings(String descr, String[] strs) {
        if (!dbg)
            return;
        Logger.out_print(logPos());
        Logger.out_println(descr);
        for (String str: strs) {
            Logger.out_print("    ");
            Logger.out_println(str);
        }
    }
    static void printStack() {
        if (!dbg)
            return;
        printStack("");
    }
    static void printStack(String descr) {
        if (!dbg)
            return;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Logger.out_println(logPos() + descr + " -> began stack");
        for (int i = 3; i < stackTraceElements.length; i++)
        {
            Logger.out_println( "    " + i + ". " + stackElemToString(stackTraceElements, i));
        }
    }
    static String stackElemToString(StackTraceElement[] elements, int i) {
        if (i >= elements.length)
            return "[?:?] ";
        return "[" + elements[i].getClassName() + "." + elements[i].getMethodName() + ":" + elements[i].getLineNumber() + "] ";
    }
    static String logPos() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return tag + stackElemToString(stackTraceElements, 3);
    }
}
