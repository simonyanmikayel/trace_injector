package proguard.inject;

import proguard.Configuration;
import proguard.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.InstructionConstants;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.ClassReferenceInitializer;
import proguard.classfile.util.ClassSubHierarchyInitializer;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.*;
import proguard.io.ClassPathDataEntry;
import proguard.io.ClassReader;
import proguard.util.MultiValueMap;

import java.io.IOException;

import static proguard.classfile.util.ClassUtil.internalClassName;

public class FlowTraceInjector
extends SimplifiedVisitor
implements
        ClassVisitor,
        MemberVisitor,
        AttributeVisitor,
        InstructionVisitor
{
    static final boolean DEBUG = true;
    private final Configuration configuration;
    private CodeAttributeEditor codeAttributeEditor;
    // Field acting as parameter for the visitor methods.
    private MultiValueMap<String, String> injectedClassMap;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;


    /**
     * Creates a new TraceInjector.
     */
    public FlowTraceInjector(Configuration configuration)
    {
        this.configuration = configuration;
        codeAttributeEditor = new CodeAttributeEditor(true, true);
    }

    /**
     * Instrumets the given program class pool.
     */
    public void execute(ClassPool programClassPool,
                        ClassPool                     libraryClassPool,
                        MultiValueMap<String, String> injectedClassMap )
    {
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;

        ClassReader classReader =
                new ClassReader(false, false, false, null,
                        new MultiClassVisitor(
                                new ClassPoolFiller(programClassPool),
                                new ClassReferenceInitializer(programClassPool, libraryClassPool),
                                new ClassSubHierarchyInitializer()
                        ));

        try
        {
            classReader.read(new ClassPathDataEntry(FlowTraceWriter.MethodSignature.class));
            classReader.read(new ClassPathDataEntry(FlowTraceWriter.class));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        // Set the injected class map for the extra visitor.
        this.injectedClassMap = injectedClassMap;

        // Replace the instruction sequences in all non-ProGuard classes.
        programClassPool.classesAccept(
                new ClassNameFilter("!proguard/**",
                        this));
    }


    public void visitProgramClass(ProgramClass programClass)
    {
        if (DEBUG)
        {
            Logger.out_println("visitProgramClass: " + programClass.getName());
        }
        injectedClassMap.put(programClass.getName(), internalClassName(FlowTracer.class.getName()));
        injectedClassMap.put(programClass.getName(), internalClassName(FlowTracer.MethodSignature.class.getName()));

        programClass.methodsAccept(this);
    }

    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        programMethod.attributesAccept(programClass, this);
    }

    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // Set up the code attribute editor.
        codeAttributeEditor.reset(codeAttribute.u4codeLength);

        codeAttribute.instructionsAccept(clazz, method, this);

        //write if modified
        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
    }

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
        if (DEBUG)
        {
            Logger.out_println("visitAnyInstruction: " + clazz.getName() + " " + method.getName(clazz) + " " + instruction.getName());
        }

//        injectedClassMap.put(clazz.getName(), internalClassName(FlowTracer.class.getName()));
//        injectedClassMap.put(clazz.getName(), internalClassName(FlowTracer.MethodSignature.class.getName()));

        if (instruction.opcode == InstructionConstants.OP_INVOKEVIRTUAL ||
            //instruction.opcode == InstructionConstants.OP_INVOKESPECIAL ||
            instruction.opcode == InstructionConstants.OP_INVOKESTATIC ||
            instruction.opcode == InstructionConstants.OP_INVOKEINTERFACE ||
            instruction.opcode == InstructionConstants.OP_INVOKEDYNAMIC)
        {
            String LOGGER_CLASS_NAME = ClassUtil.internalClassName(proguard.inject.FlowTraceWriter.class.getName());

            InstructionSequenceBuilder ____ = new InstructionSequenceBuilder(programClassPool, libraryClassPool);
            Instruction[] insBefore =  //____.dup()
            ____.invokestatic(LOGGER_CLASS_NAME, "logBefore", "()V").__();
            codeAttributeEditor.insertBeforeInstruction(offset, insBefore);
        }
    }
}
