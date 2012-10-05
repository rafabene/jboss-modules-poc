/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.jdf.modules.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ModulesFinder {

    private List<File> modulesDescriptors = new ArrayList<File>();

    public List<File> findModulesInPath(File rootPath) {
        if (!rootPath.isDirectory()) {
            throw new IllegalArgumentException(String.format("Path should be a directory: %s", rootPath));
        }
        scanDir(rootPath);

        try {
            return modulesDescriptors;
        } catch (Exception e) {
            throw new RuntimeException("Problem finding modules from xml files. Cause: " + e.getCause(), e);
        }
    }

    /**
     * Recursivally seeks for all
     * 
     * @param rootPath
     */
    private void scanDir(File rootPath) {
        for (File file : rootPath.listFiles()) {
            if (file.isDirectory()) {
                scanDir(file);
            } else {
                scanFile(file);
            }
        }
    }

    /**
     * @param file
     */
    private void scanFile(File file) {
        int indexLastDot = file.getName().lastIndexOf('.');
        // If file has extension
        if (indexLastDot >= 0) {
            String fileName = file.getName();
            String fileExtension = fileName.substring(indexLastDot + 1);
            // If the extension is xml
            if (fileExtension.equalsIgnoreCase("xml")) {
                modulesDescriptors.add(file);
            }
        }
    }
}
