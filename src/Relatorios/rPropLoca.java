/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * rSeguros.java
 *
 * Created on 28/02/2012, 15:35:45
 */
package Relatorios;

import Funcoes.AutoCompletion;
import Funcoes.Dates;
import Funcoes.DbMain;
import Funcoes.VariaveisGlobais;
import Funcoes.jDirectory;
import Funcoes.toPreview;
import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 *
 * @author supervisor
 */
public class rPropLoca extends javax.swing.JInternalFrame {
    DbMain conn = VariaveisGlobais.conexao;
    boolean bExecNomei = false, bExecCodigoi = false;
    boolean bExecNomef = false, bExecCodigof = false;

    /** Creates new form rSeguros */
    public rPropLoca() {
        initComponents();

        // Colocando enter para pular de campo
        HashSet conj = new HashSet(this.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);

        FillCombos(false, true);
        AutoCompletion.enable(jRgprp);
        AutoCompletion.enable(jNomeProp);

        AutoCompletion.enable(jRgprp1);
        AutoCompletion.enable(jNomeProp1);
        
    }

    private void FillCombos(boolean Depositos, boolean isAtivo) {
        String sSql = "";
        if (!Depositos) {
            sSql = "SELECT distinct p.rgprp, p.nome FROM proprietarios p " + (isAtivo ? "WHERE Upper(p.status) = 'ATIVO' " : "WHERE Upper(p.status) <> 'ATIVO' ") + "ORDER BY p.nome;";
        } else {
            sSql = "SELECT DISTINCT e.rgprp, p.nome AS nome FROM extrato e, proprietarios p WHERE " + (isAtivo ? "WHERE Upper(p.status) = 'ATIVO' and " : "WHERE Upper(p.status) <> 'ATIVO' and ") + "e.rgprp = p.rgprp AND TRIM(p.conta) <> '' AND tag <> 'X' ORDER BY Lower(p.nome);";
        }
        ResultSet imResult = this.conn.AbrirTabela(sSql, ResultSet.CONCUR_READ_ONLY);

        jRgprp.removeAllItems();
        jRgprp1.removeAllItems();
        jNomeProp.removeAllItems();
        jNomeProp1.removeAllItems();
        try {
            while (imResult.next()) {
                jRgprp.addItem(String.valueOf(imResult.getInt("rgprp")));
                jNomeProp.addItem(imResult.getString("nome"));

                jRgprp1.addItem(String.valueOf(imResult.getInt("rgprp")));
                jNomeProp1.addItem(imResult.getString("nome"));                
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        DbMain.FecharTabela(imResult);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbtnPreview = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jRgprp = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jNomeProp = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jRgprp1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jNomeProp1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jcbStatus = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle(".:: Relatório de Proprietários e Locatarios ... ::.");
        setVisible(true);

        jbtnPreview.setText("Relatório Prop e Locatarios");
        jbtnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPreviewActionPerformed(evt);
            }
        });

        jLabel1.setText("Rgprp:");

