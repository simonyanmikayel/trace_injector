package proguard.classfile.inject;

import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

public class ClassTraceInjector
        extends SimplifiedVisitor
        implements ClassVisitor,
        MemberVisitor {

    public void visitProgramClass(ProgramClass programClass)
    {
        // inject method traces.
        programClass.methodsAccept(this);
    }

    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // inject method traces.
        libraryClass.methodsAccept(this);
    }

    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        //TODO
        //if (configuration.verbose) {
            System.out.print("Injecting traces..." + programClass.toString() + " " + programMethod.getName(programClass));
        System.out.println("");
    }
}
