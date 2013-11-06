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

import java.io.IOException;
import java.util.Set;

import org.jboss.developer.modules.model.BaseModule;
import org.jboss.developer.modules.model.Gav;
import org.jboss.developer.modules.model.Jar;
import org.jboss.developer.modules.model.Module;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ModulesTest extends AbstractModulesTest {

    /**
     * Test method for {@link org.jboss.jdf.modules.jar.JarUtils#isPrivateModule(org.jboss.developer.modules.model.Module)}.
     */
    @Test
    public void testIsPrivateModule() {
        for (BaseModule am : modules) {
            if (am.getName().equals("org.codehaus.jackson.jackson-jaxrs")) {
                boolean isPrivate = ((Module) am).isPrivateModule();
                Assert.assertTrue("Module should be private", isPrivate);

            }
        }
    }

    /**
     * Test method for {@link org.jboss.jdf.modules.jar.JarUtils#isPrivateModule(org.jboss.developer.modules.model.Module)}.
     */
    @Test
    public void testNotPrivateModule() {
        for (BaseModule am : modules) {
            if (am.getName().equals("org.picketlink")) {
                boolean isPrivate = ((Module) am).isPrivateModule();
                Assert.assertFalse("Module should NOT be private", isPrivate);

            }
        }
    }

    /**
     * Test method for {@link org.jboss.jdf.modules.jar.JarUtils#getPackagesFromModule(org.jboss.developer.modules.model.Module)}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetPackagesFromModule() throws IOException {
        for (BaseModule am : modules) {
            if (am.getName().equals("org.picketlink")) {
                Module module = (Module) am;
                Set<String> packages = module.getPackages();
                Assert.assertEquals("Picketlink packages for picketlink module should be 106 packages", 106, packages.size());

            }
        }
    }

    /**
     * Test method for {@link org.jboss.jdf.modules.jar.JarUtils#getGavFromJar(java.io.File)}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetGavFromJar() throws IOException {
        for (BaseModule am : modules) {
            if (am.getName().equals("org.picketlink")) {
                Module module = (Module) am;
                Jar jar = module.getResources().get(0);
                Gav gav = jar.getGav();
                Assert.assertEquals(gav.getGroupId(), "org.picketlink");
                Assert.assertEquals(gav.getArtifactId(), "picketlink-core");
                Assert.assertEquals(gav.getVersion(), "2.1.6.3.Final-redhat-2");
            }
        }
    }

    /**
     * Test method for {@link org.jboss.jdf.modules.jar.JarUtils#getGavFromJar(java.io.File)}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetGavFromNoGavJar() throws IOException {
        for (BaseModule am : modules) {
            if (am.getName().equals("org.dom4j")) {
                Module module = (Module) am;
                Jar jar = module.getResources().get(0);
                Gav gav = jar.getGav();
                Assert.assertNull("GAV not present. Should be null", gav);
            }
        }
    }
}
