/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Funcoes;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Samic
 */
public class SettingPwd {
    private static final String dbPassWordSettingPath = System.getProperty("user.dir") + "/j4Rent.aut";
    
    public SettingPwd() {
        boolean exists = (new File(dbPassWordSettingPath)).exists();
        if (!exists) {
            JOptionPane.showMessageDialog(null, "Programa sem autorização!!!\n\nEntre em contato com a SAMIC Tel.: (21)2701-0261\nou\nCel.: (21)98552-1405", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        Properties p;
        FileInputStream propFile;
        try {
            propFile = new FileInputStream(dbPassWordSettingPath);
            p = new Properties(System.getProperties()){
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            p.load(propFile);

            System.setProperties(p);
        } catch (Exception ex) {ex.printStackTrace();}                
    }
    
}
