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
import java.util.ArrayList;
import java.util.List;

import org.jboss.developer.modules.ModulesInformationBuilder;
import org.jboss.developer.modules.model.BaseModule;
import org.junit.BeforeClass;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public abstract class AbstractModulesTest {

    protected static String modulesRoot;

    protected static List<BaseModule> modules = new ArrayList<BaseModule>();

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        modulesRoot = "/Users/rafaelbenevides/projetos/eap-beta/jboss-eap-6.2/modules";
        modules = ModulesInformationBuilder.getInstance(new File(modulesRoot)).build();
    }

}
