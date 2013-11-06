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

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class SystemDependency {

    private boolean export = false;

    private List<String> paths = new ArrayList<String>();

    private Filter exports;

    /**
     * Specify whether this dependency is re-exported by default; if not specified, defaults to "false"
     */
    public boolean isExport() {
        return export;
    }

    public void setExport(boolean export) {
        this.export = export;
    }

    /**
     * A filter which restricts the list of packages/paths which are re-exported by this module. If not specified, all paths are
     * selected (does not apply if the export attribute on the system element is false or unspecified).
     */
    public Filter getExports() {
        return exports;
    }

    public void setExports(Filter exports) {
        this.exports = exports;
    }

    /**
     * Specify the list of paths (or packages, with "." transformed to "/") which are exposed by this dependency.
     */
    public List<String> getPaths() {
        return paths;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("SystemDependency [export=%s, paths=%s, exports=%s]", isExport(), getPaths(), getExports());
    }

}
