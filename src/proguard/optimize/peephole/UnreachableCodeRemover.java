/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 GuardSquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.optimize.peephole;

import proguard.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This InstructionVisitor deletes blocks of code that can never be reached by
 * regular calls or branches.
 *
 * @author Eric Lafortune
 */
public class UnreachableCodeRemover
extends      SimplifiedVisitor
implements   AttributeVisitor,
             InstructionVisitor
{
    //*
    private static final boolean DEBUG = false;
    /*/
    private static       boolean DEBUG = false;
    //*/

    private final InstructionVisitor  extraInstructionVisitor;

    private final ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    private final CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();


    /**
     * Creates a new UnreachableCodeRemover.
     */
    public UnreachableCodeRemover()
    {
        this(null);
    }


    /**
     * Creates a new UnreachableCodeRemover.
     * @param extraInstructionVisitor an optional extra visitor for all
     *                                deleted instructions.
     */
    public UnreachableCodeRemover(InstructionVisitor  extraInstructionVisitor)
    {
        this.extraInstructionVisitor = extraInstructionVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
//        DEBUG =
//            clazz.getName().equals("abc/Def") &&
//            method.getName(clazz).equals("abc");

        // TODO: Remove this when the code has stabilized.
        // Catch any unexpected exceptions from the actual visiting method.
        try
        {
            // Process the code.
            visitCodeAttribute0(clazz, method, codeAttribute);
        }
        catch (RuntimeException ex)
        {
            Logger.err_println("Unexpected error while removing unreachable code:");
            Logger.err_println("  Class       = ["+clazz.getName()+"]");
            Logger.err_println("  Method      = ["+method.getName(clazz)+method.getDescriptor(clazz)+"]");
            Logger.err_println("  Exception   = ["+ex.getClass().getName()+"] ("+ex.getMessage()+")");

            throw ex;
        }
    }


    public void visitCodeAttribute0(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        if (DEBUG)
        {
            Logger.out_println("UnreachableCodeRemover: "+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz));
        }

        reachableCodeMarker.visitCodeAttribute(clazz, method, codeAttribute);

        codeAttributeEditor.reset(codeAttribute.u4codeLength);

        codeAttribute.instructionsAccept(clazz, method, this);

        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
        if (DEBUG)
        {
            Logger.out_println("  "+(reachableCodeMarker.isReachable(offset) ? "+" : "-")+" "+instruction.toString(offset));
        }

        // Is this instruction unreachable?
        if (!reachableCodeMarker.isReachable(offset))
        {
            // Then delete it.
            codeAttributeEditor.deleteInstruction(offset);

            // Visit the instruction, if required.
            if (extraInstructionVisitor != null)
            {
                instruction.accept(clazz, method, codeAttribute, offset, extraInstructionVisitor);
            }
        }
    }
}
