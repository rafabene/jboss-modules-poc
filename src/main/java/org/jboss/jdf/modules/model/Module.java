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

package org.jboss.jdf.modules.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.jboss.jdf.modules.jar.Jar;
import org.jboss.jdf.modules.xml.PropertiesAdapter;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@XmlRootElement(name = "module")
@XmlType(name = "module", propOrder = { "privateModule", "mainClass", "properties", "resources", "packages" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Module extends BaseModule {

    private String mainClass;

    private Properties properties = new Properties();

    private List<Jar> resources = new ArrayList<Jar>();

    Module() {
        // default constructor for JAXB
    }

    /**
     * @param rootPath
     */
    public Module(File rootPath, String name, String slot, File sourceXML) {
        super(rootPath, name, slot, sourceXML);
    }

    /**
     * The main class of this module, if any.
     * 
     * @return
     */
    @XmlElement(name = "main-class")
    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    /**
     * The resources that make up this module.
     * 
     * @return
     */
    @XmlElementWrapper(name = "resources")
    @XmlElement(name = "jar")
    public List<Jar> getResources() {
        return resources;
    }

    /**
     * The modules API exposes a method which can read property (string key-value pair) values from a module. To specify values
     * for these properties you use the "properties" element which can contain zero or more "property" elements
     * 
     * @return
     */
    @XmlJavaTypeAdapter(PropertiesAdapter.class)
    public Properties getProperties() {
        return properties;
    }

    /**
     * This method queries the jboss.api property to determine if this is a private module or not
     * 
     * @return
     */
    @XmlAttribute(name = "private")
    public boolean isPrivateModule() {
        String propertyValue = this.getProperties().getProperty("jboss.api");
        return propertyValue != null && propertyValue.equals("private");
    }

    /**
     * This methos get all package names from a individual jar
     * 
     * @param jar
     * @return
     * @throws IOException
     */
    private Set<String> getPackagesFromResource(File jar) throws IOException {
        Set<String> packages = new TreeSet<String>();
        if (jar.isDirectory()) {
            throw new IOException(String.format("Parameter should be a file: %s", jar));
        }
        JarFile jarFile = new JarFile(jar);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) entries.nextElement();
            // get only package folders that has Classes
            if (jarEntry.getName().endsWith(".class")) {
                String className = jarEntry.getName().replaceAll("/", ".").replaceAll(".class", "");
                // Remove the class Name
                int i = className.lastIndexOf('.');
                if (i > 0) {
                    String packageName = className.substring(0, i);
                    packages.add(packageName);
                }
            }
        }
        return packages;
    }

    /**
     * This method gets all package names from all resources in this module
     * 
     * @return
     * @throws IOException
     */
    @XmlElementWrapper(name = "packages")
    @XmlElement(name = "package")
    public Set<String> getPackages() throws IOException {
        Set<String> packageNames = new TreeSet<String>();
        for (Jar jar : this.getResources()) {
            packageNames.addAll(getPackagesFromResource(jar.getJarFile()));
        }
        return packageNames;
    }
}
