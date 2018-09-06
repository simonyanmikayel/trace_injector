package proguard.inject;


import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.*;
import proguard.optimize.peephole.InstructionSequenceReplacer;

import static proguard.optimize.peephole.InstructionSequenceReplacer.catch_;

public class TraceInjectorInstructionSequenceConstants
{
    private static String LOGGER_CLASS_NAME = ClassUtil.internalClassName(FlowTracer.class.getName());

    // Exceptions classes.
    public static final String NAME_CLASS_NOT_FOUND_EXCEPTION     = "java/lang/ClassNotFoundException";
    public static final String NAME_NO_SUCH_FIELD_EXCEPTION       = "java/lang/NoSuchFieldException";
    public static final String NAME_NO_SUCH_METHOD_EXCEPTION      = "java/lang/NoSuchMethodException";
    public static final String NAME_RUNTIME_EXCEPTION             = "java/lang/RuntimeException";
    public static final String NAME_UNSATISFIED_LINK_ERROR        = "java/lang/UnsatisfiedLinkError";
    public static final String NAME_IO_EXCEPTION                  = "java/io/IOException";

    // Matched constants.
    public static final int CLASS_NAME             = 0x30000000;
    public static final int LOCAL_VARIABLE_INDEX_1 = 0x30000001;
    public static final int LOCAL_VARIABLE_INDEX_2 = 0x30000002;
    public static final int LOCAL_VARIABLE_INDEX_3 = 0x30000003;

    public static final int CONSTANT_INDEX = InstructionSequenceMatcher.X;
    public static final int ACCESS_MODE    = InstructionSequenceMatcher.Y;

    public final Instruction[][][] RESOURCE;
    public final Constant[]        CONSTANTS;

    // Labels.
    private final InstructionSequenceReplacer.Label TRY_START = InstructionSequenceReplacer.label();
    private final InstructionSequenceReplacer.Label TRY_END   = InstructionSequenceReplacer.label();
    private final InstructionSequenceReplacer.Label CATCH_END = InstructionSequenceReplacer.label();

    private final InstructionSequenceReplacer.Label CLASS_NOT_FOUND_EXCEPTION;
    private final InstructionSequenceReplacer.Label NO_SUCH_METHOD_EXCEPTION;
    private final InstructionSequenceReplacer.Label NO_SUCH_FIELD_EXCEPTION;
    private final InstructionSequenceReplacer.Label IO_EXCEPTION;
    private final InstructionSequenceReplacer.Label RUNTIME_EXCEPTION;
    private final InstructionSequenceReplacer.Label UNSATISFIED_LINK_ERROR;

