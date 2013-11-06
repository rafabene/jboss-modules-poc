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

package org.jboss.developer.modules.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.developer.modules.model.BaseModule;
import org.jboss.developer.modules.model.Filter;
import org.jboss.developer.modules.model.Jar;
import org.jboss.developer.modules.model.Module;
import org.jboss.developer.modules.model.ModuleAlias;
import org.jboss.developer.modules.model.ModuleDependency;
import org.jboss.developer.modules.model.Services;
import org.jboss.developer.modules.model.SystemDependency;
import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class XMLModuleParser {

    private static final Logger log = Logger.getLogger(XMLModuleParser.class.getName());

    private DocumentBuilder documentBuilder;

    private File rootPath;

    public XMLModuleParser(File rootPath) {
        try {
            this.rootPath = rootPath;
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(false);
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/namespaces", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/validation", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (Exception e) {
            throw new RuntimeException("Can't construct XPathExtractor. Cause " + e.getMessage(), e);
        }
    }

    /**
     * Parses the XML file and create a wrapper that permits some operations on it.
     * 
     * @param xml file
     * @return module.xml wrapper with some operations
     * @throws SAXException
     * @throws IOException
     */
    public BaseModule parse(File xml) throws SAXException, IOException {
        Document document = documentBuilder.parse(xml);
        log.tracef("XML %s parsed. Populating info", xml);
        if (document.getDocumentElement().getNodeName().equals("module")) {
            return createModule(xml, document);
        } else if (document.getDocumentElement().getNodeName().equals("module-alias")) {
            return createModuleAlias(xml, document);
        } else {
            throw new IllegalArgumentException(xml + " is not a module descriptor");
        }
    }

    /**
     * Create ModuleAlias
     * 
     * @param xml
     * @param document
     * @return
     */
    private ModuleAlias createModuleAlias(File sourceXML, Document document) {
        String name = document.getDocumentElement().getAttribute("name");
        String slot = document.getDocumentElement().getAttribute("slot");
        String targetName = document.getDocumentElement().getAttribute("target-name");
        String targetSlot = document.getDocumentElement().getAttribute("target-slot");
        BaseModule target = new BaseModule(rootPath, targetName, targetSlot, null);
        ModuleAlias moduleAlias = new ModuleAlias(rootPath, name, slot, sourceXML, target);
        log.tracef("%s populated", moduleAlias);
        return moduleAlias;
    }

    /**
     * Create Modules
     * 
     * @param sourceXML
     * @param document
     * @return
     */
    private Module createModule(File sourceXML, Document document) {
        String name = document.getDocumentElement().getAttribute("name");
        String slot = document.getDocumentElement().getAttribute("slot");

        Module module = new Module(rootPath, name, slot, sourceXML);

        module.setMainClass(getModuleMainClass(document));
        module.setExports(getFilter(document.getDocumentElement(), "exports"));

        fillModuleProperties(document, module);
        fillModuleResources(sourceXML.getParentFile(), document, module);
        fillModuleDependencies(document, module);
        log.tracef("%s populated", module);
        return module;
    }

    /**
     * @param document
     * @return
     */
    private Filter getFilter(Element rootElement, String tagName) {
        Filter filter = null;
        List<Element> exports = getChildrenByTagName(rootElement, tagName);

        // Iterate over all exports - Should be just one at root
        for (Element exportElement : exports) {
            // Just create filter if we have a exports section
            if (filter == null) {
                filter = new Filter();
            }
            // Get exclude paths
            List<Element> excludes = getChildrenByTagName(exportElement, "exclude");
            for (Element exclude : excludes) {
                filter.getExclude().add(exclude.getAttribute("path"));
            }
            // Get exclude-set paths
            excludes = getChildrenByTagName(exportElement, "exclude-set");
            for (Element excludeset : excludes) {
                List<Element> pathElements = getChildrenByTagName(excludeset, "path");
                for (Element path : pathElements) {
                    filter.getExcludeset().add(path.getAttribute("name"));
                }
            }
            // Get include paths
            List<Element> includes = getChildrenByTagName(exportElement, "include");
            for (Element include : includes) {
                filter.getInclude().add(include.getAttribute("path"));
            }
            // Get include-set paths
            includes = getChildrenByTagName(exportElement, "include-set");
            for (Element includeset : includes) {
                List<Element> pathElements = getChildrenByTagName(includeset, "path");
                for (Element path : pathElements) {
                    filter.getIncludeset().add(path.getAttribute("name"));
                }
            }

        }
        return filter;
    }

    /**
     * Get the dependencies (System/Module) from a Module
     * 
     * @param document
     * @param module
     */
    private void fillModuleDependencies(Document document, Module module) {
        List<Element> dependenciesElements = getChildrenByTagName(document.getDocumentElement(), "dependencies");
        // Iterate over all dependencies Elements, Should be just one on root
        for (Element dependenciesElement : dependenciesElements) {
            List<Element> moduleElements = getChildrenByTagName(dependenciesElement, "module");
            // Get all Modules Dependencies information
            for (Element moduleDependencyElement : moduleElements) {
                module.getModuleDependencies().add(createModuleDependency(moduleDependencyElement));
            }
            List<Element> systemElements = getChildrenByTagName(dependenciesElement, "system");
            for (Element systemElement : systemElements) {
                module.getSystemDependencies().add(createSystemDependency(systemElement));
            }
        }

    }

    /**
     * Extract information from <dependencies><system> element
     * 
     * @param systemElement
     * @return
     */
    private SystemDependency createSystemDependency(Element systemElement) {
        SystemDependency systemDependency = new SystemDependency();
        systemDependency.setExport(Boolean.parseBoolean(systemElement.getAttribute("export")));
        List<Element> pathsElements = getChildrenByTagName(systemElement, "paths");
        for (Element pathsElement : pathsElements) {
            List<Element> pathElements = getChildrenByTagName(pathsElement, "path");
            for (Element path : pathElements) {
                systemDependency.getPaths().add(path.getAttribute("name"));
            }
        }
        systemDependency.setExports(getFilter(systemElement, "exports"));
        return systemDependency;
    }

    /**
     * Extracts information from <dependencies><module> element
     * 
     * @param moduleDependency
     * @return
     */
    private ModuleDependency createModuleDependency(Element moduleDependencyElement) {
        ModuleDependency moduleDependency = new ModuleDependency();
        moduleDependency.setName(moduleDependencyElement.getAttribute("name"));
        moduleDependency.setSlot(moduleDependencyElement.getAttribute("slot"));
        moduleDependency.setExport(Boolean.parseBoolean(moduleDependencyElement.getAttribute("export")));
        String services = moduleDependencyElement.getAttribute("services");
        if (services != null && !services.isEmpty()) {
            moduleDependency.setServices(Services.valueOf(services.toUpperCase()));
        }
        moduleDependency.setOptional(Boolean.parseBoolean(moduleDependencyElement.getAttribute("optional")));
        moduleDependency.setExports(getFilter(moduleDependencyElement, "exports"));
        moduleDependency.setImports(getFilter(moduleDependencyElement, "imports"));
        return moduleDependency;
    }

    /**
     * Get the resources from module
     * 
     * @param moduleFolder - module folder (where module.xml was found)
     * @param document
     * @param module
     */
    private void fillModuleResources(File moduleFolder, Document document, Module module) {
        if (!moduleFolder.isDirectory()) {
            throw new IllegalArgumentException(moduleFolder + " is not a directory");
        }
        List<Element> resourcesElements = getChildrenByTagName(document.getDocumentElement(), "resources");
        // Iterate over resources elements, Should be just one on root
        for (Element resourcesElement : resourcesElements) {
            List<Element> resourceRootElements = getChildrenByTagName(resourcesElement, "resource-root");
            // Iterate over resource-root elements from each resources
            for (Element resourceRootElement : resourceRootElements) {
                String resourcePath = resourceRootElement.getAttribute("path");
                File resourceFile = new File(moduleFolder, resourcePath);
                // Evicts native/resources folders
                if (resourceFile.isFile()) {
                    module.getResources().add(new Jar(rootPath, resourceFile));
                }
            }
        }
    }

    /**
     * 
     * Fill module with the optional properties/property elements
     * 
     * @param document
     * @param module
     */
    private void fillModuleProperties(Document document, Module module) {
        List<Element> propertiesElements = getChildrenByTagName(document.getDocumentElement(), "properties");
        // All <properties> iteration - Should be just one at root
        for (Element properties : propertiesElements) {
            List<Element> propertyElements = getChildrenByTagName(properties, "property");
            // All <property> iteration from each <properties>
            for (Element property : propertyElements) {
                String propertyName = property.getAttribute("name");
                String propertyValue = property.getAttribute("value");
                module.getProperties().put(propertyName, propertyValue);
            }
        }
    }

    /**
     * Get the optional main-class element from module
     * 
     * @param document
     * @return
     */
    private String getModuleMainClass(Document document) {
        List<Element> mainClassElements = getChildrenByTagName(document.getDocumentElement(), "main-class");
        // Return just the first main-class occurrence
        for (Element mainClassElement : mainClassElements) {
            return mainClassElement.getAttributes().getNamedItem("name").getNodeValue();
        }
        return null;
    }

    /**
     * Helper methos to get only child elements from a parent;
     * 
     * @param parent
     * @param name
     * @return
     */
    private List<Element> getChildrenByTagName(Element parent, String name) {
        List<Element> nodeList = new ArrayList<Element>();
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE && name.equals(child.getNodeName())) {
                nodeList.add((Element) child);
            }
        }

        return nodeList;
    }

}
