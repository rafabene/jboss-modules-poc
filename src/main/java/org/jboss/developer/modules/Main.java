package org.jboss.developer.modules;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jboss.developer.modules.model.BaseModule;
import org.jboss.developer.modules.model.Module;

public class Main {

    private static ModulesInformationBuilder mib;

    public static void main(String[] args) throws BuildException, IOException {
        File moduesPath = new File("/Users/rafaelbenevides/projetos/eap-beta/jboss-eap-6.2/modules");
        mib = ModulesInformationBuilder.getInstance(moduesPath);
//        generateListOfPrivatePAckages();
        generateXML();
    }

    private static void generateXML() throws BuildException, IOException {
        String xml = mib.buildXML();
        System.out.println(xml);
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("output.xml")));
        bw.write(xml);
        bw.close();
    }

    private static void generateListOfPrivatePAckages() throws BuildException, IOException {
        List<BaseModule> modules = mib.build();
        StringBuilder sb = new StringBuilder();
        for (BaseModule bm : modules) {
            if (bm instanceof Module) {
                Module module = (Module) bm;
                if (((Module) bm).isPrivateModule()) {
                    for (String pkg : module.getPackages()) {
                        sb.append(pkg);
                        sb.append(",\n");
                    }
                }
            }
        }
        System.out.println(sb.toString());

    }
}
