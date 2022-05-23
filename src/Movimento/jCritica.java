/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Movimento;

import Funcoes.Dates;
import Funcoes.DbMain;
import Funcoes.TableControl;
import Funcoes.VariaveisGlobais;
import Funcoes.jDirectory;
import Funcoes.toPreview;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import static java.lang.Thread.sleep;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Samic
 */
public class jCritica extends javax.swing.JInternalFrame {
    DbMain conn = VariaveisGlobais.conexao;
    final TableRowSorter<TableModel> sorter;

    /**
     * Creates new form jCritica
     */
    public jCritica() {
        initComponents();
        
        FillCriticas();

        sorter = new TableRowSorter<TableModel>(jCritica.getModel());
        jCritica.setRowSorter(sorter);

        jCritica.addMouseMotionListener(new MouseMotionAdapter(){
           public void mouseMoved(MouseEvent e){
               try {
                    Point p = e.getPoint(); 
                    int row = jCritica.rowAtPoint(p);
                    int modelRow = jCritica.convertRowIndexToModel(row);
                    int column = 5; //jCritica.columnAtPoint(p);
                    if (jCritica.getModel().getValueAt(modelRow,column) != null) jCritica.setToolTipText(String.valueOf(jCritica.getModel().getValueAt(modelRow,column)));
                } catch (Exception ex) {}
            }
        });
        
        jCritica.addKeyListener( new KeyListener() {
            public void keyTyped( KeyEvent e ) {}

            @Override
            public void keyReleased( KeyEvent e ) {}

            @Override
            public void keyPressed( KeyEvent e ) {
                int tid = Integer.valueOf(jCritica.getValueAt(jCritica.getSelectedRow(), 0).toString());
                Object[][] param = null; String ieSQL = ""; String tjustifica = null;
                if ( e.getKeyCode() == KeyEvent.VK_INSERT ) {
                    if (jCritica.getValueAt(jCritica.getSelectedRow(), 5) != null) return;
                    
                    tjustifica =  JOptionPane.showInputDialog("Digite a justificativa.");
                    if (tjustifica == null) return;
                    if (tjustifica.length() == 0) return;
                    ieSQL = "UPDATE critica SET justifica = ? WHERE id = ?;";
                    param = new Object[][] {{"string", tjustifica}, {"int", tid}};
                }
                if ( e.getKeyCode() == KeyEvent.VK_DELETE ) {
                    if (jCritica.getValueAt(jCritica.getSelectedRow(), 5) == null) return;
                    
                    Object[] options = { "Sim", "Não" };
                    int n = JOptionPane.showOptionDialog(null,
                        "Deseja apagar esta Justificativa ? ",
                        "Atenção", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                    if (n == JOptionPane.NO_OPTION) return;                    
                    ieSQL = "UPDATE critica SET justifica = null WHERE id = ?;";
                    param = new Object[][] {{"int", tid}};
                }             
                if (ieSQL.length() > 0) {
                    conn.ExecutarComando(ieSQL, param);
                    jCritica.setValueAt(tjustifica,jCritica.getSelectedRow(), 5);
                }
            }
        });
    }

    private void FillCriticas() {
        jFiltro.setEnabled(false);
        jbtClean.setEnabled(false);

        jListarNaoJustificados.setEnabled(false);
        jListarJustificados.setEnabled(false);
        
        //TableControl.header(jCritica, new String[][] {{"id","dtcritica","contrato","vencimento","msg","justifica"},{"0","60","60","60","120","0"}});
        TableControl.Clear(jCritica);

        new Thread() {
            public void run() {
                String selectSQL = "SELECT `id`,`contrato`,`vencimento`,`dtcritica`," +
                                   "`msg`,`justifica` FROM `critica`;";
                ResultSet hrs = conn.AbrirTabela(selectSQL, ResultSet.CONCUR_READ_ONLY);
                int rcount = DbMain.RecordCount(hrs); int b = 0;
                jProgressBar1.setValue(b);
                try {
                    while (hrs.next()) {
                        String tid = ""; try { tid = hrs.getString("id"); } catch (SQLException e) {}
                        String tdtcritica = ""; try { tdtcritica = Dates.DateFormata("dd-MM-yyyy", hrs.getDate("dtcritica")); } catch (SQLException e) {}
                        String tcontrato = ""; try { tcontrato = hrs.getString("contrato"); } catch (SQLException e) {}
                        String tvencimento = ""; try { tvencimento = Dates.DateFormata("dd-MM-yyyy", hrs.getDate("vencimento")); } catch (SQLException e) {}
                        String tmsg = ""; try { tmsg = hrs.getString("msg"); } catch (SQLException e) {}
                        String tjustifica = ""; try { tjustifica = hrs.getString("justifica"); } catch (SQLException e) {}
                        
                        
                        if (tmsg.contains("Diferença")) {
                            int pos = 11;
                            String ttmsg = tmsg.substring(pos);
                            pos = -1; pos = ttmsg.indexOf(":");
                            String cCod = ttmsg.substring(pos+1);
                            String[][] cartLanc = null;
                            try {
                                cartLanc = conn.LerCamposTabela(new String[] {"CART_DESCR"}, "lancart", "cart_codigo = '" + cCod + "'");
                            } catch (SQLException e) {}
                            if (cartLanc != null) {
                                tmsg = "Diferença: [" + ttmsg + "] - " + cartLanc[0][3].toString();
                            }
                        }
                        
                        TableControl.add(jCritica, new String[][]{{tid, tdtcritica, tcontrato, tvencimento, tmsg, tjustifica},{"C","C","C","C","L","L"}}, true);
                        int pgs = ((b++ * 100) / rcount) + 1;
                        jProgressBar1.setValue(pgs);
                        try {sleep(100);} catch (Exception ex) {}
                    }
                } catch (SQLException e) {}
                DbMain.FecharTabela(hrs);

                jFiltro.setEnabled(true);
                jbtClean.setEnabled(true);
                
                jListarNaoJustificados.setEnabled(true);
                jListarJustificados.setEnabled(true);
            }
        }.start();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jCritica = new javax.swing.JTable();
        jListarNaoJustificados = new javax.swing.JButton();
        jListarJustificados = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jFiltro = new javax.swing.JTextField();
        jbtClean = new javax.swing.JLabel();

        setClosable(true);
        setTitle(".:: Verificação dos Campos");

        jCritica.setAutoCreateRowSorter(true);
        jCritica.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "dtcritica", "contrato", "vencimento", "msg", "justifica"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jCritica.setColumnSelectionAllowed(true);
        jCritica.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jCritica.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jCritica);
        jCritica.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jCritica.getColumnModel().getColumnCount() > 0) {
            jCritica.getColumnModel().getColumn(0).setMinWidth(60);
            jCritica.getColumnModel().getColumn(0).setPreferredWidth(60);
            jCritica.getColumnModel().getColumn(0).setMaxWidth(60);
            jCritica.getColumnModel().getColumn(1).setMinWidth(70);
            jCritica.getColumnModel().getColumn(1).setPreferredWidth(70);
            jCritica.getColumnModel().getColumn(1).setMaxWidth(70);
            jCritica.getColumnModel().getColumn(2).setMinWidth(70);
            jCritica.getColumnModel().getColumn(2).setPreferredWidth(70);
            jCritica.getColumnModel().getColumn(2).setMaxWidth(70);
            jCritica.getColumnModel().getColumn(3).setMinWidth(70);
            jCritica.getColumnModel().getColumn(3).setPreferredWidth(70);
            jCritica.getColumnModel().getColumn(3).setMaxWidth(70);
            jCritica.getColumnModel().getColumn(4).setMinWidth(330);
            jCritica.getColumnModel().getColumn(4).setPreferredWidth(330);
            jCritica.getColumnModel().getColumn(4).setMaxWidth(330);
            jCritica.getColumnModel().getColumn(5).setMinWidth(380);
            jCritica.getColumnModel().getColumn(5).setPreferredWidth(380);
            jCritica.getColumnModel().getColumn(5).setMaxWidth(500);
        }

