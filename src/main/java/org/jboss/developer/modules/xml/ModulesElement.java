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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.developer.modules.model.BaseModule;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@XmlRootElement(name = "modules")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ModulesElement {

    private List<BaseModule> modules = new ArrayList<BaseModule>();
    
    private File rooPath;
    
    ModulesElement() {
        // Default Constructor to JAXB
    }
    
    
    /**
     * @param rooPath
     */
    public ModulesElement(File rooPath) {
        this.rooPath = rooPath;
    }
    
    
    /**
     * @return the rooPath
     */
    @XmlElement(name="root-path")
    public File getRooPath() {
        return rooPath;
    }



    /**
     * @return the module
     */
    @XmlAnyElement
    public List<BaseModule> getModules() {
        return modules;
    }
}
