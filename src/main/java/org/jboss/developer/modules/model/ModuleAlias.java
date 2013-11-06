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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@XmlRootElement(name = "module-alias")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ModuleAlias extends BaseModule {

    private BaseModule target;

    ModuleAlias() {
        // default constructor for JAXB
    }

    /**
     * @param rootPath
     */
    public ModuleAlias(File rootPath, String name, String slot, File sourceXML, BaseModule target) {
        super(rootPath, name, slot, sourceXML);
        this.target = target;
    }

    /**
     * @return the target
     */
    @XmlElement
    public BaseModule getTarget() {
        return target;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("ModuleAlias [name=%s, slot=%s, target-name=%s, target-slot=%s]", getName(), getSlot(),
                target.getName(), target.getSlot());
    }

}
