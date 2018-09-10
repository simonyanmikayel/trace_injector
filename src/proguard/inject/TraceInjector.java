package proguard.inject;

import proguard.*;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.io.*;
import proguard.optimize.peephole.*;
import proguard.util.MultiValueMap;

import java.io.IOException;

import static proguard.classfile.util.ClassUtil.internalClassName;

public class TraceInjector
        extends      SimplifiedVisitor
        implements   // Implementation interfaces.
        InstructionVisitor
{
    static final boolean DEBUG = true;
    private final Configuration configuration;

    // Field acting as parameter for the visitor methods.
    private  MultiValueMap<String, String> injectedClassMap;


    /**
     * Creates a new TraceInjector.
     */
    public TraceInjector(Configuration configuration)
    {
        this.configuration = configuration;
    }


    /**
     * Instrumets the given program class pool.
     */
    public void execute(ClassPool                     programClassPool,
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

        // Set up the instruction sequences and their replacements.
        TraceInjectorInstructionSequenceConstants constants =
                new TraceInjectorInstructionSequenceConstants(programClassPool,
                        libraryClassPool);

        BranchTargetFinder  branchTargetFinder  = new BranchTargetFinder();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

        // Set the injected class map for the extra visitor.
        this.injectedClassMap = injectedClassMap;

        // Replace the instruction sequences in all non-ProGuard classes.
        programClassPool.classesAccept(
                new ClassNameFilter("!proguard/**",
                        new AllMethodVisitor(
                                new AllAttributeVisitor(
                                        new PeepholeOptimizer(branchTargetFinder, codeAttributeEditor,
                                                new TraceInjectorInstructionSequencesReplacer(constants.CONSTANTS,
                                                        constants.RESOURCE,
                                                        branchTargetFinder,
                                                        codeAttributeEditor,
                                                        this))))));
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
        if (DEBUG)
        {
            FlowTraceWriter.out_println("visitAnyInstruction: " + clazz.getName() + " " + method.getName(clazz) + " " + instruction.getName());
        }
        // Add a dependency from the modified class on the injector class.
        injectedClassMap.put(clazz.getName(), internalClassName(FlowTracer.class.getName()));
        injectedClassMap.put(clazz.getName(), internalClassName(FlowTracer.MethodSignature.class.getName()));
    }
}
