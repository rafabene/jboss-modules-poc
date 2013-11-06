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

import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class BaseModule {

    private String name;

    private String slot = "main";

    private File sourceXML;

    private File rootPath;

    BaseModule() {
        // default constructor for JAXB
    }

    public BaseModule(File rootPath, String name, String slot, File sourceXML) {
        if (rootPath.isFile()) {
            throw new IllegalArgumentException(String.format("Root Path should be a directory: %s", rootPath));
        }
        this.rootPath = rootPath;
        this.name = name;
        this.slot = slot;
        this.sourceXML = sourceXML;
    }

    @XmlElement(name = "source-xml")
    public String getRelativePath() {
        // target modules from module-alias doesn't have sourceXML
        if (sourceXML != null) {
            String fullPath = sourceXML.getAbsolutePath();
            String relativePath = fullPath.substring(rootPath.getAbsolutePath().length() + 1, fullPath.length());
            return relativePath;
        }
        return null;
    }

    /**
     * The name of the module. This name must match the name of the module being loaded
     * 
     * @return
     */
    @XmlAttribute
    public String getName() {
        return name;
    }

    /**
     * The version slot. If not specified, defaults to "main".
     */
    @XmlAttribute
    public String getSlot() {
        if (this.slot == null || this.slot.isEmpty()) {
            return "main";
        }
        return slot;
    }

    /**
     * @return the sourceXML
     */
    @XmlTransient
    public File getSourceXML() {
        return sourceXML;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(this.getClass().getSimpleName() + " [name=%s, slot=%s] - XML: %s", getName(), getSlot(),
                getSourceXML());
    }

}
