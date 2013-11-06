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

package org.jboss.developer.modules;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.jboss.developer.modules.io.ModulesFinder;
import org.jboss.developer.modules.model.BaseModule;
import org.jboss.developer.modules.model.Module;
import org.jboss.developer.modules.model.ModuleAlias;
import org.jboss.developer.modules.xml.ModulesElement;
import org.jboss.developer.modules.xml.XMLModuleParser;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ModulesInformationBuilder {

    private List<File> modulesDescriptors;

    private File rootPath;

    private static Map<File, ModulesInformationBuilder> instances = new HashMap<File, ModulesInformationBuilder>();

    private ModulesInformationBuilder(File path) {
        if (!path.isDirectory()) {
            throw new IllegalArgumentException(String.format("Path %s should point to a directory!", path));
        }
        this.rootPath = path;
        ModulesFinder finder = new ModulesFinder();
        modulesDescriptors = finder.findModulesInPath(path);
    }

    public static ModulesInformationBuilder getInstance(File path) {
        if (instances.get(path) == null) {
            ModulesInformationBuilder instance = new ModulesInformationBuilder(path);
            instances.put(path, instance);
        }
        return instances.get(path);
    }

    public List<BaseModule> build() throws BuildException {
        List<BaseModule> modules = new ArrayList<BaseModule>();
        try {
            XMLModuleParser parser = new XMLModuleParser(rootPath);
            for (File descriptor : modulesDescriptors) {
                modules.add(parser.parse(descriptor));
            }
        } catch (Exception e) {
            throw new BuildException("Can't build Modules Information", e);
        }
        return modules;
    }

    public String buildXML() throws BuildException {
        try {
            JAXBContext context = JAXBContext.newInstance(ModulesElement.class, Module.class, ModuleAlias.class);
            Marshaller marshaller = context.createMarshaller();
            StringWriter sw = new StringWriter();
            ModulesElement me = new ModulesElement(rootPath);

            me.getModules().addAll(build());
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(me, sw);

            return sw.toString();
        } catch (Exception e) {
            throw new BuildException("Can't build Modules Information", e);
        }
    }
}
