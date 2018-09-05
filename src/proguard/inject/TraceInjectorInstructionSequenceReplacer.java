package proguard.inject;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;
import proguard.optimize.peephole.*;

public class TraceInjectorInstructionSequenceReplacer extends InstructionSequenceReplacer
{
    public TraceInjectorInstructionSequenceReplacer(InstructionSequenceMatcher instructionSequenceMatcher,
                                                           Constant[]                 patternConstants,
                                                           Instruction[]              patternInstructions,
                                                           Constant[]                 replacementConstants,
                                                           Instruction[]              replacementInstructions,
                                                           BranchTargetFinder         branchTargetFinder,
                                                           CodeAttributeEditor        codeAttributeEditor,
                                                           InstructionVisitor         extraInstructionVisitor    )
    {
        super(instructionSequenceMatcher,
                patternConstants,
                patternInstructions,
                replacementConstants,
                replacementInstructions,
                branchTargetFinder,
                codeAttributeEditor,
                extraInstructionVisitor    );
    }


    public TraceInjectorInstructionSequenceReplacer(Constant[]          patternConstants,
                                                           Instruction[]       patternInstructions,
                                                           Constant[]          replacementConstants,
                                                           Instruction[]       replacementInstructions,
                                                           BranchTargetFinder  branchTargetFinder,
                                                           CodeAttributeEditor codeAttributeEditor     )
    {
        super(patternConstants,
                patternInstructions,
                replacementConstants,
                replacementInstructions,
                branchTargetFinder,
                codeAttributeEditor     );
    }


    public TraceInjectorInstructionSequenceReplacer(Constant[]          patternConstants,
                                                           Instruction[]       patternInstructions,
                                                           Constant[]          replacementConstants,
                                                           Instruction[]       replacementInstructions,
                                                           BranchTargetFinder  branchTargetFinder,
                                                           CodeAttributeEditor codeAttributeEditor,
                                                           InstructionVisitor  extraInstructionVisitor )
    {
        super(patternConstants,
                patternInstructions,
                replacementConstants,
                replacementInstructions,
                branchTargetFinder,
                codeAttributeEditor,
                extraInstructionVisitor );
    }


    @Override
    protected int matchedArgument(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int argument)
    {
        switch (argument)
        {
            case TraceInjectorInstructionSequenceConstants.LOCAL_VARIABLE_INDEX_1:
                return codeAttribute.u2maxLocals;
            case TraceInjectorInstructionSequenceConstants.LOCAL_VARIABLE_INDEX_2:
                return codeAttribute.u2maxLocals + 1;
            case TraceInjectorInstructionSequenceConstants.LOCAL_VARIABLE_INDEX_3:
                return codeAttribute.u2maxLocals + 2;
            default:
                return super.matchedArgument(clazz, argument);
        }
    }


    @Override
    protected int matchedConstantIndex(ProgramClass programClass, int constantIndex)
    {
        switch (constantIndex)
        {
            case TraceInjectorInstructionSequenceConstants.CLASS_NAME:
                return new ConstantPoolEditor(programClass)
                        .addStringConstant(ClassUtil.externalClassName(programClass.getName()), programClass, null);
            default:
                return super.matchedConstantIndex(programClass, constantIndex);
        }
    }
}
