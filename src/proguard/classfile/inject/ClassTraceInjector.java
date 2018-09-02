package proguard.classfile.inject;

import proguard.FlowTraceWriter;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.ConstantPoolEditor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.InstructionConstants;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.obfuscate.MemberObfuscator;

public class ClassTraceInjector
        extends SimplifiedVisitor
        implements ClassVisitor,
        AttributeVisitor,
        InstructionVisitor,
        MemberVisitor {

    private final CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor(true, false);

    // Implementations for ClassVisitor.

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
        visitMethod(programClass, programMethod);
        programMethod.attributesAccept(programClass, this);
    }

    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        visitMethod(libraryClass, libraryMethod);
    }

    private void visitMethod(Clazz clazz, Member method)
    {
        FlowTraceWriter.out_print("Injecting traces..." + clazz.toString() + " " + method.getName(clazz));
        FlowTraceWriter.out_println("");
    }

    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        FlowTraceWriter.out_print("Injecting traces..." + clazz.toString() + " " + method.getName(clazz) + " " + codeAttribute.getAttributeName(clazz));
        FlowTraceWriter.out_println("");

        // Reset the code changes.
        int codeLength = codeAttribute.u4codeLength;
        codeAttributeEditor.reset(codeLength);

        codeAttribute.instructionsAccept(clazz, method, this);

        // Apply all accumulated changes to the code.
        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);

    }

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
        boolean isReturn = (
                instruction.opcode == InstructionConstants.OP_IRETURN ||
                instruction.opcode == InstructionConstants.OP_LRETURN ||
                instruction.opcode == InstructionConstants.OP_FRETURN ||
                instruction.opcode == InstructionConstants.OP_DRETURN ||
                instruction.opcode == InstructionConstants.OP_ARETURN ||
                instruction.opcode == InstructionConstants.OP_RETURN);

        Instruction newInstruction = new SimpleInstruction(InstructionConstants.OP_RETURN);
        //newInstruction.instructionOffset

        if ( !method.getName(clazz).contains("<init>"))
            codeAttributeEditor.insertAfterInstruction(offset, newInstruction);

        FlowTraceWriter.out_print("Injecting traces..." + clazz.toString() + " " + method.getName(clazz) + " " + codeAttribute.getAttributeName(clazz) + " " + instruction.getName() + " offset: " + offset);
        FlowTraceWriter.out_println("");
    }
}