        jRgprp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRgprpActionPerformed(evt);
            }
        });

        jLabel2.setText("Nome:");

        jNomeProp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNomePropActionPerformed(evt);
            }
        });

        jLabel3.setText("Rgprp:");

        jRgprp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRgprp1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Nome:");

        jNomeProp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNomeProp1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Proprietário Final:");

        jLabel6.setText("Proprietário Inicial:");

        jcbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ativo", "inativo" }));
        jcbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbStatusActionPerformed(evt);
            }
        });

        jLabel7.setText("Status:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRgprp, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jNomeProp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(15, 15, 15)
                                .addComponent(jRgprp1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jNomeProp1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jbtnPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 416, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jcbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jRgprp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jNomeProp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jRgprp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jNomeProp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnPreview)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPreviewActionPerformed
        jbtnPreview.setEnabled(false);
        String tAtivo = jcbStatus.getSelectedItem().toString().trim().toLowerCase();
        RelPropLoca(jNomeProp.getSelectedItem().toString().trim(), jNomeProp1.getSelectedItem().toString().trim(), tAtivo);
        jbtnPreview.setEnabled(true);
    }//GEN-LAST:event_jbtnPreviewActionPerformed

    private void jRgprpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRgprpActionPerformed
        if (!bExecNomei) {
            int pos = jRgprp.getSelectedIndex();
            if (jNomeProp.getItemCount() > 0) {bExecCodigoi = true; jNomeProp.setSelectedIndex(pos); bExecCodigoi = false;}
        }
    }//GEN-LAST:event_jRgprpActionPerformed

    private void jNomePropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNomePropActionPerformed
        if (!bExecCodigoi) {
            int pos = jNomeProp.getSelectedIndex();
            if (jRgprp.getItemCount() > 0) {bExecNomei = true; jRgprp.setSelectedIndex(pos); bExecNomei = false; }
        }
    }//GEN-LAST:event_jNomePropActionPerformed

    private void jRgprp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRgprp1ActionPerformed
        if (!bExecNomef) {
            int pos = jRgprp1.getSelectedIndex();
            if (jNomeProp1.getItemCount() > 0) {bExecCodigof = true; jNomeProp1.setSelectedIndex(pos); bExecCodigof = false;}
        }
    }//GEN-LAST:event_jRgprp1ActionPerformed

    private void jNomeProp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNomeProp1ActionPerformed
        if (!bExecCodigof) {
            int pos = jNomeProp1.getSelectedIndex();
            if (jRgprp1.getItemCount() > 0) {bExecNomef = true; jRgprp1.setSelectedIndex(pos); bExecNomef = false; }
        }
    }//GEN-LAST:event_jNomeProp1ActionPerformed

    private void jcbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbStatusActionPerformed
        FillCombos(false, (jcbStatus.getSelectedItem().toString().toUpperCase().trim().equalsIgnoreCase("ATIVO") ? true : false));
    }//GEN-LAST:event_jcbStatusActionPerformed

      private void RelPropLoca(String pInic, String pFinal, String pAtivo) {
        Map parametros = new HashMap();
        parametros.put("propini", pInic);
        parametros.put("propfim", pFinal);
        parametros.put("propstatus", pAtivo);
        parametros.put("logo", System.getProperty("user.dir") + "/" + "resources/logos/boleta/" + VariaveisGlobais.marca.trim() + ".gif");
        
        try {
            String fileName = System.getProperty("user.dir") + "/" + "reports/rPropLoca.jasper";
            JasperPrint print = JasperFillManager.fillReport(fileName, parametros, conn.conn);

            // Create a PDF exporter
            JRExporter exporter = new JRPdfExporter();

            new jDirectory("reports/Relatorios/" + Dates.iYear(new Date()) + "/" + Dates.Month(new Date()) + "/");
            String pathName = "reports/Relatorios/" + Dates.iYear(new Date()) + "/" + Dates.Month(new Date()) + "/";
            
            // Configure the exporter (set output file name and print object)
            String outFileName = pathName + "PropLoca_" + Dates.DateFormata("ddMMyyyy", new Date()) + ".pdf";
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

            // Export the PDF file
            exporter.exportReport();
            
            new toPreview(outFileName);
//            if (!"jasper".equals(VariaveisGlobais.reader)) {
//                ComandoExterno ce = new ComandoExterno();
//                ComandoExterno.ComandoExterno(VariaveisGlobais.reader + " " + outFileName);
//            } else {
//                JasperViewer viewer = new JasperViewer(print, false);
//                viewer.show();
//            }
                
        } catch (JRException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JComboBox jNomeProp;
    private javax.swing.JComboBox jNomeProp1;
    private javax.swing.JComboBox jRgprp;
    private javax.swing.JComboBox jRgprp1;
    private javax.swing.JButton jbtnPreview;
    private javax.swing.JComboBox jcbStatus;
    // End of variables declaration//GEN-END:variables
}
