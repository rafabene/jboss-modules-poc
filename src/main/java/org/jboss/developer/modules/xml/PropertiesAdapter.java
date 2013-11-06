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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.jboss.developer.modules.xml.PropertiesAdapter.AdaptedProperties;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class PropertiesAdapter extends XmlAdapter<AdaptedProperties, Properties> {

    public static class AdaptedProperties {
        @XmlElement(name = "property")
        private List<AdaptedProperty> properties = new ArrayList<AdaptedProperty>();

        /**
         * @return the properties
         */
        public List<AdaptedProperty> getProperties() {
            return properties;
        }
    }

    public static class AdaptedProperty {

        private String name;

        private String value;

        AdaptedProperty() {
            // Default Constructor to JAXB
        }

        /**
         * @param name
         * @param value
         */
        public AdaptedProperty(String name, String value) {
            super();
            this.name = name;
            this.value = value;
        }

        @XmlAttribute
        public String getName() {
            return name;
        }

        @XmlValue
        public String getValue() {
            return value;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public Properties unmarshal(org.jboss.developer.modules.xml.PropertiesAdapter.AdaptedProperties v) throws Exception {
        Properties p = new Properties();
        for (AdaptedProperty ap : v.getProperties()) {
            p.put(ap.getName(), ap.getValue());
        }
        return p;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public org.jboss.developer.modules.xml.PropertiesAdapter.AdaptedProperties marshal(Properties v) throws Exception {
        AdaptedProperties ap = new AdaptedProperties();
        for (Object name : v.keySet()) {
            Object value = v.get(name);
            AdaptedProperty a = new AdaptedProperty((String) name, (String) value);
            ap.getProperties().add(a);
        }
        return ap;
    }
}
