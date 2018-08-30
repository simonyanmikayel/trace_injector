package proguard.classfile.inject;

import proguard.Configuration;
import proguard.classfile.ClassPool;
import proguard.classfile.visitor.ClassCleaner;
import proguard.obfuscate.ClassRenamer;

import java.io.IOException;

public class traceInjector {
    private final Configuration configuration;

    /**
     * Creates a new trace injector.
     */
    public traceInjector (Configuration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * Performs obfuscation of the given program class pool.
     */
    public void execute(ClassPool programClassPool,
                        ClassPool libraryClassPool) throws IOException
    {

        // Clean up any old visitor info.
        programClassPool.classesAccept(new ClassCleaner());
        if (configuration.injectTracesInLibraries)
            libraryClassPool.classesAccept(new ClassCleaner());

        // Actually do trace injection.
        programClassPool.classesAccept(new ClassTraceInjector());
        if (configuration.injectTracesInLibraries)
            libraryClassPool.classesAccept(new ClassTraceInjector());
    }
}
