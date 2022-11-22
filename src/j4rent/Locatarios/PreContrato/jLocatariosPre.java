/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * jLocatarios.java
 *
 * Created on 11/12/2011, 19:06:22
 */

package j4rent.Locatarios.PreContrato;

import j4rent.Locatarios.*;
import Funcoes.*;
import Movimento.Contratos.jContrato;
import j4rent.jTelsCont;
import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author mariana
 */
public class jLocatariosPre extends javax.swing.JInternalFrame {
    String sInativos = (!VariaveisGlobais.Iloca ? "WHERE fiador1uf <> 'X' OR IsNull(fiador1uf)" : "WHERE fiador1uf = 'X'");
    DbMain conn = VariaveisGlobais.conexao;
    ResultSet pResult = conn.AbrirTabela("SELECT contrato, " +
                                            "rgimv," +
                                            "rgprp," +
                                            "tpimovel," +
                                            "tploca," +
                                            "cpfcnpj," +
                                            "rginsc," +
                                            "dtnasc," +
                                            "nomerazao," +
                                            "fantasia," +
                                            "end," +
                                            "num," +
                                            "compl," +
                                            "bairro," +
                                            "cidade," +
                                            "estado," +
                                            "cep," +
                                            "tel," +
                                            "ramal," +
                                            "celular," +
                                            "naciona," +
                                            "ecivil," +
                                            "pai," +
                                            "mae," +
                                            "empresa," +
                                            "cargo," +
                                            "salario," +
                                            "dtadmis," +
                                            "email," +
                                            "conjugue," +
                                            "cjdtnasc," +
                                            "cjempresa," +
                                            "cjtel," +
                                            "cjramal," +
                                            "cjsalario," +
                                            "fiador1tel," +
                                            "cjrg," +
                                            "cjcpf," +
                                            "sexo," +
                                            "fiador1uf, " +
                                            "conjsexo " +
                                            "FROM prelocatarios " + sInativos + " ORDER BY contrato;", ResultSet.CONCUR_UPDATABLE);
    private boolean bNew = false;