        jListarNaoJustificados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/Actions-document-print-preview-icon.png"))); // NOI18N
        jListarNaoJustificados.setText("Listar Não Justificados");
        jListarNaoJustificados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jListarNaoJustificadosActionPerformed(evt);
            }
        });

        jListarJustificados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/Actions-document-print-preview-icon.png"))); // NOI18N
        jListarJustificados.setText("Listar Justificados");
        jListarJustificados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jListarJustificadosActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Filtrar:");

        jFiltro.setBorder(null);
        jFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFiltroKeyReleased(evt);
            }
        });

        jbtClean.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Figuras/Clean_16x16.jpeg"))); // NOI18N
        jbtClean.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbtCleanMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFiltro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtClean))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jbtClean)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(jFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jListarNaoJustificados, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 313, Short.MAX_VALUE)
                        .addComponent(jListarJustificados, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jListarNaoJustificados)
                    .addComponent(jListarJustificados))
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtCleanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbtCleanMouseClicked
        jFiltro.setText(null); sorter.setRowFilter(null); jFiltro.requestFocus();
    }//GEN-LAST:event_jbtCleanMouseClicked

    private void jFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFiltroKeyReleased
        if ("".equals(jFiltro.getText().trim())) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(
                       RowFilter.regexFilter(jFiltro.getText().trim()));
            } catch (PatternSyntaxException pse) {
                   System.err.println("Bad regex pattern");
            }
        }
    }//GEN-LAST:event_jFiltroKeyReleased

    private void jListarNaoJustificadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jListarNaoJustificadosActionPerformed
        PrintCritica(false);
    }//GEN-LAST:event_jListarNaoJustificadosActionPerformed

    private void jListarJustificadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jListarJustificadosActionPerformed
        if (PrintCritica(true).size() <= 0) return;
        
        Object[] options = { "Sim", "Não" };
        int n = JOptionPane.showOptionDialog(null,
            "Deseja apagar as linhas com Justificativa ? ",
            "Atenção", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (n == JOptionPane.NO_OPTION) return;                    
        
        n = JOptionPane.showOptionDialog(null,
            "Tem certeza ? ",
            "Atenção", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (n == JOptionPane.NO_OPTION) return;                    
        
        String insertSQL = "INSERT INTO `test`.`critica` (SELECT * FROM `critica` WHERE justifica is not null);";
        conn.ExecutarComando(insertSQL);
        
        String deleteSQL = "DELETE FROM critica WHERE justifica is not null;";
        conn.ExecutarComando(deleteSQL);
        int totItens = jCritica.getRowCount() - 1;
        for (int f = totItens; f >= 0; f--) {
            if (jCritica.getValueAt(f, 5) != null) {
                TableControl.del(jCritica, f);
                jCritica.revalidate();
            }
        }
    }//GEN-LAST:event_jListarJustificadosActionPerformed

    private List<classCritica> PrintCritica(boolean just) {
        List<classCritica> listas = Lista(just);
        JasperPrint jasperPrint = null;
        try {
            Map parametros = new HashMap();
            parametros.put("justificado", just);
            JRDataSource jrds = new JRBeanCollectionDataSource(listas);

            String reportFileName = "reports/Critica.jasper";
            JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(reportFileName);
            jasperPrint = JasperFillManager.fillReport(reporte, parametros, jrds);

            JRPdfExporter exporter = new JRPdfExporter();

            try {
                new jDirectory("reports/Relatorios/" + Dates.iYear(new Date()) + "/" + Dates.Month(new Date()) + "/");
                String pathName = "reports/Relatorios/" + Dates.iYear(new Date()) + "/" + Dates.Month(new Date()) + "/";

                // Configure the exporter (set output file name and print object)
                String FileNamePdf = pathName + "Critica_" + Dates.DateFormata("dd-MM-yyyy_HH_mm", new Date()) + ".pdf";
                String outFileName = FileNamePdf;
                
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

                exporter.exportReport();
                new toPreview(outFileName);
            } catch (Exception jex) {jex.printStackTrace();}
        } catch (JRException e) {
            e.printStackTrace();
        }        
        
        return listas;
    }
    
    private List<classCritica> Lista(boolean just) {
        List<classCritica> lista = new ArrayList<>();
        for (int z=0; z<jCritica.getRowCount(); z++) {
            if (just) {
                if (jCritica.getValueAt(z, 5) == null) continue;
            } else {
                if (jCritica.getValueAt(z, 5) != null) continue;
            }
            
            classCritica bean1 = new classCritica();
            
            String tid = jCritica.getValueAt(z, 0).toString();
            bean1.setId(tid);
            
            String tdtcritica = jCritica.getValueAt(z, 1).toString();
            bean1.setDtcritica(tdtcritica);
            
            String tcontrato = jCritica.getValueAt(z, 2).toString();
            bean1.setContrato(tcontrato);
            
            String tvencimento = jCritica.getValueAt(z, 3).toString();
            bean1.setVencimento(tvencimento);
            
            String tmsg = jCritica.getValueAt(z, 4).toString();
            bean1.setMsg(tmsg);
            
            String tjustifica = null;
            try { tjustifica = jCritica.getValueAt(z, 5).toString(); } catch (NullPointerException e) {}
            if (!just) tjustifica = null;
            bean1.setJustifica(tjustifica);
            
            lista.add(bean1);
        }
        return lista;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable jCritica;
    private javax.swing.JTextField jFiltro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jListarJustificados;
    private javax.swing.JButton jListarNaoJustificados;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jbtClean;
    // End of variables declaration//GEN-END:variables
}
