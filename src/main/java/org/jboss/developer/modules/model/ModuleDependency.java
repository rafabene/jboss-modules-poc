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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@XmlRootElement(name = "dependency")
@XmlType(name = "dependency", propOrder = { "name", "slot", "optional", "services", "imports", "exports" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ModuleDependency {

    private String name;

    private String slot = "main";

    private boolean export;

    private Services services = Services.NONE;

    private boolean optional;

    private Filter imports;

    private Filter exports;

    /**
     * The name of the module upon which the module depends.
     * 
     * @return
     */
    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The version slot of the module upon which the module depends; defaults to "main".
     * 
     * @return
     */
    @XmlAttribute
    public String getSlot() {
        if (slot == null || slot.isEmpty()) {
            return "main";
        }
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    /**
     * Specify whether this dependency is re-exported by default; if not specified, defaults to "false".
     * 
     * @return
     */
    @XmlAttribute
    public boolean isExport() {
        return export;
    }

    public void setExport(boolean export) {
        this.export = export;
    }

    /**
     * Specify whether this dependency's services* are imported and/or exported. Possible values are "none", "import", or
     * "export"; defaults to "none".
     * 
     * @return
     */
    @XmlAttribute
    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    /**
     * Specify whether this dependency is optional; defaults to "false".
     * 
     * @return
     */
    @XmlAttribute
    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public Filter getImports() {
        return imports;
    }

    public void setImports(Filter imports) {
        this.imports = imports;
    }

    public Filter getExports() {
        return exports;
    }

    public void setExports(Filter exports) {
        this.exports = exports;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                "ModuleDependency [name=%s, slot=%s, export=%s, services=%s, optional=%s, imports=%s, exports=%s]", getName(),
                getSlot(), isExport(), getServices(), isOptional(), getImports(), getExports());
    }

}
