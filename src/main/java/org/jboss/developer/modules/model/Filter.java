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

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * A path filter to apply to this resource root. If not specified, all paths are accepted.
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class Filter {

    private Set<String> exclude = new HashSet<String>();

    private Set<String> excludeset = new HashSet<String>();

    private Set<String> include = new HashSet<String>();

    private Set<String> includeset = new HashSet<String>();

    /**
     * The path name, which can be a literal path name or it may include the special wildcards "*", "**", and "?".
     * 
     * @return the exclude
     */
    public Set<String> getExclude() {
        return exclude;
    }

    /**
     * A set of literal path names which can be used for efficient matching against multiple possible values.
     * 
     * @return the excludeset
     */
    public Set<String> getExcludeset() {
        return excludeset;
    }

    /**
     * The path name, which can be a literal path name or it may include the special wildcards "*", "**", and "?".
     * 
     * @return the include
     */
    public Set<String> getInclude() {
        return include;
    }

    /**
     * A set of literal path names which can be used for efficient matching against multiple possible values.
     * 
     * @return the includeset
     */
    public Set<String> getIncludeset() {
        return includeset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Filter:\nIncludes: " + getInclude() + "\nIncludes-Set: " + getIncludeset() + "\nExcludes: " + getExclude()
                + "\nExcludes-Set: " + getExcludeset();
    }
}
