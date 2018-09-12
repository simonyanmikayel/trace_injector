package proguard.inject;

import proguard.Configuration;
import proguard.FlowTraceWriter;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.ClassReferenceInitializer;
import proguard.classfile.util.ClassSubHierarchyInitializer;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.*;
import proguard.io.ClassPathDataEntry;
import proguard.io.ClassReader;
import proguard.optimize.peephole.BranchTargetFinder;
import proguard.optimize.peephole.PeepholeOptimizer;
import proguard.util.MultiValueMap;

import java.io.IOException;

import static proguard.classfile.util.ClassUtil.internalClassName;

public class FlowTraceInjector
extends SimplifiedVisitor
implements
        ClassVisitor,
        MemberVisitor,
        InstructionVisitor
{
    static final boolean DEBUG = true;
    private final Configuration configuration;

    // Field acting as parameter for the visitor methods.
    private MultiValueMap<String, String> injectedClassMap;


    /**
     * Creates a new TraceInjector.
     */
    public FlowTraceInjector(Configuration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * Instrumets the given program class pool.
     */
    public void execute(ClassPool programClassPool,
                        ClassPool                     libraryClassPool,
                        MultiValueMap<String, String> injectedClassMap )
    {
        // TODO: The initialization could be incomplete if the loaded classes depend on one another.
        ClassReader classReader =
                new ClassReader(false, false, false, null,
                        new MultiClassVisitor(
                                new ClassPoolFiller(programClassPool),
                                new ClassReferenceInitializer(programClassPool, libraryClassPool),
                                new ClassSubHierarchyInitializer()
                        ));

        try
        {
            classReader.read(new ClassPathDataEntry(FlowTracer.MethodSignature.class));
            classReader.read(new ClassPathDataEntry(FlowTracer.class));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        BranchTargetFinder branchTargetFinder  = new BranchTargetFinder();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

        // Set the injected class map for the extra visitor.
        this.injectedClassMap = injectedClassMap;

        // Replace the instruction sequences in all non-ProGuard classes.
        programClassPool.classesAccept(
                new ClassNameFilter("!proguard/**",
                        this));
    }


    public void visitAnyClass(Clazz clazz)
    {
        if (DEBUG)
        {
            FlowTraceWriter.out_println("visitAnyClass: " + clazz.getName());
        }
        injectedClassMap.put(clazz.getName(), internalClassName(FlowTracer.class.getName()));
        injectedClassMap.put(clazz.getName(), internalClassName(FlowTracer.MethodSignature.class.getName()));

        clazz.accept(this);
    }

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
        if (DEBUG)
        {
            FlowTraceWriter.out_println("visitAnyInstruction: " + clazz.getName() + " " + method.getName(clazz) + " " + instruction.getName());
        }
    }
}
