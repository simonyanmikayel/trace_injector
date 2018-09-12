package proguard.inject;

import proguard.FlowTraceWriter;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.instruction.*;
import proguard.classfile.util.InstructionSequenceMatcher;
import proguard.classfile.util.InstructionSequenceMatcherInterface;

public class TraceInjectorInstructionSequenceMatcher
implements InstructionSequenceMatcherInterface
{

    private static boolean DEBUG = true;
    private static boolean TEST = true;
    private static boolean VISIT_CONSTANTS = true;
    private InstructionSequenceMatcher instructionSequenceMatcher;
    protected final Constant[]    patternConstants;
    protected final Instruction[] patternInstructions;

    public TraceInjectorInstructionSequenceMatcher(Constant[] patternConstants, Instruction[] patternInstructions) {
        this.patternConstants    = patternConstants;
        this.patternInstructions = patternInstructions;
        instructionSequenceMatcher = new InstructionSequenceMatcher(patternConstants, patternInstructions);
    }

    public void reset()
    {
        instructionSequenceMatcher.reset();
    }

    public boolean isIvokeOp()
    {
        if (instructionSequenceMatcher.isMatching())
            return false;
        return  visitInfo.instruction.opcode == InstructionConstants.OP_INVOKEVIRTUAL ||
                visitInfo.instruction.opcode == InstructionConstants.OP_INVOKESPECIAL ||
                visitInfo.instruction.opcode == InstructionConstants.OP_INVOKESTATIC ||
                visitInfo.instruction.opcode == InstructionConstants.OP_INVOKEINTERFACE ||
                visitInfo.instruction.opcode == InstructionConstants.OP_INVOKEDYNAMIC;
    }

    public boolean isMatching()
    {
        return instructionSequenceMatcher.isMatching() || isIvokeOp();
    }

    public int instructionCount()
    {
        return patternInstructions.length;
    }

    public int matchedInstructionOffset(int index)
    {
        return instructionSequenceMatcher.matchedInstructionOffset(index);
    }

    public boolean wasConstant(int argument)
    {
        return instructionSequenceMatcher.wasConstant(argument);
    }

    public int matchedArgument(int argument)
    {
        return instructionSequenceMatcher.matchedArgument(argument);
    }

    public int[] matchedArguments(int[] arguments)
    {
        return instructionSequenceMatcher.matchedArguments(arguments);
    }

    public int matchedConstantIndex(int constantIndex)
    {
        return instructionSequenceMatcher.matchedConstantIndex(constantIndex);
    }

    public int matchedBranchOffset(int offset, int branchOffset)
    {
        return instructionSequenceMatcher.matchedBranchOffset(offset, branchOffset);
    }

    public int[] matchedJumpOffsets(int offset, int[] jumpOffsets)
    {
        return instructionSequenceMatcher.matchedJumpOffsets(offset, jumpOffsets);
    }


    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
    {
        FlowTraceWriter.out_println("visit simpleInstruction: ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]: "+simpleInstruction.toString(offset));
        instructionSequenceMatcher.visitSimpleInstruction(clazz, method, codeAttribute, offset, simpleInstruction);
    }

    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction)
    {
        FlowTraceWriter.out_println("visit variableInstruction: ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]: "+variableInstruction.toString(offset));
        instructionSequenceMatcher.visitVariableInstruction(clazz, method, codeAttribute, offset, variableInstruction);
    }

    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        FlowTraceWriter.out_println("visit constantInstruction: ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]: "+constantInstruction.toString(offset));
        instructionSequenceMatcher.visitConstantInstruction(clazz, method, codeAttribute, offset, constantInstruction);
    }

    public void visitBranchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, BranchInstruction branchInstruction)
    {
        FlowTraceWriter.out_println("visit branchInstruction: ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]: "+branchInstruction.toString(offset));
        instructionSequenceMatcher.visitBranchInstruction(clazz, method, codeAttribute, offset, branchInstruction);
    }


    public void visitTableSwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, TableSwitchInstruction tableSwitchInstruction)
    {
        FlowTraceWriter.out_println("visit tableSwitchInstruction: ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]: "+tableSwitchInstruction.toString(offset));
        instructionSequenceMatcher.visitTableSwitchInstruction(clazz, method, codeAttribute, offset, tableSwitchInstruction);
    }


    public void visitLookUpSwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, LookUpSwitchInstruction lookUpSwitchInstruction)
    {
        FlowTraceWriter.out_println("visit lookUpSwitchInstruction: ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]: "+lookUpSwitchInstruction.toString(offset));
        instructionSequenceMatcher.visitLookUpSwitchInstruction(clazz, method, codeAttribute, offset, lookUpSwitchInstruction);
    }


    // Implementations for ConstantVisitor.

    public void visitIntegerConstant(Clazz clazz, IntegerConstant integerConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit integerConstant: "+clazz.getName());
            instructionSequenceMatcher.visitIntegerConstant(clazz, integerConstant);
        }
    }


    public void visitLongConstant(Clazz clazz, LongConstant longConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit longConstant: "+clazz.getName());
            instructionSequenceMatcher.visitLongConstant(clazz, longConstant);
        }
    }


    public void visitFloatConstant(Clazz clazz, FloatConstant floatConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit floatConstant: "+clazz.getName());
            instructionSequenceMatcher.visitFloatConstant(clazz, floatConstant);
        }
    }


    public void visitDoubleConstant(Clazz clazz, DoubleConstant doubleConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit doubleConstant: "+clazz.getName());
            instructionSequenceMatcher.visitDoubleConstant(clazz, doubleConstant);
        }
    }


    public void visitPrimitiveArrayConstant(Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit primitiveArrayConstant: "+clazz.getName());
            instructionSequenceMatcher.visitPrimitiveArrayConstant(clazz, primitiveArrayConstant);
        }
    }


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit stringConstant: "+clazz.getName()+" " + stringConstant.getString(clazz));
            instructionSequenceMatcher.visitStringConstant(clazz, stringConstant);
        }
    }


    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit utf8Constant: "+clazz.getName());
            instructionSequenceMatcher.visitUtf8Constant(clazz, utf8Constant);
        }
    }


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit invokeDynamicConstant: "+clazz.getName());
            instructionSequenceMatcher.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        }
    }


    public void visitMethodHandleConstant(Clazz clazz, MethodHandleConstant methodHandleConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit methodHandleConstant: "+clazz.getName());
            instructionSequenceMatcher.visitMethodHandleConstant(clazz, methodHandleConstant);
        }
    }


    public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit visitAnyRefConstant: "+clazz.getName());
            instructionSequenceMatcher.visitAnyRefConstant(clazz, refConstant);
        }
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit classConstant: "+clazz.getName());
            instructionSequenceMatcher.visitClassConstant(clazz, classConstant);
        }
    }

    public void visitMethodTypeConstant(Clazz clazz, MethodTypeConstant methodTypeConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit methodTypeConstant: "+clazz.getName());
            instructionSequenceMatcher.visitMethodTypeConstant(clazz, methodTypeConstant);
        }
    }


    public void visitNameAndTypeConstant(Clazz clazz, NameAndTypeConstant nameAndTypeConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit nameAndTypeConstant: "+clazz.getName());
            instructionSequenceMatcher.visitNameAndTypeConstant(clazz, nameAndTypeConstant);
        }
    }

    public void setVisitInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
        visitInfo.clazz = clazz;
        visitInfo.method = method;
        visitInfo.codeAttribute = codeAttribute;
        visitInfo.offset = offset;
        visitInfo.instruction = instruction;
    }

    private VisitInfo visitInfo = new VisitInfo();
    private class VisitInfo {
        Clazz clazz;
        Method method;
        CodeAttribute codeAttribute;
        int offset;
        Instruction instruction;
    }
}
