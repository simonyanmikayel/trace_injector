package proguard.classfile.util;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;

public interface InstructionSequenceMatcherInterface
extends InstructionVisitor
{
    public void reset();
    public boolean isMatching();
    public int instructionCount();
    public int matchedInstructionOffset(int index);
    public boolean wasConstant(int argument);
    public int matchedArgument(int argument);
    public int[] matchedArguments(int[] arguments);
    public int matchedConstantIndex(int constantIndex);
    public int matchedBranchOffset(int offset, int branchOffset);
    public int[] matchedJumpOffsets(int offset, int[] jumpOffsets);
    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction);
    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction);
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction);
    public void visitBranchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, BranchInstruction branchInstruction);
    public void visitTableSwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, TableSwitchInstruction tableSwitchInstruction);
    public void visitLookUpSwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, LookUpSwitchInstruction lookUpSwitchInstruction);
    public void visitIntegerConstant(Clazz clazz, IntegerConstant integerConstant);
    public void visitLongConstant(Clazz clazz, LongConstant longConstant);
    public void visitFloatConstant(Clazz clazz, FloatConstant floatConstant);
    public void visitDoubleConstant(Clazz clazz, DoubleConstant doubleConstant);
    public void visitPrimitiveArrayConstant(Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant);
    public void visitStringConstant(Clazz clazz, StringConstant stringConstant);
    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant);
    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant);
    public void visitMethodHandleConstant(Clazz clazz, MethodHandleConstant methodHandleConstant);
    public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant);
    public void visitClassConstant(Clazz clazz, ClassConstant classConstant);
    public void visitMethodTypeConstant(Clazz clazz, MethodTypeConstant methodTypeConstant);
    public void visitNameAndTypeConstant(Clazz clazz, NameAndTypeConstant nameAndTypeConstant);
}
