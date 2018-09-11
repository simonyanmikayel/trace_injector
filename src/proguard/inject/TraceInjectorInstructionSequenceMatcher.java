package proguard.inject;

import proguard.FlowTraceWriter;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.instruction.*;
import proguard.classfile.util.InstructionSequenceMatcher;

public class TraceInjectorInstructionSequenceMatcher extends InstructionSequenceMatcher {

    private static boolean DEBUG = true;
    private static boolean TEST = true;
    private static boolean VISIT_CONSTANTS = true;

    public TraceInjectorInstructionSequenceMatcher(Constant[] patternConstants, Instruction[] patternInstructions) {
        super(patternConstants, patternInstructions);
    }

    public void reset()
    {
        super.reset();
    }

    public boolean isMatching()
    {
        return super.isMatching();
    }

    public int instructionCount()
    {
        return patternInstructions.length;
    }

    public int matchedInstructionOffset(int index)
    {
        return super.matchedInstructionOffset(index);
    }

    public boolean wasConstant(int argument)
    {
        return super.wasConstant(argument);
    }

    public int matchedArgument(int argument)
    {
        return super.matchedArgument(argument);
    }

    public int[] matchedArguments(int[] arguments)
    {
        return super.matchedArguments(arguments);
    }

    public int matchedConstantIndex(int constantIndex)
    {
        return super.matchedConstantIndex(constantIndex);
    }

    public int matchedBranchOffset(int offset, int branchOffset)
    {
        return super.matchedBranchOffset(offset, branchOffset);
    }

    public int[] matchedJumpOffsets(int offset, int[] jumpOffsets)
    {
        return super.matchedJumpOffsets(offset, jumpOffsets);
    }


    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
    {
        FlowTraceWriter.out_println("visit simpleInstruction: ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]: "+simpleInstruction.toString(offset));
        super.visitSimpleInstruction(clazz, method, codeAttribute, offset, simpleInstruction);
    }

    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction)
    {
        FlowTraceWriter.out_println("visit variableInstruction: ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]: "+variableInstruction.toString(offset));
        super.visitVariableInstruction(clazz, method, codeAttribute, offset, variableInstruction);
    }

    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        FlowTraceWriter.out_println("visit constantInstruction: ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]: "+constantInstruction.toString(offset));
        super.visitConstantInstruction(clazz, method, codeAttribute, offset, constantInstruction);
    }

    public void visitBranchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, BranchInstruction branchInstruction)
    {
        FlowTraceWriter.out_println("visit branchInstruction: ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]: "+branchInstruction.toString(offset));
        super.visitBranchInstruction(clazz, method, codeAttribute, offset, branchInstruction);
    }


    public void visitTableSwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, TableSwitchInstruction tableSwitchInstruction)
    {
        FlowTraceWriter.out_println("visit tableSwitchInstruction: ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]: "+tableSwitchInstruction.toString(offset));
        super.visitTableSwitchInstruction(clazz, method, codeAttribute, offset, tableSwitchInstruction);
    }


    public void visitLookUpSwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, LookUpSwitchInstruction lookUpSwitchInstruction)
    {
        FlowTraceWriter.out_println("visit lookUpSwitchInstruction: ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]: "+lookUpSwitchInstruction.toString(offset));
        super.visitLookUpSwitchInstruction(clazz, method, codeAttribute, offset, lookUpSwitchInstruction);
    }


    // Implementations for ConstantVisitor.

    public void visitIntegerConstant(Clazz clazz, IntegerConstant integerConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit integerConstant: "+clazz.getName());
            super.visitIntegerConstant(clazz, integerConstant);
        }
    }


    public void visitLongConstant(Clazz clazz, LongConstant longConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit longConstant: "+clazz.getName());
            super.visitLongConstant(clazz, longConstant);
        }
    }


    public void visitFloatConstant(Clazz clazz, FloatConstant floatConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit floatConstant: "+clazz.getName());
            super.visitFloatConstant(clazz, floatConstant);
        }
    }


    public void visitDoubleConstant(Clazz clazz, DoubleConstant doubleConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit doubleConstant: "+clazz.getName());
            super.visitDoubleConstant(clazz, doubleConstant);
        }
    }


    public void visitPrimitiveArrayConstant(Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit primitiveArrayConstant: "+clazz.getName());
            super.visitPrimitiveArrayConstant(clazz, primitiveArrayConstant);
        }
    }


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit stringConstant: "+clazz.getName()+" " + stringConstant.getString(clazz));
            super.visitStringConstant(clazz, stringConstant);
        }
    }


    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit utf8Constant: "+clazz.getName());
            super.visitUtf8Constant(clazz, utf8Constant);
        }
    }


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit invokeDynamicConstant: "+clazz.getName());
            super.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        }
    }


    public void visitMethodHandleConstant(Clazz clazz, MethodHandleConstant methodHandleConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit methodHandleConstant: "+clazz.getName());
            super.visitMethodHandleConstant(clazz, methodHandleConstant);
        }
    }


    public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit visitAnyRefConstant: "+clazz.getName());
            super.visitAnyRefConstant(clazz, refConstant);
        }
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit classConstant: "+clazz.getName());
            super.visitClassConstant(clazz, classConstant);
        }
    }

    public void visitMethodTypeConstant(Clazz clazz, MethodTypeConstant methodTypeConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit methodTypeConstant: "+clazz.getName());
            super.visitMethodTypeConstant(clazz, methodTypeConstant);
        }
    }


    public void visitNameAndTypeConstant(Clazz clazz, NameAndTypeConstant nameAndTypeConstant)
    {
        if (VISIT_CONSTANTS) {
            FlowTraceWriter.out_println("visit nameAndTypeConstant: "+clazz.getName());
            super.visitNameAndTypeConstant(clazz, nameAndTypeConstant);
        }
    }

    protected boolean matchingOpcodes(Instruction instruction1, Instruction instruction2)
    {
        return super.matchingOpcodes(instruction1, instruction2);
    }


    protected boolean matchingArguments(int argument1, int argument2)
    {
        return super.matchingArguments(argument1, argument2);
    }

    protected boolean matchingArguments(int[] arguments1, int[] arguments2)
    {
        return super.matchingArguments(arguments1, arguments2);
    }


    protected boolean matchingConstantIndices(Clazz clazz, int constantIndex1, int constantIndex2)
    {
        return super.matchingConstantIndices(clazz, constantIndex1, constantIndex2);
    }

    protected boolean matchingBranchOffsets(int offset, int branchOffset1, int branchOffset2)
    {
        return super.matchingBranchOffsets(offset, branchOffset1, branchOffset2);
    }


    protected boolean matchingJumpOffsets(int   offset, int[] jumpOffsets1, int[] jumpOffsets2)
    {
        return super.matchingJumpOffsets(offset, jumpOffsets1, jumpOffsets2);
    }

}
