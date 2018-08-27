package proguard;

public class Dbg {
    static boolean dbg = true;
    static String tag = "fff. ";
    static void log() {
        if (!dbg)
            return;
        System.out.println(logPos());
    }
    static void println(Object o) {
        if (!dbg)
            return;
        System.out.println(logPos());
        System.out.println(o);
    }
    static void print(Object o) {
        if (!dbg)
            return;
        System.out.print(o);
    }
    static void printStrings(String descr, String[] strs) {
        if (!dbg)
            return;
        System.out.print(logPos());
        System.out.println(descr);
        for (String str: strs) {
            System.out.print("    ");
            System.out.println(str);
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
        System.out.println(logPos() + descr + " -> began stack");
        for (int i = 3; i < stackTraceElements.length; i++)
        {
            System.out.println( "    " + i + ". " + stackElemToString(stackTraceElements, i));
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
