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
package proguard;

import proguard.classfile.ClassPool;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.ClassVersionSetter;

import java.io.IOException;
import java.util.*;

/**
 * This class sets the target version on program classes.
 *
 * @author Eric Lafortune
 */
public class Targeter
{
    private final Configuration configuration;


    /**
     * Creates a new Targeter to set the target version on program classes
     * according to the given configuration.
     */
    public Targeter(Configuration configuration)
    {
        this.configuration = configuration;
    }


    /**
     * Sets the target version on classes in the given program class pool.
     */
    public void execute(ClassPool programClassPool) throws IOException
    {
        Set newerClassVersions = configuration.warn != null ? null : new HashSet();

        programClassPool.classesAccept(new ClassVersionSetter(configuration.targetClassVersion,
                                                              newerClassVersions));

        if (newerClassVersions != null &&
            newerClassVersions.size() > 0)
        {
            Logger.err_print("Warning: some classes have more recent versions (");

            Iterator iterator = newerClassVersions.iterator();
            while (iterator.hasNext())
            {
                Integer classVersion = (Integer)iterator.next();
                Logger.err_print(ClassUtil.externalClassVersion(classVersion.intValue()));

                if (iterator.hasNext())
                {
                    Logger.err_print(",");
                }
            }

            Logger.err_println(")");
            Logger.err_println("         than the target version ("+ClassUtil.externalClassVersion(configuration.targetClassVersion)+").");

            if (!configuration.ignoreWarnings)
            {
                Logger.err_println("         If you are sure this is not a problem,");
                Logger.err_println("         you could try your luck using the '-ignorewarnings' option.");
                throw new IOException("Please correct the above warnings first.");
            }
        }
    }
}