    /** Creates new form jLocatarios */
    public jLocatariosPre() {
        initComponents();
        if (!VariaveisGlobais.Iloca) {
            //setBackground(new java.awt.Color (237, 236, 235));
        } else { setBackground(new java.awt.Color(255, 0, 0)); }
        
        try {LerDados(true);} catch (SQLException ex) {}

        // Colocando enter para pular de campo
        HashSet conj = new HashSet(this.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);

        mfCep.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (mfEndereco.getText().trim().equals("") && mfCep.getText().trim() != "") {
                    CEPEndereco cepEnder = ClienteViaCepWS.buscarCep(mfCep.getText());
                    if (cepEnder != null) {
                        mfEndereco.setText(cepEnder.getLogradouro());
                        mfNumero.setText("");
                        mfCplto.setText("");
                        mfBairro.setText(cepEnder.getBairro());
                        mfCidade.setText(cepEnder.getLocalidade());
                        mfEstado.setText(cepEnder.getUf());
                        
                        mfNumero.requestFocus();
                    }
                }            
            }
        });
        
        mjCep.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (mjEndereco.getText().trim().equals("") && mjCep.getText().trim() != "") {
                    CEPEndereco cepEnder = ClienteViaCepWS.buscarCep(mjCep.getText());
                    if (cepEnder != null) {
                        mjEndereco.setText(cepEnder.getLogradouro());
                        mjNumero.setText("");
                        mjCplto.setText("");
                        mjBairro.setText(cepEnder.getBairro());
                        mjCidade.setText(cepEnder.getLocalidade());
                        mjEstado.setText(cepEnder.getUf());
                        
                        mjNumero.requestFocus();
                    }
                }            
            }
        });
        
        SelectAll();
    }

    private void SelectAll() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher(){
            public boolean dispatchKeyEvent(KeyEvent ke){
                if(ke.getID()==KeyEvent.KEY_RELEASED) {
                    int key = ke.getKeyCode();
                    if(key == KeyEvent.VK_ENTER) {
                        Component comp = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                        if(comp instanceof JTextField) {
                            ((JTextField)comp).selectAll();
                        }
                    }
                }
                return false;}});
    }

    public boolean MoveToLoca(String campo, String seek) throws SQLException {
        boolean achei = false;
        try {
            pResult.first();
            while (pResult.next()) {
                if (pResult.getInt(campo) == Integer.parseInt(seek)) {
                    achei = true;
                    break;
                }
            }
        } catch (Exception ex) {}
        if (!achei) pResult.first();
        LerDados(false);
        return achei;
    }

    private void LerDados(boolean bFirst) throws SQLException {
        if (DbMain.RecordCount(pResult) <= 0) {return;}
        
        // Limpa tela
        LimpaDados();
        //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (bFirst) pResult.first();
        mRgprp.setText(Integer.toString(pResult.getInt("rgprp")));
        mRgimv.setText(Integer.toString(pResult.getInt("rgimv")));
        mTpImv.setText(pResult.getString("tpimovel"));
        mContrato.setText(pResult.getString("contrato"));
        if (pResult.getString("tploca").toUpperCase().contains("F")) {
            jrbFisica.setSelected(true);
            mCpf.setText(pResult.getString("cpfcnpj"));
            mCnpj.setText("");

            jrbFisica.setEnabled(true);
            jrbJuridica.setEnabled(false);
            jDados.setEnabledAt(0, false);
            jDados.setEnabledAt(1, true);
            jDados.setSelectedIndex(1);

            mCpf.setEnabled(true);
            mCnpj.setEnabled(false);
        } else {
            jrbJuridica.setSelected(true);
            mCnpj.setText(pResult.getString("cpfcnpj"));
            mCpf.setText("");

            jrbFisica.setEnabled(false);
            jrbJuridica.setEnabled(true);

            jDados.setEnabledAt(0, true);
            jDados.setEnabledAt(1, false);
            jDados.setSelectedIndex(0);

            mCpf.setEnabled(false);
            mCnpj.setEnabled(true);
        }
        mIdentidade.setText(pResult.getString("rginsc"));

        if (jrbFisica.isSelected()) {
            // Pessoa Física (limpar dados dos campos juridica)
            mfNome.setText(pResult.getString("nomerazao"));
            mfSexo.getModel().setSelectedItem(pResult.getString("sexo"));
            
            try {mfDtNasc.setDate(Dates.StringtoDate(pResult.getString("dtnasc"), "yyyy-MM-dd"));} catch (Exception ex) {mfDtNasc.setDate(null);}
            mfNacionalidade.setText(pResult.getString("naciona"));

            //mfTel2.setText(FuncoesGlobais.rmvNumero(pResult.getString("fiador1tel")));            

            String pCivil = pResult.getString("ecivil").trim().toLowerCase();
            String sCivil[] = {"solteiro","solteira","casado","casada","separado","separada","divorciado","divorciada","viuvo","viuva"};
            Integer nPos = FuncoesGlobais.IndexOf2(sCivil,pCivil) ;
            if (nPos == -1) {
                nPos = 0;
            }
            else if (nPos == 0 || nPos == 1){
                nPos = 0;
            }
            else if (nPos == 2 || nPos == 3) {
                nPos = 1;
            }
            else if (nPos == 4 || nPos == 5) {
                nPos = 2;
            }
            else if (nPos == 6 || nPos == 7) {
                nPos = 3;
            }
            else if (nPos == 8 || nPos == 9 ) {
                nPos = 4;
            }
            else
                nPos = 0;

            mfEstCivil.setSelectedIndex(nPos);

            mfTel1.removeAllItems();
            String[] telef = null;
            try { telef = pResult.getString("celular").split(";");
                if (telef != null) {
                    for (int w=0;w<telef.length;w++) {
                        if (!telef[w].trim().equalsIgnoreCase("")) {
                            String[] tmptel = telef[w].split(",");

                            String teltmp = tmptel[0];
                            if (tmptel.length > 1) teltmp += " * " + tmptel[1];
                            mfTel1.addItem(teltmp);
                        }
                    }
                }
            } catch (Exception e) {}
            //mfTel1.setText(FuncoesGlobais.rmvNumero(pResult.getString("celular")));
            
            mfMae.setText(pResult.getString("mae"));
            mfPai.setText(pResult.getString("pai"));
            mfEmpresa.setText(pResult.getString("empresa"));

            try {mfDtAdmis.setDate(Dates.StringtoDate(pResult.getString("dtadmis"), "yyyy-MM-dd"));} catch (Exception ex) {mfDtAdmis.setDate(null);}
            mfEndereco.setText(pResult.getString("end"));
            mfNumero.setText(pResult.getString("num"));
            mfCplto.setText(pResult.getString("compl"));
            mfBairro.setText(pResult.getString("bairro"));
            mfCidade.setText(pResult.getString("cidade"));
            mfEstado.setText(pResult.getString("estado"));
            mfCep.setText(pResult.getString("cep"));
            mfTelEmpresa.setText(FuncoesGlobais.rmvNumero(pResult.getString("tel")));
            mfRamalEmpresa.setText(pResult.getString("ramal"));
            mfCargo.setText(pResult.getString("cargo"));
            mfSalario.setText(pResult.getString("salario"));
            
            // Email
            List<String> tEmail = new ArrayList<>();
            String tpEmail = null;
            try {
                tpEmail = pResult.getString("email");
            } catch (SQLException ex) {}
            if (tpEmail != null) {
                String sepEmail = null;
                int pos = -1;
                pos = tpEmail.indexOf(" ");
                if (pos == -1) {
                    pos = tpEmail.indexOf(",");
                    if (pos == -1) {
                        pos = tpEmail.indexOf(";");
                        if (pos != -1) sepEmail = ";";
                    } else sepEmail = ",";
                } else sepEmail = " ";

                if (sepEmail != null && tpEmail != null) {
                    String[] atpEmail = tpEmail.split(sepEmail);
                    for (String item : atpEmail) {
                        tEmail.add(item.replace(" ", "").replace(",", "").replace(";", ""));
                    }
                } else if (sepEmail == null && tpEmail != null) {
                    tEmail.add(tpEmail);
                }
            }
            mfEmail.setModel(new DefaultComboBoxModel<>(tEmail.toArray(new String[0])));
            //mfEmail.setText(pResult.getString("email"));
            
            mfConjugue.setText(pResult.getString("conjugue"));
            mfConjSexo.getModel().setSelectedItem(pResult.getString("conjsexo"));
            try {mfDtNascConj.setDate(Dates.StringtoDate(pResult.getString("cjdtnasc"), "yyyy-MM-dd"));} catch (Exception ex) {mfDtNascConj.setDate(null);}
            mfCpfConj.setText(pResult.getString("cjcpf"));
            mfIdentidadeConj.setText(pResult.getString("cjrg"));
            mfSalarioConj.setText(pResult.getString("cjsalario"));
            mfEmpresaConj.setText(pResult.getString("cjempresa"));
            mfEmpresaTelConj.setText(FuncoesGlobais.rmvNumero(pResult.getString("cjtel")));
            mfEmpresaRamalConj.setText(pResult.getString("cjramal"));
        } else {
            // Pessoa Jurica (limpar dados dos campos física)
            mjRazao.setText(pResult.getString("nomerazao"));
            mjFantasia.setText(pResult.getString("fantasia"));
            mjEndereco.setText(pResult.getString("end"));
            mjNumero.setText(pResult.getString("num"));
            mjCplto.setText(pResult.getString("compl"));
            mjBairro.setText(pResult.getString("bairro"));
            mjCidade.setText(pResult.getString("cidade"));
            mjEstado.setText(pResult.getString("estado"));
            mjCep.setText(pResult.getString("cep"));
            
            mjTelefone.removeAllItems();
            String[] telef = null;
            try { telef = pResult.getString("celular").split(";");
                if (telef != null) {
                    for (int w=0;w<telef.length;w++) {
                        if (!telef[w].trim().equalsIgnoreCase("")) {
                            String[] tmptel = telef[w].split(",");

                            String teltmp = tmptel[0];
                            if (tmptel.length > 1) teltmp += " * " + tmptel[1];
                            mjTelefone.addItem(teltmp);
                        }
                    }
                }
            } catch (Exception e) {e.printStackTrace();}
            //mjTelefone.setText(FuncoesGlobais.rmvNumero(pResult.getString("tel")));
            
            //mjRamal.setText(pResult.getString("ramal"));
            //mjCelular.setText(FuncoesGlobais.rmvNumero(pResult.getString("celular")));
            try {mjDtContratoSocial.setDate(Dates.StringtoDate(pResult.getString("dtnasc"), "yyyy-MM-dd"));} catch (Exception ex) {mjDtContratoSocial.setDate(null);}

            // Email
            List<String> tEmail = new ArrayList<>();
            String tpEmail = null;
            try {
                tpEmail = pResult.getString("email");
            } catch (SQLException ex) {}
            if (tpEmail != null) {
                String sepEmail = null;
                int pos = -1;
                pos = tpEmail.indexOf(" ");
                if (pos == -1) {
                    pos = tpEmail.indexOf(",");
                    if (pos == -1) {
                        pos = tpEmail.indexOf(";");
                        if (pos != -1) sepEmail = ";";
                    } else sepEmail = ",";
                } else sepEmail = " ";

                if (sepEmail != null && tpEmail != null) {
                    String[] atpEmail = tpEmail.split(sepEmail);
                    for (String item : atpEmail) {
                        tEmail.add(item.replace(" ", "").replace(",", "").replace(";", ""));
                    }
                } else if (sepEmail == null && tpEmail != null) {
                    tEmail.add(tpEmail);
                }
            }
            mjEmail.setModel(new DefaultComboBoxModel<>(tEmail.toArray(new String[0])));
            //mjEmail.setText(pResult.getString("email"));

            FillSocios(jSocios, pResult.getString("contrato"));
        }

        FillFiadores(tbFiadores, String.valueOf(pResult.getInt("contrato")));
        
        if ((" " + pResult.getString("fiador1uf")).trim().equals("X")) {
            btIncluir.setEnabled(false);
            btCarteira.setEnabled(false);
            btGravar.setEnabled(false);
            VariaveisGlobais.isBloqueado = true;
            
            jDadosIniciais.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Locatário INATIVO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(255, 0, 0))); // NOI18N
            jDadosIniciais.setFont(new java.awt.Font("SansSerif", 0, 8)); // NOI18N
        } else {
            btIncluir.setEnabled(true);
            btCarteira.setEnabled(true);
            btGravar.setEnabled(true);
            VariaveisGlobais.isBloqueado = false;
            
            jDadosIniciais.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jDadosIniciais.setFont(new java.awt.Font("SansSerif", 0, 8)); // NOI18N
        }
    }
    
    private void LimpaDados() {
        mRgprp.setText("");
        mRgimv.setText("");
        mTpImv.setText("");
        mContrato.setText("");

        jrbFisica.setSelected(true);
        mCpf.setValue("");
        mCnpj.setText("");

        jFisica.setVisible(true);
        jJuridica.setVisible(false);

        jrbFisica.setEnabled(true);
        jrbJuridica.setEnabled(false);

        mIdentidade.setText("");

        // Pessoa Física (limpar dados dos campos juridica)
        mfNome.setText("");
        mfSexo.setSelectedIndex(0);
        mfDtNasc.setDate(null);
        mfNacionalidade.setText("");
        mfEstCivil.setSelectedIndex(0);
        //mfTel1.setText("");
        mfTel1.removeAllItems();
        mfTel1.setSelectedIndex(-1);
        //mfTel2.setText("");
        mfMae.setText("");
        mfPai.setText("");
        mfEmpresa.setText("");
        mfDtAdmis.setDate(null);
        mfEndereco.setText("");
        mfNumero.setText("");
        mfCplto.setText("");
        mfBairro.setText("");
        mfCidade.setText("");
        mfEstado.setText("");
        mfCep.setText("");
        mfTelEmpresa.setText("");
        mfRamalEmpresa.setText("");
        mfCargo.setText("");
        mfSalario.setText("");
        mfEmail.removeAllItems();
        mfEmail.setSelectedIndex(-1);
        mfConjugue.setText("");
        mfConjSexo.setSelectedIndex(0);
        mfDtNascConj.setDate(null);
        mfCpfConj.setText("");
        mfIdentidadeConj.setText("");
        mfSalarioConj.setText("");
        mfEmpresaConj.setText("");
        mfEmpresaTelConj.setText("");
        mfEmpresaRamalConj.setText("");

        // Pessoa Jurica (limpar dados dos campos física)
        mjRazao.setText("");
        mjFantasia.setText("");
        mjEndereco.setText("");
        mjNumero.setText("");
        mjCplto.setText("");
        mjBairro.setText("");
        mjCidade.setText("");
        mjEstado.setText("");
        mjCep.setText("");
        mjTelefone.removeAllItems();
        mjTelefone.setSelectedIndex(-1);
        //mjTelefone.setText("");
        //mfTel2.setText("");
        //mjRamal.setText("");
        //mjCelular.setText("");
        mjDtContratoSocial.setDate(null);
        mjEmail.removeAllItems();
        mjEmail.setSelectedIndex(-1);
        //mjEmail.setText("");

        TableControl.header(jSocios, new String[][] {{"cpfcnpj","nomerazao","cargo"},{"150","500","150"}});

        TableControl.header(tbFiadores, new String[][] {{"contrato","cpfcnpj","nomerazao"},{"50","120","500"}});
    }

    private void GravarDados() throws SQLException {
        int iNewContrato = 0;
        if (bNew) {
            int NewContrato = 0;
            NewContrato = Integer.parseInt(conn.LerParametros("PRECONTRATO"));
            iNewContrato = NewContrato + 1;

            String cPar[] = {"PRECONTRATO",String.valueOf(iNewContrato),"NUMERICO"};
            try {
                conn.GravarParametros(cPar);
            } catch (SQLException ex) { ex.printStackTrace(); }

        } else iNewContrato = Integer.parseInt(mContrato.getText());

        if (bNew) {
            pResult.moveToInsertRow();

            pResult.updateInt("rgprp", Integer.parseInt(mRgprp.getText().trim()));
            pResult.updateInt("rgimv", Integer.parseInt(mRgimv.getText().trim()));
            pResult.updateInt("contrato", iNewContrato);
            pResult.updateString("tploca", (jrbFisica.isSelected() ? "F" : "J"));

            mContrato.setText(Integer.toString(iNewContrato));
        }

        if (jrbFisica.isSelected()) {
            pResult.updateString("cpfcnpj", mCpf.getText());
            pResult.updateString("rginsc", mIdentidade.getText());

            pResult.updateString("nomerazao", mfNome.getText());
            try {pResult.updateString("sexo", mfSexo.getSelectedItem().toString());} catch (Exception e) {}

            try {if (mfDtNasc.getDate() != null) pResult.updateString("dtnasc", Dates.DateFormata("yyyy-MM-dd", mfDtNasc.getDate()));} catch (Exception ex) {ex.printStackTrace();}
            pResult.updateString("naciona", mfNacionalidade.getText());
            pResult.updateString("ecivil", (mfEstCivil.getSelectedItem().toString() + "          ").substring(0, 10));
            
            String telef = "";
            for (int w=0;w<mfTel1.getItemCount();w++) {
                String tmptel = mfTel1.getModel().getElementAt(w).toString();
                try {
                    telef += tmptel.substring(0, tmptel.indexOf("*") - 1) + "," + tmptel.substring(tmptel.indexOf("*") + 1) + ";";
                } catch (Exception e) {telef = tmptel;}
            }
            if (!telef.isEmpty()) {
                pResult.updateString("celular",telef.substring(0, telef.length() - 1));
            } else {
                pResult.updateString("celular",null);
            }
            //pResult.updateString("celular", mfTel1.getText());
            
            pResult.updateString("mae", mfMae.getText());
            pResult.updateString("pai",mfPai.getText());
            pResult.updateString("empresa", mfEmpresa.getText());


            try {if (mfDtAdmis.getDate() != null) pResult.updateString("dtadmis",Dates.DateFormata("yyyy-MM-dd",mfDtAdmis.getDate()));} catch (Exception ex) {ex.printStackTrace();}

            pResult.updateString("end",mfEndereco.getText());
            pResult.updateString("num",mfNumero.getText());
            pResult.updateString("compl", mfCplto.getText());
            pResult.updateString("bairro", mfBairro.getText());
            pResult.updateString("cidade", mfCidade.getText());
            pResult.updateString("estado", mfEstado.getText());
            pResult.updateString("cep", mfCep.getText());
            
            pResult.updateString("tel", mfTelEmpresa.getText());
            
            //pResult.updateString("fiador1tel",mfTel2.getText());            
            //pResult.updateString("ramal", mfRamalEmpresa.getText());
            pResult.updateString("cargo", mfCargo.getText());
            pResult.updateString("salario", mfSalario.getText());
            
            String tpEmail = "";
            for (int i=0; i < mfEmail.getItemCount(); i++ ) tpEmail += mfEmail.getItemAt(i).toString() + ";";
            pResult.updateString("email", tpEmail);            
            //pResult.updateString("email", mfEmail.getText());
            pResult.updateString("conjugue", mfConjugue.getText());

            try {pResult.updateString("conjsexo", mfConjSexo.getSelectedItem().toString());} catch (Exception e) {}

            try {if (mfDtNascConj.getDate() != null) pResult.updateString("cjdtnasc", Dates.DateFormata("yyyy-MM-dd", mfDtNascConj.getDate()));} catch (Exception ex) {ex.printStackTrace();}

            pResult.updateString("cjcpf", mfCpfConj.getText());
            pResult.updateString("cjrg", mfIdentidadeConj.getText());
            pResult.updateString("cjsalario", mfSalarioConj.getText());
            pResult.updateString("cjempresa", mfEmpresaConj.getText());
            pResult.updateString("cjtel", mfEmpresaTelConj.getText());
            pResult.updateString("cjramal", mfEmpresaRamalConj.getText());
        } else {
            pResult.updateString("cpfcnpj", mCnpj.getText());
            pResult.updateString("rginsc", mIdentidade.getText());

            pResult.updateString("nomerazao", mjRazao.getText());
            pResult.updateString("fantasia", mjFantasia.getText());
            pResult.updateString("end", mjEndereco.getText());
            pResult.updateString("num", mjNumero.getText());
            pResult.updateString("compl", mjCplto.getText());
            pResult.updateString("bairro", mjBairro.getText());
            pResult.updateString("cidade", mjCidade.getText());
            pResult.updateString("estado", mjEstado.getText());
            pResult.updateString("cep", mjCep.getText());
            
            //pResult.updateString("tel", mjTelefone.getText());
            
            //pResult.updateString("fiador1tel",mfTel2.getText());
            //pResult.updateString("ramal", mjRamal.getText());

            String telef = "";
            for (int w=0;w<mjTelefone.getItemCount();w++) {
                String tmptel = mjTelefone.getModel().getElementAt(w).toString();
                try {
                    telef += tmptel.substring(0, tmptel.indexOf("*") - 1) + "," + tmptel.substring(tmptel.indexOf("*") + 1) + ";";
                } catch (Exception e) {telef = tmptel;}
            }
            if (!telef.isEmpty()) {
                pResult.updateString("celular",telef.substring(0, telef.length() - 1));
            } else {
                pResult.updateString("celular",null);
            }
            //pResult.updateString("celular", mjCelular.getText());

            try {if (mjDtContratoSocial.getDate() != null) pResult.updateString("dtnasc", Dates.DateFormata("yyyy-MM-dd", mjDtContratoSocial.getDate()));} catch (Exception ex) {ex.printStackTrace();}

            String tpEmail = "";
            for (int i=0; i < mjEmail.getItemCount(); i++ ) tpEmail += mjEmail.getItemAt(i).toString() + ";";
            pResult.updateString("email", tpEmail);
            //pResult.updateString("email", mjEmail.getText());
        }

        pResult.updateString("tpimovel", mTpImv.getText().trim());

        if (bNew) {
            mContrato.setText(Integer.toString(iNewContrato));
            pResult.insertRow();
        } else {
            pResult.updateRow();
        }

    }

    private void FillSocios(JTable table, String contrato) {
        // Seta Cabecario
        TableControl.header(table, new String[][] {{"cpfcnpj","nomerazao","cargo"},{"150","500","150"}});

        String sSql = "SELECT * FROM prelocatarios WHERE contrato = '" + contrato + "';";
        ResultSet imResult = this.conn.AbrirTabela(sSql, ResultSet.CONCUR_READ_ONLY);

        try {
            imResult.next();
            int i = 0;
            for (i=1;i<=4;i++) {
                String sCpfCnpj = null;
                try {
                    sCpfCnpj = imResult.getString("sociocpf" + Integer.toString(i));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                String sNomeRazao = null;
                try {
                    sNomeRazao = imResult.getString("socionome" + Integer.toString(i));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                String sCargo = null;
                try {
                    sCargo = imResult.getString("sociocargo" + Integer.toString(i));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                if (sCpfCnpj != null) {
                    TableControl.add(table, new String[][]{{sCpfCnpj, sNomeRazao, sCargo},{"C","L","L"}}, true);
                }
            }
        } catch (Exception e) {}
        DbMain.FecharTabela(imResult);

        return;
    }

    private void FillFiadores(JTable table, String contrato) {
        // Seta Cabecario
        TableControl.header(table, new String[][] {{"contrato","cpfcnpj","nomerazao"},{"50","120","500"}});

        String sSql = "SELECT contrato, cpfcnpj, nomerazao FROM prefiadores WHERE contrato = '" + contrato + "' ORDER BY nomerazao;";
        ResultSet imResult = this.conn.AbrirTabela(sSql, ResultSet.CONCUR_READ_ONLY);

        try {
            while (imResult.next()) {
                String tcontrato = String.valueOf(imResult.getInt("contrato"));
                String tcpfcnpj = imResult.getString("cpfcnpj");
                String tnome = imResult.getString("nomerazao").toUpperCase().trim() ;

                TableControl.add(table, new String[][]{{tcontrato, tcpfcnpj, tnome},{"C","C","L"}}, true);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        DbMain.FecharTabela(imResult);

        return;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jDadosIniciais = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        mTpImv = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        mContrato = new LimitedTextField(6);
        jrbFisica = new javax.swing.JRadioButton();
        jrbJuridica = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        mIdentidade = new LimitedTextField(20);
        mCpf = new javax.swing.JFormattedTextField();
        mRgprp = new javax.swing.JTextField();
        mRgimv = new javax.swing.JTextField();
        mCnpj = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        jDados = new javax.swing.JTabbedPane();
        jJuridica = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        mjRazao = new LimitedTextField(60);
        jLabel47 = new javax.swing.JLabel();
        mjFantasia = new LimitedTextField(60);
        jLabel48 = new javax.swing.JLabel();
        mjEndereco = new LimitedTextField(60);
        jLabel49 = new javax.swing.JLabel();
        mjNumero = new LimitedTextField(10);
        jLabel50 = new javax.swing.JLabel();
        mjCplto = new LimitedTextField(15);
        jLabel51 = new javax.swing.JLabel();
        mjBairro = new LimitedTextField(25);
        jLabel52 = new javax.swing.JLabel();
        mjCidade = new LimitedTextField(25);
        jLabel53 = new javax.swing.JLabel();
        mjEstado = new LimitedTextField(2);
        jLabel54 = new javax.swing.JLabel();
        mjCep = new javax.swing.JFormattedTextField();
        jLabel55 = new javax.swing.JLabel();
        mjDtContratoSocial = new com.toedter.calendar.JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jLabel58 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jSocios = new javax.swing.JTable();
        btFicha = new javax.swing.JButton();
        btExcluirFicha = new javax.swing.JButton();
        jbtBuscaCep = new javax.swing.JButton();
        mjEmail = new javax.swing.JComboBox<>();
        btEmailPlus = new javax.swing.JButton();
        btEmailMinus = new javax.swing.JButton();
        mjTelefone = new javax.swing.JComboBox();
        plus = new javax.swing.JLabel();
        minus = new javax.swing.JLabel();
        jFisica = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        mfNome = new LimitedTextField(60);
        jLabel9 = new javax.swing.JLabel();
        mfDtNasc = new com.toedter.calendar.JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        mfNacionalidade = new LimitedTextField(25);
        jLabel15 = new javax.swing.JLabel();
        mfEstCivil = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        mfMae = new LimitedTextField(60);
        mfPai = new LimitedTextField(60);
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        mfEmpresa = new LimitedTextField(60);
        jLabel16 = new javax.swing.JLabel();
        mfDtAdmis = new com.toedter.calendar.JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jLabel17 = new javax.swing.JLabel();
        mfEndereco = new LimitedTextField(60);
        jLabel18 = new javax.swing.JLabel();
        mfNumero = new LimitedTextField(10);
        mfCplto = new LimitedTextField(15);
        jLabel20 = new javax.swing.JLabel();
        mfBairro = new LimitedTextField(25);
        jLabel21 = new javax.swing.JLabel();
        mfCidade = new LimitedTextField(25);
        jLabel22 = new javax.swing.JLabel();
        mfEstado = new LimitedTextField(2);
        jLabel23 = new javax.swing.JLabel();
        mfCep = new javax.swing.JFormattedTextField();
        jLabel24 = new javax.swing.JLabel();
        mfTelEmpresa = new javax.swing.JFormattedTextField();
        mfRamalEmpresa = new LimitedTextField(4);
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        mfCargo = new LimitedTextField(25);
        jLabel27 = new javax.swing.JLabel();
        mfSalario = new javax.swing.JFormattedTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        mfConjugue = new LimitedTextField(60);
        jLabel30 = new javax.swing.JLabel();
        mfDtNascConj = new com.toedter.calendar.JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        mfIdentidadeConj = new LimitedTextField(15);
        jLabel33 = new javax.swing.JLabel();
        mfSalarioConj = new javax.swing.JFormattedTextField();
        jLabel34 = new javax.swing.JLabel();
        mfEmpresaConj = new LimitedTextField(60);
        jLabel35 = new javax.swing.JLabel();
        mfEmpresaTelConj = new javax.swing.JFormattedTextField();
        jLabel36 = new javax.swing.JLabel();
        mfEmpresaRamalConj = new LimitedTextField(4);
        jLabel61 = new javax.swing.JLabel();
        mfCpfConj = new javax.swing.JFormattedTextField();
        jbtBuscaCep1 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        mfSexo = new javax.swing.JComboBox();
        jLabel62 = new javax.swing.JLabel();
        mfConjSexo = new javax.swing.JComboBox();
        mfEmail = new javax.swing.JComboBox<>();
        btEmailPlus1 = new javax.swing.JButton();
        btEmailMinus1 = new javax.swing.JButton();
        mfTel1 = new javax.swing.JComboBox();
        plus1 = new javax.swing.JLabel();
        minus1 = new javax.swing.JLabel();
        mFiaCorHist = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbFiadores = new javax.swing.JTable();
        jBotoes = new javax.swing.JPanel();
        btIncluir = new javax.swing.JButton();
        btCarteira = new javax.swing.JButton();
        btTras = new javax.swing.JButton();
        btFrente = new javax.swing.JButton();
        btIrPara = new javax.swing.JButton();
        btGravar = new javax.swing.JButton();
        btRetornar = new javax.swing.JButton();
        btFiador = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        jBtGeraCtro = new javax.swing.JButton();
        jBtConsolidar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle(".:: Pré - Cadastro de Locatários / Condominos");
        setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        setVisible(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jDadosIniciais.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jDadosIniciais.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        jLabel1.setText("Prop.:");

        jLabel2.setText("Imóvel:");

        jLabel3.setText("Tp.Imóvel:");

        mTpImv.setDisabledTextColor(new java.awt.Color(147, 147, 1));
        mTpImv.setEnabled(false);

        jLabel4.setText("Contrato:");

        mContrato.setEditable(false);
        mContrato.setForeground(new java.awt.Color(0, 41, 255));
        mContrato.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        mContrato.setDisabledTextColor(new java.awt.Color(0, 41, 255));
        mContrato.setEnabled(false);
        mContrato.setName("mContrato"); // NOI18N
        mContrato.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                mContratoFocusLost(evt);
            }
        });

        buttonGroup1.add(jrbFisica);
        jrbFisica.setSelected(true);
        jrbFisica.setText("Física");
        jrbFisica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbFisicaActionPerformed(evt);
            }
        });

        buttonGroup1.add(jrbJuridica);
        jrbJuridica.setText("Jurídica");
        jrbJuridica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbJuridicaActionPerformed(evt);
            }
        });

        jLabel5.setText("Cnpj:");

        jLabel6.setText("RG/Insc:");

        try {
            mCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        mCpf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                mCpfFocusLost(evt);
            }
        });

        mRgprp.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        mRgprp.setDisabledTextColor(new java.awt.Color(21, 1, 176));
        mRgprp.setEnabled(false);

        mRgimv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        mRgimv.setDisabledTextColor(new java.awt.Color(1, 169, 37));
        mRgimv.setEnabled(false);

        try {
            mCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        mCnpj.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                mCnpjFocusLost(evt);
            }
        });

        jLabel7.setText("Cpf:");

        javax.swing.GroupLayout jDadosIniciaisLayout = new javax.swing.GroupLayout(jDadosIniciais);
        jDadosIniciais.setLayout(jDadosIniciaisLayout);
        jDadosIniciaisLayout.setHorizontalGroup(
            jDadosIniciaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDadosIniciaisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDadosIniciaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDadosIniciaisLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mRgprp, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mRgimv, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mTpImv)
                        .addGap(120, 120, 120)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(mContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jDadosIniciaisLayout.createSequentialGroup()
                        .addComponent(jrbFisica)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jrbJuridica)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(8, 8, 8)
                        .addComponent(mCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mIdentidade, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))))
        );
        jDadosIniciaisLayout.setVerticalGroup(
            jDadosIniciaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDadosIniciaisLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jDadosIniciaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDadosIniciaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(mContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDadosIniciaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(mRgprp, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(mRgimv, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(mTpImv, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jDadosIniciaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDadosIniciaisLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jDadosIniciaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mIdentidade, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jDadosIniciaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jrbFisica)
                        .addComponent(jrbJuridica)
                        .addComponent(mCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(mCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jJuridica.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jJuridica.setPreferredSize(new java.awt.Dimension(808, 329));

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel46.setText("Razão Social:");

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Nome Fantasia:");

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Endereço:");

        mjEndereco.setName("mjEndereco"); // NOI18N

        jLabel49.setText("N°.:");

        mjNumero.setName("mjNumero"); // NOI18N

        jLabel50.setText("Cplto:");

        mjCplto.setName("mjCplto"); // NOI18N

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel51.setText("Bairro:");

        mjBairro.setName("mjBairro"); // NOI18N

        jLabel52.setText("Cidade:");

        mjCidade.setName("mjCidade"); // NOI18N

        jLabel53.setText("UF:");

        mjEstado.setName("mjEstado"); // NOI18N

        jLabel54.setText("Cep:");

        try {
            mjCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel55.setText("Telefone:");

        mjDtContratoSocial.setDate(new java.util.Date(-2208977612000L));

        jLabel58.setText("Dt.Contr Socl:");

        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel60.setText("E-Mail:");

        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Sócios"), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED)));
        jPanel1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N

        jSocios.setAutoCreateRowSorter(true);
        jSocios.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jSocios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jSocios.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jSocios.getTableHeader().setResizingAllowed(false);
        jSocios.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(jSocios);

        btFicha.setText("Ficha");
        btFicha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFichaActionPerformed(evt);
            }
        });

        btExcluirFicha.setText("Excluir");
        btExcluirFicha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirFichaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btFicha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btExcluirFicha))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btFicha)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btExcluirFicha)
                        .addGap(65, 65, 65))))
        );

        jbtBuscaCep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Figuras/find.png"))); // NOI18N
        jbtBuscaCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtBuscaCepActionPerformed(evt);
            }
        });

        btEmailPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/plus.png"))); // NOI18N
        btEmailPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEmailPlusActionPerformed(evt);
            }
        });

        btEmailMinus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/minus.png"))); // NOI18N
        btEmailMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEmailMinusActionPerformed(evt);
            }
        });

        plus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        plus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        plus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/plus.png"))); // NOI18N
        plus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        plus.setPreferredSize(new java.awt.Dimension(10, 10));
        plus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                plusMouseReleased(evt);
            }
        });

        minus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        minus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/minus.png"))); // NOI18N
        minus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        minus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                minusMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jJuridicaLayout = new javax.swing.GroupLayout(jJuridica);
        jJuridica.setLayout(jJuridicaLayout);
        jJuridicaLayout.setHorizontalGroup(
            jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jJuridicaLayout.createSequentialGroup()
                .addGap(618, 618, 618)
                .addComponent(mjCep, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
            .addGroup(jJuridicaLayout.createSequentialGroup()
                .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jJuridicaLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel60)
                            .addComponent(jLabel55)
                            .addComponent(jLabel51)
                            .addComponent(jLabel48)
                            .addComponent(jLabel47)
                            .addComponent(jLabel46))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jJuridicaLayout.createSequentialGroup()
                                .addComponent(mjEmail, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btEmailPlus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btEmailMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(mjFantasia)
                            .addComponent(mjRazao)
                            .addGroup(jJuridicaLayout.createSequentialGroup()
                                .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jJuridicaLayout.createSequentialGroup()
                                        .addComponent(mjBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel52)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mjCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel53)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mjEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel54))
                                    .addGroup(jJuridicaLayout.createSequentialGroup()
                                        .addComponent(mjEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jbtBuscaCep, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(22, 22, 22)
                                        .addComponent(jLabel49)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mjNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel50)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mjCplto, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jJuridicaLayout.createSequentialGroup()
                                        .addComponent(mjTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(plus, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(minus, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel58)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mjDtContratoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jJuridicaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jJuridicaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel46, jLabel47, jLabel48, jLabel51, jLabel55, jLabel60});

        jJuridicaLayout.setVerticalGroup(
            jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jJuridicaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mjRazao, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mjFantasia, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(mjEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(mjNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(mjCplto, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                    .addComponent(jbtBuscaCep, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mjBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mjCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mjEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mjCep, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel58, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(mjDtContratoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(minus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(plus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(mjTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jJuridicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btEmailPlus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btEmailMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(mjEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jJuridicaLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel46, jLabel47, jLabel48, jLabel51, jLabel55, jLabel60});

        jDados.addTab("Jurídica", jJuridica);

        jFisica.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jFisica.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Nome:");

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel9.setText("Dt.Nasc:");

        mfDtNasc.setDate(new java.util.Date(-2208977612000L));

        mfNacionalidade.setName("mfNacionalidade"); // NOI18N

        jLabel15.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel15.setText("Est.Civil:");

        mfEstCivil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Solteriro(a)", "Casado(a)", "Separado(a)", "Divorciado(a)", "Viuvo(a)" }));
        mfEstCivil.setName("mfEstCivil"); // NOI18N

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Nacionalidade:");

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel10.setText("Tels.:");

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Mãe:");

        mfMae.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mfMaeKeyPressed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel12.setText("Pai:");

        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Empresa:");

        jLabel16.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel16.setText("Dt.Adm:");

        mfDtAdmis.setDate(new java.util.Date(-2208977612000L));

        jLabel17.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Endereço:");

        mfEndereco.setName("mfEndereco"); // NOI18N

        jLabel18.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel18.setText("N°.:");

        mfNumero.setName("mfNumero"); // NOI18N

        mfCplto.setName("mfCplto"); // NOI18N

        jLabel20.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Bairro:");

        mfBairro.setName("mfBairro"); // NOI18N

        jLabel21.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel21.setText("Cidade:");

        mfCidade.setName("mfCidade"); // NOI18N

        jLabel22.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel22.setText("UF:");

        mfEstado.setName("mfEstado"); // NOI18N

        jLabel23.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel23.setText("Cep:");

        try {
            mfCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*****-***")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel24.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Telefone:");

        try {
            mfTelEmpresa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)*####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        mfRamalEmpresa.setName("mNumero"); // NOI18N

        jLabel25.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel25.setText("Ramal:");

        jLabel26.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel26.setText("Cargo:");

        mfCargo.setName("mCidade"); // NOI18N

        jLabel27.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel27.setText("Salário:");

        mfSalario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        mfSalario.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mfSalario.setText("0,00");

        jLabel28.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("E-Mail:");

        jLabel29.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Conjugue:");

        jLabel30.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Dt.Nasc:");

        mfDtNascConj.setDate(new java.util.Date(-2208977612000L));

        jLabel31.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel31.setText("Cpfj:");

        jLabel32.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel32.setText("RG:");

        jLabel33.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel33.setText("Salário:");

        mfSalarioConj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        mfSalarioConj.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mfSalarioConj.setText("0,00");

        jLabel34.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Empresa:");

        jLabel35.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel35.setText("Telefones:");

        try {
            mfEmpresaTelConj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)*####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel36.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel36.setText("Ramal:");

        mfEmpresaRamalConj.setName("mNumero"); // NOI18N

        jLabel61.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel61.setText("Cplto");

        try {
            mfCpfConj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("***.***.***-**")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jbtBuscaCep1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Figuras/find.png"))); // NOI18N
        jbtBuscaCep1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtBuscaCep1ActionPerformed(evt);
            }
        });

        jLabel19.setText("Sexo:");

        mfSexo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "M", "F" }));

        jLabel62.setText("Sexo:");

        mfConjSexo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "M", "F" }));

        btEmailPlus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/plus.png"))); // NOI18N
        btEmailPlus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEmailPlus1ActionPerformed(evt);
            }
        });

        btEmailMinus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/minus.png"))); // NOI18N
        btEmailMinus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEmailMinus1ActionPerformed(evt);
            }
        });

        plus1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        plus1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        plus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/plus.png"))); // NOI18N
        plus1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        plus1.setPreferredSize(new java.awt.Dimension(10, 10));
        plus1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                plus1MouseReleased(evt);
            }
        });

        minus1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        minus1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/minus.png"))); // NOI18N
        minus1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        minus1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                minus1MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jFisicaLayout = new javax.swing.GroupLayout(jFisica);
        jFisica.setLayout(jFisicaLayout);
        jFisicaLayout.setHorizontalGroup(
            jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFisicaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFisicaLayout.createSequentialGroup()
                        .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jFisicaLayout.createSequentialGroup()
                                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jFisicaLayout.createSequentialGroup()
                                        .addComponent(mfConjugue, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel62)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mfConjSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFisicaLayout.createSequentialGroup()
                                        .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jFisicaLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(btEmailPlus1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, 0)
                                                .addComponent(btEmailMinus1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jFisicaLayout.createSequentialGroup()
                                                .addComponent(mfEmpresaConj, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel35)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(mfEmpresaTelConj, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(27, 27, 27)
                                                .addComponent(jLabel36)
                                                .addGap(2, 2, 2)
                                                .addComponent(mfEmpresaRamalConj))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jFisicaLayout.createSequentialGroup()
                                                .addComponent(mfDtNascConj, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel31)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(mfCpfConj, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel32)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(mfIdentidadeConj)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel33)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(mfSalarioConj, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(19, 19, 19)))
                                .addGap(34, 34, 34))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFisicaLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(mfEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(mfDtAdmis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(59, 59, 59))
                            .addGroup(jFisicaLayout.createSequentialGroup()
                                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jFisicaLayout.createSequentialGroup()
                                            .addComponent(mfTelEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel25)
                                            .addGap(6, 6, 6)
                                            .addComponent(mfRamalEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel26)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(mfCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(mfSalario))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jFisicaLayout.createSequentialGroup()
                                            .addComponent(mfBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel21)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(mfCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel22)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(mfEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel23)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(mfCep, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jFisicaLayout.createSequentialGroup()
                                            .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(mfNome)
                                                .addGroup(jFisicaLayout.createSequentialGroup()
                                                    .addComponent(mfNacionalidade, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel15)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(mfEstCivil, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel19)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(mfSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel9)
                                            .addGap(6, 6, 6)
                                            .addComponent(mfDtNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jFisicaLayout.createSequentialGroup()
                                            .addComponent(mfMae, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(jFisicaLayout.createSequentialGroup()
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel10)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(mfTel1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(3, 3, 3)
                                                    .addComponent(plus1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 0, 0)
                                                    .addComponent(minus1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jFisicaLayout.createSequentialGroup()
                                                    .addGap(19, 19, 19)
                                                    .addComponent(jLabel12)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(mfPai)))))
                                    .addComponent(mfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(108, 108, 108))
                    .addGroup(jFisicaLayout.createSequentialGroup()
                        .addComponent(mfEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jbtBuscaCep1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)
                        .addGap(2, 2, 2)
                        .addComponent(mfNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel61)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(mfCplto, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jFisicaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel11, jLabel13, jLabel17, jLabel20, jLabel24, jLabel28, jLabel29, jLabel30, jLabel34});

        jFisicaLayout.setVerticalGroup(
            jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFisicaLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(mfNome, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mfDtNasc, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(mfSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFisicaLayout.createSequentialGroup()
                        .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel15)
                            .addComponent(mfNacionalidade, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mfEstCivil, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(minus1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(plus1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mfTel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5))
                    .addGroup(jFisicaLayout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mfPai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(mfMae, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(mfDtAdmis, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mfEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mfEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mfNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mfCplto, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtBuscaCep1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mfCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mfBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mfEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mfCep, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(mfTelEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mfCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mfSalario, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mfRamalEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFisicaLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jFisicaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btEmailPlus1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btEmailMinus1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5)
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mfConjugue, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62)
                    .addComponent(mfConjSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mfDtNascConj, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jFisicaLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mfIdentidadeConj, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mfSalarioConj, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mfCpfConj, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFisicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mfEmpresaConj, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mfEmpresaTelConj, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mfEmpresaRamalConj, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jDados.addTab("Física", jFisica);

        tbFiadores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbFiadores);

        mFiaCorHist.addTab("Fiadores", jScrollPane1);

        jBotoes.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        btIncluir.setText("Incluir");
        btIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btIncluirActionPerformed(evt);
            }
        });

        btCarteira.setText("Carteira");
        btCarteira.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCarteiraActionPerformed(evt);
            }
        });

        btTras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Figuras/previous.png"))); // NOI18N
        btTras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTrasActionPerformed(evt);
            }
        });

        btFrente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Figuras/next.png"))); // NOI18N
        btFrente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFrenteActionPerformed(evt);
            }
        });

        btIrPara.setText("Ir Para");
        btIrPara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btIrParaActionPerformed(evt);
            }
        });

        btGravar.setText("Gravar");
        btGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGravarActionPerformed(evt);
            }
        });

        btRetornar.setText("Retornar");
        btRetornar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRetornarActionPerformed(evt);
            }
        });

        btFiador.setText("Fiador");
        btFiador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFiadorActionPerformed(evt);
            }
        });

        btExcluir.setText("Excluir");
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        jBtGeraCtro.setText("Gerar Ctro");
        jBtGeraCtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtGeraCtroActionPerformed(evt);
            }
        });

        jBtConsolidar.setText("Consolidar");
        jBtConsolidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtConsolidarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jBotoesLayout = new javax.swing.GroupLayout(jBotoes);
        jBotoes.setLayout(jBotoesLayout);
        jBotoesLayout.setHorizontalGroup(
            jBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btIncluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btExcluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btCarteira, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jBotoesLayout.createSequentialGroup()
                        .addComponent(btTras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btFrente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btIrPara, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btFiador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btRetornar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btGravar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtConsolidar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtGeraCtro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jBotoesLayout.setVerticalGroup(
            jBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBotoesLayout.createSequentialGroup()
                .addComponent(btIncluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btExcluir)
                .addGap(31, 31, 31)
                .addComponent(btCarteira)
                .addGap(30, 30, 30)
                .addGroup(jBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btTras)
                    .addComponent(btFrente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btIrPara)
                .addGap(29, 29, 29)
                .addComponent(btFiador)
                .addGap(38, 38, 38)
                .addComponent(jBtGeraCtro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtConsolidar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btGravar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRetornar)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDadosIniciais, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDados, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(mFiaCorHist, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jDadosIniciais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDados, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mFiaCorHist, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mContratoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mContratoFocusLost
        boolean achei = false;
        try {
            achei = MoveToLoca("contrato", mContrato.getText());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        mContrato.setEditable(false);
        mContrato.setEnabled(false);
}//GEN-LAST:event_mContratoFocusLost

    private void btIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btIncluirActionPerformed
        bNew = true;

        // Bloqueio dos botões
        btIncluir.setEnabled(false);
        btCarteira.setEnabled(false);
        btTras.setEnabled(false);
        btFrente.setEnabled(false);
        btIrPara.setEnabled(false);
        btFiador.setEnabled(false);
        btGravar.setEnabled(true);
        btRetornar.setEnabled(true);
        btFicha.setEnabled(false);
        btExcluirFicha.setEnabled(false);

        jLocaInclusaoPre oInc= new jLocaInclusaoPre(null, closable);
        String action = (String)oInc.showDialog();
        if(action.equals(jLocaInclusaoPre.CANCELCMD)) {
            bNew = false;

            // Bloqueio dos botões
            btIncluir.setEnabled(true);
            btCarteira.setEnabled(true);
            btTras.setEnabled(true);
            btFrente.setEnabled(true);
            btIrPara.setEnabled(true);
            btFiador.setEnabled(true);
            btGravar.setEnabled(true);
            btRetornar.setEnabled(true);
            btFicha.setEnabled(true);
            btExcluirFicha.setEnabled(true);
            try {
                LerDados(false);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            LimpaDados();

            String tRgprp = oInc.getRgprp();
            String tRgimv = oInc.getRgimv();
            String tContrato = oInc.getContrato();
            String tTpImv = oInc.getTpImv();

            mRgprp.setText(tRgprp);
            mRgimv.setText(tRgimv);
            mContrato.setText(tContrato);
            mTpImv.setText(tTpImv);

            jrbFisica.setEnabled(true);
            jrbJuridica.setEnabled(true);
            jDados.setEnabledAt(0, false);
            jDados.setEnabledAt(1, false);
        }
    }//GEN-LAST:event_btIncluirActionPerformed

    private void btCarteiraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCarteiraActionPerformed
        String gContrato = mContrato.getText().trim();
        VariaveisGlobais.ccontrato = mContrato.getText();
        VariaveisGlobais.crgprp = mRgprp.getText();
        VariaveisGlobais.crgimv = mRgimv.getText();

        jCarteiraPre oCart = null;
        try {
            try {
                oCart = new jCarteiraPre(gContrato, null, closable);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        oCart.setVisible(true);
}//GEN-LAST:event_btCarteiraActionPerformed

    private void btTrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTrasActionPerformed
        try {
            boolean previous = pResult.previous();
            if (previous) LerDados(false);
        } catch (SQLException ex) {}
}//GEN-LAST:event_btTrasActionPerformed

    private void btFrenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFrenteActionPerformed
        try {
            boolean next = pResult.next();
            if (next) LerDados(false);
        } catch (SQLException ex) {}
}//GEN-LAST:event_btFrenteActionPerformed

    private void btIrParaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btIrParaActionPerformed
        mContrato.setEnabled(true);
        mContrato.setEditable(true);
        mContrato.selectAll();
        mContrato.requestFocus();
}//GEN-LAST:event_btIrParaActionPerformed

    private void btGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGravarActionPerformed

        if (jrbFisica.isSelected() && mfNome.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null, "Campo 'NOME' nào pode ser vazio!!!", "Erro", JOptionPane.ERROR_MESSAGE);
            mfNome.requestFocus();
            return;
        }

        if (jrbJuridica.isSelected() && mjRazao.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null, "Campo 'RAZÃO' nào pode ser vazio!!!", "Erro", JOptionPane.ERROR_MESSAGE);
            mjRazao.requestFocus();
            return;
        }

        if (jrbFisica.isSelected() && mCpf.getText().replace(".", "").replace("-", "").trim().length() == 0) {
            JOptionPane.showMessageDialog(null, "Campo 'CPF' nào pode ser vazio!!!", "Erro", JOptionPane.ERROR_MESSAGE);
            mCpf.requestFocus();
            return;
        }

        if (jrbJuridica.isSelected() && mCnpj.getText().replace(".", "").replace("-", "").replace("/","").trim().length() == 0) {
            JOptionPane.showMessageDialog(null, "Campo 'CNPJ' nào pode ser vazio!!!", "Erro", JOptionPane.ERROR_MESSAGE);
            mCnpj.requestFocus();
            return;
        }

        try {
            GravarDados();
            conn.ExecutarComando("UPDATE imoveis SET situacao = 'RESERVADO' WHERE rgprp = '" + mRgprp.getText().trim() + "' AND rgimv = '" + mRgimv.getText().trim() + "';");
        } catch (SQLException ex) { ex.printStackTrace();}

        bNew = false;

        // Bloqueio dos botões
        btIncluir.setEnabled(true);
        btCarteira.setEnabled(true);
        btTras.setEnabled(true);
        btFrente.setEnabled(true);
        btIrPara.setEnabled(true);
        btFiador.setEnabled(true);
        btGravar.setEnabled(true);
        btRetornar.setEnabled(true);
        btFicha.setEnabled(true);
        btExcluirFicha.setEnabled(true);
    }//GEN-LAST:event_btGravarActionPerformed

    private void btRetornarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRetornarActionPerformed
        if (bNew) {
            try {
                LerDados(false);

                // Bloqueio dos botões
                btIncluir.setEnabled(true);
                btCarteira.setEnabled(true);
                btTras.setEnabled(true);
                btFrente.setEnabled(true);
                btIrPara.setEnabled(true);
                btFiador.setEnabled(true);
                btGravar.setEnabled(true);
                btRetornar.setEnabled(true);
                btFicha.setEnabled(true);
                btExcluirFicha.setEnabled(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            bNew = false;
        } else this.dispose();
}//GEN-LAST:event_btRetornarActionPerformed

    private void btFiadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFiadorActionPerformed
        VariaveisGlobais.frgprp = mRgprp.getText();
        VariaveisGlobais.frgimv = mRgimv.getText();
        VariaveisGlobais.fcontrato = (tbFiadores.getSelectedRow() >  -1 ? tbFiadores.getModel().getValueAt(tbFiadores.getSelectedRow(), 0).toString() : mContrato.getText());
        VariaveisGlobais.fnome = (tbFiadores.getSelectedRow() >  -1 ? tbFiadores.getModel().getValueAt(tbFiadores.getSelectedRow(), 2).toString() : "");

        jFiadoresPre oFia = null;
        oFia = new jFiadoresPre(null, closable);
        oFia.setVisible(true);
        FillFiadores(tbFiadores, mContrato.getText());
}//GEN-LAST:event_btFiadorActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        String[] sql;
        Object[] options = { "Sim", "Não" };
        int n = JOptionPane.showOptionDialog(null,
                "Deseja excluir este pré-locatario ? \nIra apagar todas as informações...\nSem retorno.",
                "Atenção", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (n == JOptionPane.YES_OPTION) {
            sql = new String[] {
                "DELETE FROM `precarteira` WHERE contrato = '" + mContrato.getText() + "';",
                "DELETE FROM `prefiadores` WHERE contrato = '" + mContrato.getText() + "';",
                "DELETE FROM `prelocatarios` WHERE contrato = '" + mContrato.getText() + "';",
                "UPDATE imoveis SET situacao = 'VAZIO' WHERE rgprp = '" + mRgprp.getText().trim() + "' AND rgimv = '" + mRgimv.getText().trim() + "';"
            };
            
            for (String nsql : sql) {
                try {conn.ExecutarComando(nsql);} catch (Exception e) {e.printStackTrace();}
            }
            try {
                conn.Auditor("EXCLUSAO: PRELOCATARIO", mContrato.getText());
            } catch (Exception e) {}

            JOptionPane.showMessageDialog(null, "Locatário excluido!!!");
            this.dispose();
        }
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btFichaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFichaActionPerformed
        int selRow = jSocios.getSelectedRow();
        if (selRow > -1) {
            VariaveisGlobais.pResult = pResult;
            VariaveisGlobais.mContrato = mContrato.getText();
            VariaveisGlobais.mQtdSoc = jSocios.getRowCount();
            VariaveisGlobais.mPosSoc = selRow + 1;
        } else {
            VariaveisGlobais.pResult = pResult;
            VariaveisGlobais.mContrato = ""; //mContrato.getText();
            VariaveisGlobais.mQtdSoc = jSocios.getRowCount();
            VariaveisGlobais.mPosSoc = 0;
        }

        try {
            jFichaSociosPre oSoc = new jFichaSociosPre(null, closable);
            oSoc.setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Atualiza table
        try {pResult.refreshRow();} catch (SQLException e) {}
        FillSocios(jSocios, mContrato.getText());
}//GEN-LAST:event_btFichaActionPerformed

    private void btExcluirFichaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirFichaActionPerformed
        Object[] options = { "Sim", "Não" };
        int i = JOptionPane.showOptionDialog(null,
                "Tem certeza que deseja Excluir este Sócio", "Excluir",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                options, options[0]);
        if (i == JOptionPane.YES_OPTION) {
            try {
                ExcluirSocio(mContrato.getText());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        FillSocios(jSocios, mContrato.getText());
}//GEN-LAST:event_btExcluirFichaActionPerformed

    private void ExcluirSocio(String contrato) throws SQLException {
        int selRow = jSocios.getSelectedRow();
        String updSql = "";
        if (selRow > -1) {
            updSql = "SET socionome" + (selRow + 1) + " = null, ";
            updSql += "sociodtnasc" + (selRow + 1) + " = null, ";
            updSql += "socionac" + (selRow + 1) + " = null, ";
            updSql += "socioecivil" + (selRow + 1) + " = null, ";
            updSql += "sociocpf" + (selRow + 1) + " = null, ";
            updSql += "sociorg" + (selRow + 1) + " = null, ";
            updSql += "sociosalario" + (selRow + 1) + " = null, ";
            updSql += "sociocargo" + (selRow + 1) + " = null, ";
            updSql += "sociomae" + (selRow + 1) + " = null, ";
            updSql += "sociopai" + (selRow + 1) + " = null ";

        String sSql = "UPDATE locatarios " + updSql + "WHERE contrato = '" + contrato + "';";
        try {conn.ExecutarComando(sSql);} catch (Exception e) {}
        }
    }

    private void jrbFisicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbFisicaActionPerformed
        mCpf.setEnabled(true);
        jDados.setEnabledAt(1, true);
        jDados.setEnabledAt(0, false);
        mCnpj.setEnabled(false);
        jDados.setSelectedIndex(1);
    }//GEN-LAST:event_jrbFisicaActionPerformed

    private void jrbJuridicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbJuridicaActionPerformed
        mCpf.setEnabled(false);
        jDados.setEnabledAt(1, false);
        jDados.setEnabledAt(0, true);
        mCnpj.setEnabled(true);
        jDados.setSelectedIndex(0);
    }//GEN-LAST:event_jrbJuridicaActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // Retornat status de inativos
        VariaveisGlobais.Iloca = false;
    }//GEN-LAST:event_formInternalFrameClosing

    private void mfMaeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mfMaeKeyPressed
        
    }//GEN-LAST:event_mfMaeKeyPressed

    private void mCpfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mCpfFocusLost
        if (!FuncoesGlobais.ValidarCPFCNPJ(mCpf.getText())) mCpf.requestFocus();
    }//GEN-LAST:event_mCpfFocusLost

    private void mCnpjFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mCnpjFocusLost
        if (!FuncoesGlobais.ValidarCPFCNPJ(mCnpj.getText())) mCnpj.requestFocus();
    }//GEN-LAST:event_mCnpjFocusLost

    private void jbtBuscaCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtBuscaCepActionPerformed
        BuscaCep oCep = new BuscaCep(null, true);
        oCep.setVisible(true);

        Object[] dados = oCep.dados;
        oCep = null;

        if (dados != null) {
            mjEndereco.setText(dados[1].toString());
            mjBairro.setText(dados[2].toString());
            mjCidade.setText(dados[3].toString());
            mjEstado.setText(dados[4].toString());
            mjCep.setText(dados[5].toString());

            mjNumero.requestFocus();
        }
    }//GEN-LAST:event_jbtBuscaCepActionPerformed

    private void jbtBuscaCep1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtBuscaCep1ActionPerformed
        BuscaCep oCep = new BuscaCep(null, true);
        oCep.setVisible(true);

        Object[] dados = oCep.dados;
        oCep = null;

        if (dados != null) {
            mfEndereco.setText(dados[0].toString() + " " + dados[1].toString());
            mfBairro.setText(dados[2].toString());
            mfCidade.setText(dados[3].toString());
            mfEstado.setText(dados[4].toString());
            mfCep.setText(dados[5].toString());

            mfNumero.requestFocus();
        }
    }//GEN-LAST:event_jbtBuscaCep1ActionPerformed

    private void btEmailPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEmailPlusActionPerformed
        String tpEmail = null;
        tpEmail = JOptionPane.showInputDialog("Adiciona Email.");
        if (tpEmail != null) mjEmail.addItem(tpEmail);
    }//GEN-LAST:event_btEmailPlusActionPerformed

    private void btEmailMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEmailMinusActionPerformed
        if (mjEmail.getSelectedIndex() > -1) {
            mjEmail.removeItemAt(mjEmail.getSelectedIndex());
        }
    }//GEN-LAST:event_btEmailMinusActionPerformed

    private void plusMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_plusMouseReleased
        if (!plus.isEnabled()) return;

        jTelsCont oTela = new jTelsCont(null, true);
        oTela.setVisible(true);
        oTela.setAlwaysOnTop(true);
        String[] aTelCon = oTela.get();
        if (aTelCon != null) {
            mjTelefone.addItem(aTelCon[0] + " * " + aTelCon[1]);
        }
        oTela = null;
    }//GEN-LAST:event_plusMouseReleased

    private void minusMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minusMouseReleased
        if (!minus.isEnabled()) return;

        if(mjTelefone.getSelectedIndex() > -1) mjTelefone.removeItemAt(mjTelefone.getSelectedIndex());
    }//GEN-LAST:event_minusMouseReleased

    private void btEmailPlus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEmailPlus1ActionPerformed
        String tpEmail = null;
        tpEmail = JOptionPane.showInputDialog("Adiciona Email.");
        if (tpEmail != null) mfEmail.addItem(tpEmail);
    }//GEN-LAST:event_btEmailPlus1ActionPerformed

    private void btEmailMinus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEmailMinus1ActionPerformed
        if (mfEmail.getSelectedIndex() > -1) {
            mfEmail.removeItemAt(mfEmail.getSelectedIndex());
        }
    }//GEN-LAST:event_btEmailMinus1ActionPerformed

    private void plus1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_plus1MouseReleased
        if (!plus.isEnabled()) return;

        jTelsCont oTela = new jTelsCont(null, true);
        oTela.setVisible(true);
        String[] aTelCon = oTela.get();
        if (aTelCon != null) {
            mfTel1.addItem(aTelCon[0] + " * " + aTelCon[1]);
        }
        oTela = null;
    }//GEN-LAST:event_plus1MouseReleased

    private void minus1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minus1MouseReleased
        if (!minus.isEnabled()) return;

        if(mfTel1.getSelectedIndex() == -1) return;
        mfTel1.removeItemAt(mfTel1.getSelectedIndex());
    }//GEN-LAST:event_minus1MouseReleased

    private void jBtConsolidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtConsolidarActionPerformed
        String[] sql;
        Object[] options = { "Sim", "Não" };
        int n = JOptionPane.showOptionDialog(null,
                "Deseja consolidar este pré-locatario ? \nIra colcar todas as informações no cadastro de locatários e o imovel OCUPADO...\nSem retorno.",
                "Atenção", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (n == JOptionPane.NO_OPTION) return;

        int iNewContrato = 0; int NewContrato = 0;
        try {NewContrato = Integer.parseInt(conn.LerParametros("CONTRATO")); } catch (SQLException sqlEx) {}
        iNewContrato = NewContrato + 1;

        String cPar[] = {"CONTRATO",String.valueOf(iNewContrato),"NUMERICO"};
        try {
            conn.GravarParametros(cPar);
        } catch (SQLException ex) { ex.printStackTrace(); }
        
        // Atualiza Carteira
        String nsql = "INSERT INTO `carteira` (`RGPRP`, `RGIMV`, `CONTRATO`, `CAMPO`, `DTINICIO`, `DTTERMINO`, `DTVENCIMENTO`) SELECT `RGPRP`, `RGIMV`, `CONTRATO`, `CAMPO`, `DTINICIO`, `DTTERMINO`, `DTVENCIMENTO` FROM `precarteira` WHERE `contrato` = '" + mContrato.getText() + "';";
        try {conn.ExecutarComando(nsql);} catch (Exception e) {e.printStackTrace();}
        nsql = "UPDATE `carteira` SET `CONTRATO` = '" + String.valueOf(iNewContrato) + "' WHERE `CONTRATO` = '" + mContrato.getText() + "';";
        try {conn.ExecutarComando(nsql);} catch (Exception e) {e.printStackTrace();}
        nsql = "DELETE FROM `precarteira` WHERE contrato = '" + mContrato.getText() + "';";
        try {conn.ExecutarComando(nsql);} catch (Exception e) {e.printStackTrace();}
        
            // Atualiza Fiadores
        nsql =  "INSERT INTO `fiadores` (`contrato`, `rgimv`, `rgprp`, `tploca`, `cpfcnpj`, `rginsc`, " +
                "`dtnasc`, `nomerazao`, `fantasia`, `end`, `num`, `compl`, `bairro`, `cidade`, `estado`, " +
                "`cep`, `tel`, `ramal`, `celular`, `naciona`, `ecivil`, `pai`, `mae`, `empresa`, " +
                "`cargo`, `salario`, `dtadmis`, `email`, `conjugue`, `cjdtnasc`, `cjempresa`, `cjtel`, " +
                "`cjramal`, `cjsalario`, `cjrg`, `cjcpf`, `cor_nome`, `cor_end`, `cor_num`, `cor_compl`, " +
                "`cor_bairro`, `cor_cidade`, `cor_estado`, `cor_cep`, `sociodtnasc1`, `socionac1`, " +
                "`socioecivil1`, `sociocpf1`, `sociorg1`, `sociopai1`, `sociomae1`, `sociosalario1`, `sociocargo1`, " +
                "`socionome1`, `sociodtnasc2`, `socionac2`, `socioecivil2`, `sociocpf2`, `sociorg2`, " +
                "`sociopai2`, `sociomae2`, `sociosalario2`, `sociocargo2`, `socionome2`, `sociodtnasc3`, " +
                "`socionac3`, `socioecivil3`, `sociocpf3`, `sociorg3`, `sociopai3`, `sociomae3`, " +
                "`sociosalario3`, `sociocargo3`, `socionome3`, `sociodtnasc4`, `socionac4`, `socioecivil4`, " +
                "`sociocpf4`, `sociorg4`, `sociopai4`, `sociomae4`, `sociosalario4`, `sociocargo4`, `socionome4`, " +
                "`ord`) SELECT `contrato`, `rgimv`, `rgprp`, `tploca`, `cpfcnpj`, `rginsc`, " +
                "`dtnasc`, `nomerazao`, `fantasia`, `end`, `num`, `compl`, `bairro`, `cidade`, `estado`, " +
                "`cep`, `tel`, `ramal`, `celular`, `naciona`, `ecivil`, `pai`, `mae`, `empresa`, " +
                "`cargo`, `salario`, `dtadmis`, `email`, `conjugue`, `cjdtnasc`, `cjempresa`, `cjtel`, " +
                "`cjramal`, `cjsalario`, `cjrg`, `cjcpf`, `cor_nome`, `cor_end`, `cor_num`, `cor_compl`, " +
                "`cor_bairro`, `cor_cidade`, `cor_estado`, `cor_cep`, `sociodtnasc1`, `socionac1`, " +
                "`socioecivil1`, `sociocpf1`, `sociorg1`, `sociopai1`, `sociomae1`, `sociosalario1`, `sociocargo1`, " +
                "`socionome1`, `sociodtnasc2`, `socionac2`, `socioecivil2`, `sociocpf2`, `sociorg2`, " +
                "`sociopai2`, `sociomae2`, `sociosalario2`, `sociocargo2`, `socionome2`, `sociodtnasc3`, " +
                "`socionac3`, `socioecivil3`, `sociocpf3`, `sociorg3`, `sociopai3`, `sociomae3`, " +
                "`sociosalario3`, `sociocargo3`, `socionome3`, `sociodtnasc4`, `socionac4`, `socioecivil4`, " +
                "`sociocpf4`, `sociorg4`, `sociopai4`, `sociomae4`, `sociosalario4`, `sociocargo4`, `socionome4`, " +
                "`ord` FROM `prefiadores` WHERE `contrato` = '" + mContrato.getText() + "';";
        try {conn.ExecutarComando(nsql);} catch (Exception e) {e.printStackTrace();}
        nsql =  "UPDATE `fiadores` SET `contrato` = '" + String.valueOf(iNewContrato) + "' WHERE `contrato` = '" + mContrato.getText() + "';";
        try {conn.ExecutarComando(nsql);} catch (Exception e) {e.printStackTrace();}
        nsql =  "DELETE FROM `prefiadores` WHERE contrato = '" + mContrato.getText() + "';";
        try {conn.ExecutarComando(nsql);} catch (Exception e) {e.printStackTrace();}
        
        // Atualiza Locatarios
        nsql =  "INSERT INTO `locatarios` (`contrato`, `rgimv`, `rgprp`, `tpimovel`, `tploca`, `cpfcnpj`, `rginsc`, `dtnasc`, " + 
                "`nomerazao`, `fantasia`, `end`, `num`, `compl`, `bairro`, `cidade`, `estado`, `cep`, `tel`, `ramal`, `celular`, " + 
                "`naciona`, `ecivil`, `pai`, `mae`, `empresa`, `cargo`, `salario`, `dtadmis`, `email`, `conjugue`, `cjdtnasc`, " + 
                "`cjempresa`, `cjtel`, `cjramal`, `cjsalario`, `fiador1tel`, `cjrg`, `cjcpf`, `sexo`, `fiador1uf`, `conjsexo`) " + 
                "SELECT `contrato`, `rgimv`, `rgprp`, `tpimovel`, `tploca`, `cpfcnpj`, `rginsc`, `dtnasc`, " + 
                "`nomerazao`, `fantasia`, `end`, `num`, `compl`, `bairro`, `cidade`, `estado`, `cep`, `tel`, `ramal`, `celular`, " + 
                "`naciona`, `ecivil`, `pai`, `mae`, `empresa`, `cargo`, `salario`, `dtadmis`, `email`, `conjugue`, `cjdtnasc`, " + 
                "`cjempresa`, `cjtel`, `cjramal`, `cjsalario`, `fiador1tel`, `cjrg`, `cjcpf`, `sexo`, `fiador1uf`, `conjsexo` " + 
                "FROM `prelocatarios` WHERE `contrato` = '" + mContrato.getText() + "';";
        try {conn.ExecutarComando(nsql);} catch (Exception e) {e.printStackTrace();}
        nsql = "UPDATE `locatarios` SET `contrato` = '" + String.valueOf(iNewContrato) + "' WHERE `contrato` = '" + mContrato.getText() + "';";
        try {conn.ExecutarComando(nsql);} catch (Exception e) {e.printStackTrace();}
        nsql = "DELETE FROM `prelocatarios` WHERE `contrato` = '" + mContrato.getText() + "';";
        try {conn.ExecutarComando(nsql);} catch (Exception e) {e.printStackTrace();}
            
        // Atualiza Imoveis
        nsql = "UPDATE imoveis SET situacao = 'OCUPADO' WHERE rgprp = '" + mRgprp.getText().trim() + "' AND rgimv = '" + mRgimv.getText().trim() + "';";
        try {conn.ExecutarComando(nsql);} catch (Exception e) {e.printStackTrace();}                

        try {
            conn.Auditor("CONSOLIDACAO: PRELOCATARIO", "PRE:" + mContrato.getText() + " / CTR:" + String.valueOf(iNewContrato));
        } catch (Exception e) {}

        JOptionPane.showMessageDialog(null, "Pré-Locatário Consolidado!!!");
        this.dispose();
    }//GEN-LAST:event_jBtConsolidarActionPerformed

    private void jBtGeraCtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtGeraCtroActionPerformed
        String _rgprp = mRgprp.getText().trim(); String _rgimv = mRgimv.getText().trim();
        String _nome = ""; String _contrato = mContrato.getText().trim();
        if (jrbFisica.isSelected()) { _nome = mfNome.getText().trim(); } else { _nome = mjRazao.getText().trim(); }
        jGerCtro geraCtro = new jGerCtro(null, true);
        geraCtro.setDados(_rgprp, _rgimv, _contrato, _nome);
        geraCtro.show();
    }//GEN-LAST:event_jBtGeraCtroActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCarteira;
    private javax.swing.JButton btEmailMinus;
    private javax.swing.JButton btEmailMinus1;
    private javax.swing.JButton btEmailPlus;
    private javax.swing.JButton btEmailPlus1;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btExcluirFicha;
    private javax.swing.JButton btFiador;
    private javax.swing.JButton btFicha;
    private javax.swing.JButton btFrente;
    private javax.swing.JButton btGravar;
    private javax.swing.JButton btIncluir;
    private javax.swing.JButton btIrPara;
    private javax.swing.JButton btRetornar;
    private javax.swing.JButton btTras;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jBotoes;
    private javax.swing.JButton jBtConsolidar;
    private javax.swing.JButton jBtGeraCtro;
    private javax.swing.JTabbedPane jDados;
    private javax.swing.JPanel jDadosIniciais;
    private javax.swing.JPanel jFisica;
    private javax.swing.JPanel jJuridica;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jSocios;
    private javax.swing.JButton jbtBuscaCep;
    private javax.swing.JButton jbtBuscaCep1;
    private javax.swing.JRadioButton jrbFisica;
    private javax.swing.JRadioButton jrbJuridica;
    private javax.swing.JFormattedTextField mCnpj;
    private javax.swing.JTextField mContrato;
    private javax.swing.JFormattedTextField mCpf;
    private javax.swing.JTabbedPane mFiaCorHist;
    private javax.swing.JTextField mIdentidade;
    private javax.swing.JTextField mRgimv;
    private javax.swing.JTextField mRgprp;
    private javax.swing.JTextField mTpImv;
    private javax.swing.JTextField mfBairro;
    private javax.swing.JTextField mfCargo;
    private javax.swing.JFormattedTextField mfCep;
    private javax.swing.JTextField mfCidade;
    private javax.swing.JComboBox mfConjSexo;
    private javax.swing.JTextField mfConjugue;
    private javax.swing.JFormattedTextField mfCpfConj;
    private javax.swing.JTextField mfCplto;
    private com.toedter.calendar.JDateChooser mfDtAdmis;
    private com.toedter.calendar.JDateChooser mfDtNasc;
    private com.toedter.calendar.JDateChooser mfDtNascConj;
    private javax.swing.JComboBox<String> mfEmail;
    private javax.swing.JTextField mfEmpresa;
    private javax.swing.JTextField mfEmpresaConj;
    private javax.swing.JTextField mfEmpresaRamalConj;
    private javax.swing.JFormattedTextField mfEmpresaTelConj;
    private javax.swing.JTextField mfEndereco;
    private javax.swing.JComboBox mfEstCivil;
    private javax.swing.JTextField mfEstado;
    private javax.swing.JTextField mfIdentidadeConj;
    private javax.swing.JTextField mfMae;
    private javax.swing.JTextField mfNacionalidade;
    private javax.swing.JTextField mfNome;
    private javax.swing.JTextField mfNumero;
    private javax.swing.JTextField mfPai;
    private javax.swing.JTextField mfRamalEmpresa;
    private javax.swing.JFormattedTextField mfSalario;
    private javax.swing.JFormattedTextField mfSalarioConj;
    private javax.swing.JComboBox mfSexo;
    private javax.swing.JComboBox mfTel1;
    private javax.swing.JFormattedTextField mfTelEmpresa;
    private javax.swing.JLabel minus;
    private javax.swing.JLabel minus1;
    private javax.swing.JTextField mjBairro;
    private javax.swing.JFormattedTextField mjCep;
    private javax.swing.JTextField mjCidade;
    private javax.swing.JTextField mjCplto;
    private com.toedter.calendar.JDateChooser mjDtContratoSocial;
    private javax.swing.JComboBox<String> mjEmail;
    private javax.swing.JTextField mjEndereco;
    private javax.swing.JTextField mjEstado;
    private javax.swing.JTextField mjFantasia;
    private javax.swing.JTextField mjNumero;
    private javax.swing.JTextField mjRazao;
    private javax.swing.JComboBox mjTelefone;
    private javax.swing.JLabel plus;
    private javax.swing.JLabel plus1;
    private javax.swing.JTable tbFiadores;
    // End of variables declaration//GEN-END:variables

}
