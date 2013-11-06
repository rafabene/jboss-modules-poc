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

package org.jboss.developer.modules.model;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class Jar {

    private File jarFile;
    private File rootPath;

    Jar() {
        // Default constructor to JAXB
    }

    /**
     * @param jarFile
     */
    public Jar(File rootPath, File jarFile) {
        if (rootPath.isFile()) {
            throw new IllegalArgumentException(String.format("Root Path should be a directory: %s", rootPath));
        }
        if (jarFile.isDirectory()) {
            throw new IllegalArgumentException(String.format("Jar should be a file: %s", jarFile));
        }
        this.rootPath = rootPath;
        this.jarFile = jarFile;
    }

    /**
     * @return the jarFile
     */

    public File getJarFile() {
        return jarFile;
    }

    @XmlAttribute(name = "file")
    public String getRelativePath() {
        String fullPath = jarFile.getAbsolutePath();
        String relativePath = fullPath.substring(rootPath.getAbsolutePath().length() + 1, fullPath.length());
        return relativePath;
    }

    /**
     * Get Maven (GroupId, ArtifactId and Version) GAV
     * 
     * @return null if no gav information
     * 
     * @throws IOException
     */
    @XmlElement(name = "gav")
    public Gav getGav() throws IOException {
        JarFile jarFile = new JarFile(this.getJarFile());
        try {
            Enumeration<JarEntry> entries = jarFile.entries();
            // For all Entries
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                // look for pom.properties
                if (jarEntry.getName().endsWith("pom.properties")) {
                    Gav gav = extractGavInformation(jarFile, jarEntry);
                    return gav;

                }
            }
            return null;
        } finally {
            jarFile.close();
        }
    }

    /**
     * 
     * This methos extract GAV (GroupdId, ArtifactId, Version) from the JAR file (pom.properties)
     * 
     * @param jarFile the JAR File
     * @param jarEntry entry for pom.properties
     * @return null if no GAV information found
     * @throws IOException
     */
    private static Gav extractGavInformation(JarFile jarFile, JarEntry jarEntry) throws IOException {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        StringReader stringReader = null;
        try {
            is = jarFile.getInputStream(jarEntry);
            bos = new ByteArrayOutputStream();
            int i = 0;
            while ((i = is.read()) != -1) {
                bos.write(i);
            }
            String pomContent = new String(bos.toByteArray());
            stringReader = new StringReader(pomContent);
            Properties p = new Properties();
            p.load(stringReader);
            return new Gav(p.getProperty("groupId"), p.getProperty("artifactId"), p.getProperty("version"));
        } finally {
            safeClose(is);
            safeClose(bos);
            safeClose(stringReader);
        }
    }

    private static void safeClose(final Closeable closeable) {
        if (closeable != null)
            try {
                closeable.close();
            } catch (Exception e) {
                // no-op
            }
    }

}
