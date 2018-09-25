package proguard.inject;

import java.util.Arrays;

public class FlowTraceWriter {

    static public void logFlow(int log_type) {
        String thisClassName = "";
        String thisMethodName = "";
        String callClassName = "";
        String callMethodName = "";
        int thisLineNumber = -1;
        int callLineNumber = -1;

        int s1 = 2;
        int s2 = s1 + 1;
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        if (stack.length <= s1) {
            //System.out.println("stack.length: " + stack.length);
            return;
        }
        thisClassName = stack[s1].getClassName();
        thisMethodName = stack[s1].getMethodName();
        thisLineNumber = stack[s1].getLineNumber();
        if (stack.length > s2) {
            callClassName = stack[s2].getClassName();
            callMethodName = stack[s2].getMethodName();
            callLineNumber = stack[s2].getLineNumber();
        }

        int thisID = thisClassName.hashCode() +  31 * thisMethodName.hashCode();;
        int callID = callClassName.hashCode() +  31 * callMethodName.hashCode();;

        System.out.println( (log_type == 0 ? " -> " : " <- ") + thisClassName + " " + thisMethodName + " "  + thisLineNumber + " <> " + callClassName + " " + callMethodName + " "  + callLineNumber);
//        if (runnableID != 0)
//            System.out.println("       JAVA_LOG_RUNNABLE");
    }

    static public void logRunnable(int runnableMethod, Object o)
    {
        System.out.println("   ----------------->" + (runnableMethod == 1 ? " <init> " : " run ") + o.hashCode());
    }

    public static class MethodSignature
    {
        private String   name;
        private String[] parameters;


        public MethodSignature(String name, Class[] parameters)
        {
            this.name       = name;
            this.parameters = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++)
            {
                this.parameters[i] = parameters[i].getName();
            }
        }


        // Implementations for Object.

        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FlowTraceWriter.MethodSignature that = (FlowTraceWriter.MethodSignature)o;

            if (!name.equals(that.name)) return false;
            return Arrays.equals(parameters, that.parameters);
        }


        public int hashCode()
        {
            int result = name.hashCode();
            result = 31 * result + Arrays.hashCode(parameters);
            return result;
        }
    }
}
