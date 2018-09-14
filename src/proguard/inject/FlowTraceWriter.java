package proguard.inject;

import java.util.Arrays;

public class FlowTraceWriter {
    static public void logBefore(String thisClassName, String thisMethodName, String callClassName, String callMethodName, int thisLineNumber, int callLineNumber) {
        System.out.println("Before -> " + thisClassName + " " + thisMethodName + " "  + thisLineNumber + " " + callClassName + " " + callMethodName + " "  + callLineNumber);
    }

    static public void logAfter(String thisClassName, String thisMethodName, String callClassName, String callMethodName, int thisLineNumber, int callLineNumber) {
        System.out.println("After <- " + thisClassName + " " + thisMethodName + " "  + thisLineNumber + " " + callClassName + " " + callMethodName + " "  + callLineNumber);
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
