package proguard.inject;

import proguard.Configuration;
import proguard.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.preverification.StackMapFrame;
import proguard.classfile.attribute.preverification.VerificationType;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.ConstantInstruction;
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
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.target.*;
import java.io.IOException;
import static proguard.classfile.util.ClassUtil.internalClassName;

public class FlowTraceInjector_Old
        extends SimplifiedVisitor
        implements
        ClassVisitor,
        MemberVisitor,
        AttributeVisitor,
        InstructionVisitor
{
    static final boolean DEBUG = false;
    private final Configuration configuration;
    private CodeAttributeEditor codeAttributeEditor;
    // Field acting as parameter for the visitor methods.
    private MultiValueMap<String, String> injectedClassMap;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private InstructionSequenceBuilder ____;
    private final String LOGGER_CLASS_NAME = ClassUtil.internalClassName(proguard.inject.FlowTraceWriter.class.getName());



    /**
     * Creates a new TraceInjector.
     */
    public FlowTraceInjector_Old(Configuration configuration)
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

    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        if (DEBUG)
        {
            Logger.out_println("visitProgramClass: " + programClass.getName());
        }
        injectedClassMap.put(programClass.getName(), internalClassName(FlowTraceWriter.class.getName()));
        injectedClassMap.put(programClass.getName(), internalClassName(FlowTraceWriter.MethodSignature.class.getName()));

        ____ = new InstructionSequenceBuilder(programClass, programClassPool, libraryClassPool);
        programClass.methodsAccept(this);
    }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        programMethod.attributesAccept(programClass, this);
    }

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // Set up the code attribute editor.
        codeAttributeEditor.reset(codeAttribute.u4codeLength);

        codeAttribute.instructionsAccept(clazz, method, this);

        //write if modified
        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
    }

    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        if (DEBUG)
        {
            Logger.out_println("visitConstantInstruction: " + clazz.getName() + " " + method.getName(clazz) + " " + constantInstruction.getName() + " " + constantInstruction.constantIndex);
        }

        if (constantInstruction.opcode == InstructionConstants.OP_INVOKEVIRTUAL ||
                constantInstruction.opcode == InstructionConstants.OP_INVOKESPECIAL ||
                constantInstruction.opcode == InstructionConstants.OP_INVOKESTATIC ||
                constantInstruction.opcode == InstructionConstants.OP_INVOKEINTERFACE ||
                constantInstruction.opcode == InstructionConstants.OP_INVOKEDYNAMIC)
        {

            try
            {
                String thisClassName = clazz.getName();
                String thisMethodName = method.getName(clazz);
                ProgramClass programClass = (ProgramClass)clazz;
                RefConstant refConstant = (RefConstant)programClass.getConstant(constantInstruction.constantIndex);
                String callClassName = refConstant.getClassName(programClass);
                String callMethodName = refConstant.getName(programClass);
                int thisLineNumber = codeAttribute.getLineNumber(0);
                int callLineNumber = codeAttribute.getLineNumber(offset);

                int thisClassNameRef = ____.getConstantPoolEditor().addStringConstant(thisClassName, clazz, null);
                int thisMetodNameRef = ____.getConstantPoolEditor().addStringConstant(thisMethodName, clazz, null);
                int callClassNameRef = ____.getConstantPoolEditor().addStringConstant(callClassName, clazz, null);
                int callMetodNameRef = ____.getConstantPoolEditor().addStringConstant(callMethodName, clazz, null);

                codeAttributeEditor.insertBeforeInstruction(offset, logInstruction(true, thisClassNameRef, thisMetodNameRef, callClassNameRef, callMetodNameRef, thisLineNumber, callLineNumber));
                codeAttributeEditor.insertAfterInstruction(offset, logInstruction(false, thisClassNameRef, thisMetodNameRef, callClassNameRef, callMetodNameRef, thisLineNumber, callLineNumber));
            }
            catch (Exception e)
            {
                Logger.err_println("Eception on visitConstantInstruction: " + clazz.getName() + " " + method.getName(clazz) + " " + constantInstruction.getName() + " " + constantInstruction.constantIndex);
                Logger.err_println("Eception: " + e.toString());
            }
        }
    }

    public void visitAnyClass(Clazz clazz){}
    public void visitAnyMember(Clazz clazz, Member member){}
    public void visitAnyConstant(Clazz clazz, Constant constant){}
    public void visitAnyPrimitiveArrayConstant(Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant, Object values){}
    public void visitAnyPrimitiveArrayConstantElement(Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant, int index){}
    public void visitAnyAttribute(Clazz clazz, Attribute attribute){}
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction){}
    public void visitAnyStackMapFrame(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, StackMapFrame stackMapFrame){}
    public void visitAnyVerificationType(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VerificationType verificationType){}
    public void visitAnnotation(Clazz clazz, Annotation annotation){}
    public void visitTypeAnnotation(Clazz clazz, TypeAnnotation typeAnnotation){}
    public void visitAnyTargetInfo(Clazz clazz, TypeAnnotation typeAnnotation, TargetInfo targetInfo){}
    public void visitTypePathInfo(Clazz clazz, TypeAnnotation typeAnnotation, TypePathInfo typePathInfo){}
    public void visitAnyElementValue(Clazz clazz, Annotation annotation, ElementValue elementValue){}


    private Instruction[] logInstruction(boolean before, int thisClassNameRef, int thisMetodNameRef, int callClassNameRef, int callMetodNameRef, int thisLineNumber, int callLineNumber)
    {
        return   ____
                .ldc_(thisClassNameRef)
                .ldc_(thisMetodNameRef)
                .ldc_(callClassNameRef)
                .ldc_(callMetodNameRef)
                .ldc(thisLineNumber)
                .ldc(callLineNumber)
                .invokestatic(LOGGER_CLASS_NAME, before ? "logBefore" : "logAfter", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V").__();
    }
}
