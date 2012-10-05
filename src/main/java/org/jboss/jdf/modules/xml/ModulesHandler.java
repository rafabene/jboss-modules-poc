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

package org.jboss.jdf.modules.xml;

import java.io.File;

import org.jboss.jdf.modules.jar.Jar;
import org.jboss.jdf.modules.model.BaseModule;
import org.jboss.jdf.modules.model.Module;
import org.jboss.jdf.modules.model.ModuleAlias;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ModulesHandler extends DefaultHandler {

    private BaseModule baseModule;
    private Module module;
    private ModuleAlias moduleAlias;
    private File rootPath;
    private File sourceXML;
    private boolean parsingDependencies = false;

    /**
     * @param rootPath
     * @param xml
     */
    public ModulesHandler(File rootPath, File xml) {
        this.rootPath = rootPath;
        this.sourceXML = xml;
    }

    /**
     * @return the baseModule
     */
    public BaseModule getBaseModule() {
        return baseModule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
     * org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals("module") && !parsingDependencies) {
            String name = attributes.getValue("name");
            String slot = attributes.getValue("slot");
            module = new Module(rootPath, name, slot, sourceXML);
        }
        if (localName.equals("main-class")) {
            module.setMainClass(attributes.getValue("name"));
        }
        if (localName.equals("property")) {
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            module.getProperties().put(name, value);
        }
        if (localName.equals("resource-root")) {
            String jarName = attributes.getValue("path");
            File jarFile = new File(sourceXML.getParentFile(), jarName);
            // Evicts native/resources folders
            if (jarFile.isFile()) {
                module.getResources().add(new Jar(rootPath, jarFile));
            }
        }
        if (localName.equals("dependencies")) {
            parsingDependencies = true;
        }

        // Module Alias
        if (localName.equals("module-alias")) {
            String name = attributes.getValue("name");
            String slot = attributes.getValue("slot");
            String targetName = attributes.getValue("target-name");
            String targetSlot = attributes.getValue("target-slot");
            BaseModule target = new BaseModule(rootPath, targetName, targetSlot, null);
            moduleAlias = new ModuleAlias(rootPath, name, slot, sourceXML, target);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("dependencies")) {
            parsingDependencies = false;
        }
        if (localName.equals("module") && !parsingDependencies) {
            baseModule = module;
        }
        if (localName.equals("module-alias")) {
            baseModule = moduleAlias;
        }
    }
}
