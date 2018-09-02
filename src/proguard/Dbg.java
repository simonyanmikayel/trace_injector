package proguard;

public class Dbg {
    static boolean dbg = true;
    static String tag = "fff. ";
    static void log() {
        if (!dbg)
            return;
        FlowTraceWriter.out_println(logPos());
    }
    static void println(Object o) {
        if (!dbg)
            return;
        FlowTraceWriter.out_println(logPos());
        FlowTraceWriter.out_println(o);
    }
    static void print(Object o) {
        if (!dbg)
            return;
        FlowTraceWriter.out_print(o);
    }
    static void printStrings(String descr, String[] strs) {
        if (!dbg)
            return;
        FlowTraceWriter.out_print(logPos());
        FlowTraceWriter.out_println(descr);
        for (String str: strs) {
            FlowTraceWriter.out_print("    ");
            FlowTraceWriter.out_println(str);
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
        FlowTraceWriter.out_println(logPos() + descr + " -> began stack");
        for (int i = 3; i < stackTraceElements.length; i++)
        {
            FlowTraceWriter.out_println( "    " + i + ". " + stackElemToString(stackTraceElements, i));
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