    /**
     * Creates a new instance of ResourceIdInstructionSequenceConstants,
     * with constants that reference classes from the given class pools.
     */
    public TraceInjectorInstructionSequenceConstants(ClassPool programClassPool,
                                                            ClassPool libraryClassPool)
    {
        InstructionSequenceBuilder ____ =
                new InstructionSequenceBuilder(programClassPool, libraryClassPool);

        ConstantPoolEditor constantPoolEditor = ____.getConstantPoolEditor();

        CLASS_NOT_FOUND_EXCEPTION =
                catch_(TRY_START.offset(),
                        TRY_END.offset(),
                        constantPoolEditor.addClassConstant(NAME_CLASS_NOT_FOUND_EXCEPTION, null));
        NO_SUCH_METHOD_EXCEPTION =
                catch_(TRY_START.offset(),
                        TRY_END.offset(),
                        constantPoolEditor.addClassConstant(NAME_NO_SUCH_METHOD_EXCEPTION, null));
        NO_SUCH_FIELD_EXCEPTION =
                catch_(TRY_START.offset(),
                        TRY_END.offset(),
                        constantPoolEditor.addClassConstant(NAME_NO_SUCH_FIELD_EXCEPTION, null));
        IO_EXCEPTION =
                catch_(TRY_START.offset(),
                        TRY_END.offset(),
                        constantPoolEditor.addClassConstant(NAME_IO_EXCEPTION, null));
        RUNTIME_EXCEPTION =
                catch_(TRY_START.offset(),
                        TRY_END.offset(),
                        constantPoolEditor.addClassConstant(NAME_RUNTIME_EXCEPTION, null));
        UNSATISFIED_LINK_ERROR =
                catch_(TRY_START.offset(),
                        TRY_END.offset(),
                        constantPoolEditor.addClassConstant(NAME_UNSATISFIED_LINK_ERROR, null));

        RESOURCE = new Instruction[][][]
                {
                        // Classes.
                        {
                                ____.invokestatic("java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;").__(),

                                ____.dup()
                                        .astore(LOCAL_VARIABLE_INDEX_1)
                                        .label(TRY_START)
                                        .invokestatic("java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;")
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(CLASS_NOT_FOUND_EXCEPTION)
                                        .ldc_(CLASS_NAME)
                                        .aload(LOCAL_VARIABLE_INDEX_1)
                                        .invokestatic(LOGGER_CLASS_NAME, "logForName", "(Ljava/lang/String;Ljava/lang/String;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },
                        {
                                ____.invokestatic("java/lang/Class", "forName", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;").__(),

                                ____.dup_x2()
                                        .pop()
                                        .dup_x2()
                                        .pop()
                                        .dup_x2()
                                        .astore(LOCAL_VARIABLE_INDEX_1)
                                        .label(TRY_START)
                                        .invokestatic("java/lang/Class", "forName", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;")
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(CLASS_NOT_FOUND_EXCEPTION)
                                        .ldc_(CLASS_NAME)
                                        .aload(LOCAL_VARIABLE_INDEX_1)
                                        .invokestatic(LOGGER_CLASS_NAME, "logForName", "(Ljava/lang/String;Ljava/lang/String;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },
                        {
                                ____.invokevirtual("java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;").__(),

                                ____.dup()
                                        .astore(LOCAL_VARIABLE_INDEX_1)
                                        .label(TRY_START)
                                        .invokevirtual("java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;")
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(CLASS_NOT_FOUND_EXCEPTION)
                                        .ldc_(CLASS_NAME)
                                        .aload(LOCAL_VARIABLE_INDEX_1)
                                        .invokestatic(LOGGER_CLASS_NAME, "logLoadClass", "(Ljava/lang/String;Ljava/lang/String;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },

                        // Constructors.
                        {
                                ____.invokevirtual("java/lang/Class", "getDeclaredConstructor", "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;").__(),

                                ____.dup_x1()
                                        .astore(LOCAL_VARIABLE_INDEX_2)
                                        .dup_x1()
                                        .astore(LOCAL_VARIABLE_INDEX_1)
                                        .label(TRY_START)
                                        .invokevirtual("java/lang/Class", "getDeclaredConstructor", "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;")
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(NO_SUCH_METHOD_EXCEPTION)
                                        .ldc_(CLASS_NAME)
                                        .aload(LOCAL_VARIABLE_INDEX_1)
                                        .aload(LOCAL_VARIABLE_INDEX_2)
                                        .invokestatic(LOGGER_CLASS_NAME, "logGetDeclaredConstructor", "(Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Class;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },
                        {
                                ____.invokevirtual("java/lang/Class", "getConstructor", "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;").__(),

                                ____.dup_x1()
                                        .astore(LOCAL_VARIABLE_INDEX_2)
                                        .dup_x1()
                                        .astore(LOCAL_VARIABLE_INDEX_1)
                                        .label(TRY_START)
                                        .invokevirtual("java/lang/Class", "getConstructor", "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;")
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(NO_SUCH_METHOD_EXCEPTION)
                                        .ldc_(CLASS_NAME)
                                        .aload(LOCAL_VARIABLE_INDEX_1)
                                        .aload(LOCAL_VARIABLE_INDEX_2)
                                        .invokestatic(LOGGER_CLASS_NAME, "logGetConstructor", "(Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Class;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },
                        {
                                ____.invokevirtual("java/lang/Class", "getDeclaredConstructors", "()[Ljava/lang/reflect/Constructor;").__(),

                                ____.dup()
                                        .ldc_(CLASS_NAME)
                                        .swap()
                                        .invokestatic(LOGGER_CLASS_NAME, "logGetDeclaredConstructors", "(Ljava/lang/String;Ljava/lang/Class;)V")
                                        .invokevirtual("java/lang/Class", "getDeclaredConstructors", "()[Ljava/lang/reflect/Constructor;").__()
                        },
                        {
                                ____.invokevirtual("java/lang/Class", "getConstructors", "()[Ljava/lang/reflect/Constructor;").__(),

                                ____.dup()
                                        .ldc_(CLASS_NAME)
                                        .swap()
                                        .invokestatic(LOGGER_CLASS_NAME, "logGetConstructors", "(Ljava/lang/String;Ljava/lang/Class;)V")
                                        .invokevirtual("java/lang/Class", "getConstructors", "()[Ljava/lang/reflect/Constructor;").__()
                        },

                        // Methods.

                        {
                                ____.invokevirtual("java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;").__(),

                                ____.dup_x2()
                                        .astore(LOCAL_VARIABLE_INDEX_3)
                                        .dup_x2()
                                        .astore(LOCAL_VARIABLE_INDEX_2)
                                        .dup_x2()
                                        .astore(LOCAL_VARIABLE_INDEX_1)
                                        .label(TRY_START)
                                        .invokevirtual("java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;")
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(NO_SUCH_METHOD_EXCEPTION)
                                        .ldc_(CLASS_NAME)
                                        .aload(LOCAL_VARIABLE_INDEX_1)
                                        .aload(LOCAL_VARIABLE_INDEX_2)
                                        .aload(LOCAL_VARIABLE_INDEX_3)
                                        .invokestatic(LOGGER_CLASS_NAME, "logGetDeclaredMethod", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },
                        {
                                ____.invokevirtual("java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;").__(),

                                ____.dup_x2()
                                        .astore(LOCAL_VARIABLE_INDEX_3)
                                        .dup_x2()
                                        .astore(LOCAL_VARIABLE_INDEX_2)
                                        .dup_x2()
                                        .astore(LOCAL_VARIABLE_INDEX_1)
                                        .label(TRY_START)
                                        .invokevirtual("java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;")
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(NO_SUCH_METHOD_EXCEPTION)
                                        .ldc_(CLASS_NAME)
                                        .aload(LOCAL_VARIABLE_INDEX_1)
                                        .aload(LOCAL_VARIABLE_INDEX_2)
                                        .aload(LOCAL_VARIABLE_INDEX_3)
                                        .invokestatic(LOGGER_CLASS_NAME, "logGetMethod", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },
                        {
                                ____.invokevirtual("java/lang/Class", "getDeclaredMethods", "()[Ljava/lang/reflect/Method;").__(),

                                ____.dup()
                                        .ldc_(CLASS_NAME)
                                        .swap()
                                        .invokestatic(LOGGER_CLASS_NAME, "logGetDeclaredMethods", "(Ljava/lang/String;Ljava/lang/Class;)V")
                                        .invokevirtual("java/lang/Class", "getDeclaredMethods", "()[Ljava/lang/reflect/Method;").__()
                        },
                        {
                                ____.invokevirtual("java/lang/Class", "getMethods", "()[Ljava/lang/reflect/Method;").__(),

                                ____.dup()
                                        .ldc_(CLASS_NAME)
                                        .swap()
                                        .invokestatic(LOGGER_CLASS_NAME, "logGetMethods", "(Ljava/lang/String;Ljava/lang/Class;)V")
                                        .invokevirtual("java/lang/Class", "getMethods", "()[Ljava/lang/reflect/Method;").__()
                        },

                        // Fields.

                        {
                                ____.invokevirtual("java/lang/Class", "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;").__(),

                                ____.dup_x1()
                                        .astore(LOCAL_VARIABLE_INDEX_2)
                                        .dup_x1()
                                        .astore(LOCAL_VARIABLE_INDEX_1)
                                        .label(TRY_START)
                                        .invokevirtual("java/lang/Class", "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;")
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(NO_SUCH_FIELD_EXCEPTION)
                                        .ldc_(CLASS_NAME)
                                        .aload(LOCAL_VARIABLE_INDEX_1)
                                        .aload(LOCAL_VARIABLE_INDEX_2)
                                        .invokestatic(LOGGER_CLASS_NAME, "logGetDeclaredField", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/String;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },
                        {
                                ____.invokevirtual("java/lang/Class", "getField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;").__(),

                                ____.dup_x1()
                                        .astore(LOCAL_VARIABLE_INDEX_2)
                                        .dup_x1()
                                        .astore(LOCAL_VARIABLE_INDEX_1)
                                        .label(TRY_START)
                                        .invokevirtual("java/lang/Class", "getField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;")
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(NO_SUCH_FIELD_EXCEPTION)
                                        .ldc_(CLASS_NAME)
                                        .aload(LOCAL_VARIABLE_INDEX_1)
                                        .aload(LOCAL_VARIABLE_INDEX_2)
                                        .invokestatic(LOGGER_CLASS_NAME, "logGetField", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/String;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },
                        {
                                ____.invokevirtual("java/lang/Class", "getDeclaredFields", "()[Ljava/lang/reflect/Field;").__(),

                                ____.dup()
                                        .ldc_(CLASS_NAME)
                                        .swap()
                                        .invokestatic(LOGGER_CLASS_NAME, "logGetDeclaredFields", "(Ljava/lang/String;Ljava/lang/Class;)V")
                                        .invokevirtual("java/lang/Class", "getDeclaredFields", "()[Ljava/lang/reflect/Field;").__()
                        },
                        {
                                ____.invokevirtual("java/lang/Class", "getFields", "()[Ljava/lang/reflect/Field;").__(),

                                ____.dup()
                                        .ldc_(CLASS_NAME)
                                        .swap()
                                        .invokestatic(LOGGER_CLASS_NAME, "logGetFields", "(Ljava/lang/String;Ljava/lang/Class;)V")
                                        .invokevirtual("java/lang/Class", "getFields", "()[Ljava/lang/reflect/Field;").__()
                        },

                        // Resource files.

                        // System.loadLibrary(String)
                        {
                                ____.ldc_(CONSTANT_INDEX)
                                        .invokestatic(ClassConstants.NAME_JAVA_LANG_SYSTEM,
                                                ClassConstants.METHOD_NAME_LOAD_LIBRARY,
                                                ClassConstants.METHOD_TYPE_LOAD_LIBRARY).__(),

                                ____.ldc_(CONSTANT_INDEX)
                                        .label(TRY_START)
                                        .invokestatic(ClassConstants.NAME_JAVA_LANG_SYSTEM,
                                                ClassConstants.METHOD_NAME_LOAD_LIBRARY,
                                                ClassConstants.METHOD_TYPE_LOAD_LIBRARY)
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(UNSATISFIED_LINK_ERROR)
                                        .ldc_(CLASS_NAME)
                                        .ldc_(CONSTANT_INDEX)
                                        .invokestatic(LOGGER_CLASS_NAME, "logLoadLibrary", "(Ljava/lang/String;Ljava/lang/String;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },
                        {
                                ____.invokestatic(ClassConstants.NAME_JAVA_LANG_SYSTEM,
                                        ClassConstants.METHOD_NAME_LOAD_LIBRARY,
                                        ClassConstants.METHOD_TYPE_LOAD_LIBRARY).__(),

                                ____.dup()
                                        .astore(LOCAL_VARIABLE_INDEX_1)
                                        .label(TRY_START)
                                        .invokestatic(ClassConstants.NAME_JAVA_LANG_SYSTEM,
                                                ClassConstants.METHOD_NAME_LOAD_LIBRARY,
                                                ClassConstants.METHOD_TYPE_LOAD_LIBRARY)
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(UNSATISFIED_LINK_ERROR)
                                        .ldc_(CLASS_NAME)
                                        .aload(LOCAL_VARIABLE_INDEX_1)
                                        .invokestatic(LOGGER_CLASS_NAME, "logLoadLibrary", "(Ljava/lang/String;Ljava/lang/String;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },

                        // System.load(String)
                        {
                                ____.ldc_(CONSTANT_INDEX)
                                        .invokestatic(ClassConstants.NAME_JAVA_LANG_SYSTEM,
                                                ClassConstants.METHOD_NAME_LOAD,
                                                ClassConstants.METHOD_TYPE_LOAD).__(),

                                ____.ldc_(CONSTANT_INDEX)
                                        .label(TRY_START)
                                        .invokestatic(ClassConstants.NAME_JAVA_LANG_SYSTEM,
                                                ClassConstants.METHOD_NAME_LOAD,
                                                ClassConstants.METHOD_TYPE_LOAD)
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(UNSATISFIED_LINK_ERROR)
                                        .ldc_(CLASS_NAME)
                                        .ldc_(CONSTANT_INDEX)
                                        .invokestatic(LOGGER_CLASS_NAME, "logLoad", "(Ljava/lang/String;Ljava/lang/String;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },
                        {
                                ____.invokestatic(ClassConstants.NAME_JAVA_LANG_SYSTEM,
                                        ClassConstants.METHOD_NAME_LOAD,
                                        ClassConstants.METHOD_TYPE_LOAD).__(),

                                ____.dup()
                                        .astore(LOCAL_VARIABLE_INDEX_1)
                                        .label(TRY_START)
                                        .invokestatic(ClassConstants.NAME_JAVA_LANG_SYSTEM,
                                                ClassConstants.METHOD_NAME_LOAD,
                                                ClassConstants.METHOD_TYPE_LOAD)
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(UNSATISFIED_LINK_ERROR)
                                        .ldc_(CLASS_NAME)
                                        .aload(LOCAL_VARIABLE_INDEX_1)
                                        .invokestatic(LOGGER_CLASS_NAME, "logLoad", "(Ljava/lang/String;Ljava/lang/String;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },

                        // Runtime.loadLibrary(String)
                        {
                                ____.ldc_(CONSTANT_INDEX)
                                        .invokestatic(ClassConstants.NAME_JAVA_LANG_RUNTIME,
                                                ClassConstants.METHOD_NAME_LOAD_LIBRARY,
                                                ClassConstants.METHOD_TYPE_LOAD_LIBRARY).__(),

                                ____.ldc_(CONSTANT_INDEX)
                                        .label(TRY_START)
                                        .invokestatic(ClassConstants.NAME_JAVA_LANG_RUNTIME,
                                                ClassConstants.METHOD_NAME_LOAD_LIBRARY,
                                                ClassConstants.METHOD_TYPE_LOAD_LIBRARY)
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(UNSATISFIED_LINK_ERROR)
                                        .ldc_(CLASS_NAME)
                                        .ldc_(CONSTANT_INDEX)
                                        .invokestatic(LOGGER_CLASS_NAME, "logLoadLibrary", "(Ljava/lang/String;Ljava/lang/String;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },
                        {
                                ____.invokestatic(ClassConstants.NAME_JAVA_LANG_RUNTIME,
                                        ClassConstants.METHOD_NAME_LOAD_LIBRARY,
                                        ClassConstants.METHOD_TYPE_LOAD_LIBRARY).__(),

                                ____.dup()
                                        .astore(LOCAL_VARIABLE_INDEX_1)
                                        .label(TRY_START)
                                        .invokestatic(ClassConstants.NAME_JAVA_LANG_RUNTIME,
                                                ClassConstants.METHOD_NAME_LOAD_LIBRARY,
                                                ClassConstants.METHOD_TYPE_LOAD_LIBRARY)
                                        .label(TRY_END)
                                        .goto_(CATCH_END.offset())
                                        .catch_(UNSATISFIED_LINK_ERROR)
                                        .ldc_(CLASS_NAME)
                                        .aload(LOCAL_VARIABLE_INDEX_1)
                                        .invokestatic(LOGGER_CLASS_NAME, "logLoadLibrary", "(Ljava/lang/String;Ljava/lang/String;)V")
                                        .athrow()
                                        .label(CATCH_END).__()
                        },
                };

        CONSTANTS = ____.constants();
    }
}
