package proguard.inject;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.util.InstructionSequenceMatcher;

public class TraceInjectorInstructionSequenceMatcher extends InstructionSequenceMatcher {

    private static boolean DEBUG = true;
    private static boolean TEST = true;

    public TraceInjectorInstructionSequenceMatcher(Constant[] patternConstants, Instruction[] patternInstructions) {
        super(patternConstants, patternInstructions);
    }

    @Override
    protected boolean  matchingOpcodes(Instruction instruction1,
                                      Instruction instruction2)
    {
        if (TEST)
            return super.matchingOpcodes(instruction1, instruction2);
        return true;
    }

    @Override
    protected boolean matchingArguments(int argument1,
                                        int argument2)
    {
        if (TEST)
            return super.matchingArguments(argument1, argument2);
        return true;
    }

}
