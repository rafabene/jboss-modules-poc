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

package org.jboss.developer.test;

import java.io.File;
import java.util.List;

import org.jboss.developer.modules.io.ModulesFinder;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ModuleFinderTest extends AbstractModulesTest {

    private ModulesFinder mf = new ModulesFinder();

    /**
     * Test method for {@link org.jboss.developer.modules.io.ModulesFinder#findModulesInPath(java.io.File)}.
     */
    @Test
    public void testFindModulesInPath() {
        List<File> modules = mf.findModulesInPath(new File(modulesRoot));
        Assert.assertEquals("Should be 280 modules found on EAP 6.2 modules folder", 280, modules.size());
    }

    /**
     * Test method for {@link org.jboss.developer.modules.io.ModulesFinder#findModulesInPath(java.io.File)}.
     */
    @Test
    public void testFindModulesInPathNotExistentFolder() {
        try {
            mf.findModulesInPath(new File("/xpto"));
            Assert.fail("Should not run");
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull("Should give exception on non existing folder", e);
        }
    }

}
