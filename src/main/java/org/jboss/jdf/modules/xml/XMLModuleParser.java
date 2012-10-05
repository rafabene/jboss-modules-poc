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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.jboss.jdf.modules.model.BaseModule;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class XMLModuleParser {

    private XMLReader reader;

    private File rootPath;

    public XMLModuleParser(File rootPath) {
        try {
            this.rootPath = rootPath;
            reader = XMLReaderFactory.createXMLReader();
        } catch (Exception e) {
            throw new RuntimeException("Can't construct XMLModuleParser. Cause " + e.getMessage(), e);
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
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(xml));
        InputSource is = new InputSource(bis);
        ModulesHandler mh = new ModulesHandler(rootPath, xml);
        reader.setContentHandler(mh);
        reader.parse(is);
        return mh.getBaseModule();
    }

}
