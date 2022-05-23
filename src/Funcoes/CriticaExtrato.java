/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Funcoes;

import Protocolo.Calculos;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author Samic
 */
public class CriticaExtrato {
    DbMain conn = VariaveisGlobais.conexao;

    public CriticaExtrato(int n) {
        if (n == -1) return;

        // Se não existe cria a tabela critica
        ExistCritica();

        if (n == 0) {
            String selectSQL = "SELECT distinct contrato, DTVENCIMENTO FROM extrato where (tag != 'X' or et_aut = 0) AND rc_aut != 0 ORDER BY contrato, DTVENCIMENTO;";
            ResultSet hrs = conn.AbrirTabela(selectSQL, ResultSet.CONCUR_READ_ONLY);
            int rcount = DbMain.RecordCount(hrs); int b = 0;
            try {
                while (hrs.next()) {
                    String tcontrato = hrs.getString("contrato");
                    String tvencimento = hrs.getString("dtvencimento");                
                    Atual(tcontrato, tvencimento);

                    int pgs = ((b++ * 100) / rcount) + 1;
                    System.out.println("Contrato: " + tcontrato + " - Vencimento: [" + tvencimento + "] - (" + b + "/" + rcount + ") " + pgs + "% >" + FuncoesGlobais.Repete("#", pgs));
                }
            } catch (SQLException ex) {}
            DbMain.FecharTabela(hrs);
        } else if (n >= 1 && n <= 3) {
            Limpa(n);
        }
    }
    
    private void Limpa(int n) {
        if (n == 1) {
            String deleteSQL = "DELETE FROM `" + VariaveisGlobais.dbnome + "`.`recibo` r WHERE r.AUTENTICACAO = 0 AND NOT EXISTS(SELECT c.* FROM `" + VariaveisGlobais.dbnome + "`.`carteira` c WHERE c.contrato = r.contrato);";
            conn.ExecutarComando(deleteSQL);
            System.out.println("Limpeza RECIBO OK.");

            deleteSQL = "DELETE FROM `" + VariaveisGlobais.dbnome + "`.`critica` r WHERE NOT EXISTS(SELECT c.* FROM `" + VariaveisGlobais.dbnome + "`.`carteira` c WHERE c.contrato = r.contrato);";
            conn.ExecutarComando(deleteSQL);
            System.out.println("Limpeza CRITICA OK.");
        } else if (n == 2) {
            String deleteSQL = "DELETE FROM `" + VariaveisGlobais.dbnome + "`.`recibo` r WHERE r.AUTENTICACAO = 0 AND NOT EXISTS(SELECT c.* FROM `" + VariaveisGlobais.dbnome + "`.`carteira` c WHERE c.contrato = r.contrato);";
            conn.ExecutarComando(deleteSQL);
            System.out.println("Limpeza RECIBO OK.");
        } else if (n == 3) {
            String deleteSQL = "DELETE FROM `" + VariaveisGlobais.dbnome + "`.`critica` r WHERE NOT EXISTS(SELECT c.* FROM `" + VariaveisGlobais.dbnome + "`.`carteira` c WHERE c.contrato = r.contrato);";
            conn.ExecutarComando(deleteSQL);
            System.out.println("Limpeza CRITICA OK.");            
        }
    }
    
    public CriticaExtrato(String jContrato, String jVencimento) {
        Atual(jContrato, jVencimento);
    }
    
    private void Atual(String jContrato, String jVencimento) {
        // Checa se já houve critica
        if (ChecaCritica(jContrato, jVencimento)) return;
        String sql = "SELECT contrato, rgprp, rgimv, campo, dtvencimento FROM extrato WHERE contrato = '&1.' AND (rc_aut <> 0) AND et_aut = 0 AND dtvencimento = '&2.' ORDER BY dtvencimento;";
               sql = FuncoesGlobais.Subst(sql, new String[] {jContrato, jVencimento});
        ResultSet hrs = conn.AbrirTabela(sql, ResultSet.CONCUR_READ_ONLY);
        
        Calculos c = new Calculos();
        try {
            c.Inicializa(hrs.getString("rgprp").toString(), hrs.getString("rgimv").toString(), hrs.getString("contrato").toString());
        } catch (SQLException ex) {}
        String[] admPer = null;
        try {
            admPer = new Calculos().percADM(hrs.getString("rgprp").toString(), hrs.getString("rgimv").toString());
        } catch (SQLException ex) {}
        
        Object[][] aa = {};
        
        try {
            while (hrs.next()) {
                String tmpCampo = hrs.getString("campo");
                String[][] rCampos = FuncoesGlobais.treeArray(tmpCampo, true);

                for (int j = 0; j<rCampos.length; j++) {
                    String tpCampo = rCampos[j][rCampos[j].length - 1].toString().trim().toUpperCase();
                    try {
                        if ("AL".equals(rCampos[j][4])) {
                            if (LerValor.isNumeric(rCampos[j][0])) {
                                float verify_fAL = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][2],2));
                                aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"AL", verify_fAL});
                                
                                float verify_fCM = 0;
                                int nPos = FuncoesGlobais.IndexOf(rCampos[j], "CM");
                                if (nPos > -1) {
                                    // "CM"
                                    verify_fCM = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][nPos].substring(2),2));
                                    aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"CM", verify_fCM});
                                }

                                float verify_fAD = 0;
                                nPos = FuncoesGlobais.IndexOf(rCampos[j], "AD");
                                if (nPos > -1) {
                                    // AD
                                    verify_fAD = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][nPos].substring(2),2));
                                    aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"AD", verify_fAD});
                                }
                                
                                float verify_fMU = 0;
                                nPos = FuncoesGlobais.IndexOf(rCampos[j], "MU");
                                if (nPos > -1) {
                                    // "MU"
                                    verify_fMU = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][nPos].substring(2),2));
                                    aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"MU", verify_fMU});
                                }

                                float verify_fJU = 0;
                                nPos = FuncoesGlobais.IndexOf(rCampos[j], "JU");
                                if (nPos > -1) {
                                    // "JU"
                                    verify_fJU = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][nPos].substring(2),2));
                                    aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"JU", verify_fJU});
                                }

                                float verify_fCO = 0;
                                nPos = FuncoesGlobais.IndexOf(rCampos[j], "CO");
                                if (nPos > -1) {
                                    // "CO"
                                    verify_fCO = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][nPos].substring(2),2));
                                    aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"CO", verify_fCO});
                                }

                                float verify_fEP = 0;
                                nPos = FuncoesGlobais.IndexOf(rCampos[j], "EP");
                                if (nPos > -1) {
                                    // "EP"
                                    verify_fEP = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][nPos].substring(2),2));
                                    aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"EP", verify_fEP});
                                }                                
                            }
                        } else if ("DC".equals(rCampos[j][4])) {
                            float verify_fDC = 0;
                            verify_fDC = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][2],2));
                            aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"AL".equals(rCampos[j][5]) ? "DCA" : "DC", verify_fDC});
                        } else if ("DF".equals(rCampos[j][4])) {
                            float verify_fDF = 0;
                            verify_fDF = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][2],2));
                            aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"AL".equals(rCampos[j][5]) ? "DFA" : "DF", verify_fDF});
                        } else if ("SG".equals(rCampos[j][4])) {
                            float verify_fSG = 0;
                            verify_fSG = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][2],2));
                            aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"SG", verify_fSG});
                        } else {
                            float verify_fTX = 0;
                            verify_fTX = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][2],2));
                            aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"TX:" + rCampos[j][0], verify_fTX});
                        }
                    } catch (Exception e) {}
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        DbMain.FecharTabela(hrs);

        Arrays.sort (aa, new Comparator() {
            public int compare(Object o1, Object o2) {
                String p1 = ((Object[])o1)[0].toString();
                String p2 = ((Object[])o2)[0].toString();
                return p1.compareTo(p2);
            }
        });

        Date dtCritica = new Date();
        
        Object[][] at = Anterior(jContrato, jVencimento);
        
        if (at.length > 0) {
            for (Object[] it : aa) {
                int pos = -1;
                pos = FuncoesGlobais.FindinObjects(at, 0, it[0].toString());
                if (pos == -1) {
                    //System.out.println("Diferença: " + it[0].toString());
                    insertCritica(jContrato, jVencimento, dtCritica, "Diferença: " + it[0].toString());
                } else {
                    if (it[0].toString().equalsIgnoreCase("AL")) {
                        if (it[0] != at[pos][0]) {
                            //System.out.println("Diferença: AL -> " + it[0].toString() + " <=> " + at[pos][0].toString());
                            insertCritica(jContrato, jVencimento, dtCritica, "Diferença: AL -> " + it[0].toString() + " <=> " + at[pos][0].toString());
                        } else {
                            //System.out.println("Igualdade: " + it[0].toString());
                        }
                    } else if (it[0].toString().equalsIgnoreCase("CM")) {
                        if (it[0] != at[pos][0]) {
                            //System.out.println("Diferença: CM -> " + it[0].toString() + " <=> " + at[pos][0].toString());
                            insertCritica(jContrato, jVencimento, dtCritica, "Diferença: CM -> " + it[0].toString() + " <=> " + at[pos][0].toString());
                        } else {
                            //System.out.println("Igualdade: " + it[0].toString());
                        }                    
                    } else {
                        if (admPer != null) {
                            switch (it[0].toString()) {
                                case "MU":
                                    if (Integer.valueOf(admPer[0]) == 100) {
                                        //System.out.println("MU não pode existir no contrato.");
                                        insertCritica(jContrato, jVencimento, dtCritica, "MU não pode existir no contrato.");
                                    }
                                    break;
                                case "JU":
                                    if (Integer.valueOf(admPer[1]) == 100) {
                                        //System.out.println("JU não pode existir no contrato.");
                                        insertCritica(jContrato, jVencimento, dtCritica, "JU não pode existir no contrato.");
                                    }
                                    break;
                                case "CO":
                                    if (Integer.valueOf(admPer[2]) == 100) {
                                        //System.out.println("CO não pode existir no contrato.");
                                        insertCritica(jContrato, jVencimento, dtCritica, "CO não pode existir no contrato.");
                                    }
                                    break;
                                case "EP":
                                    if (Integer.valueOf(admPer[3]) == 100) {
                                        //System.out.println("EP não pode existir no contrato.");
                                        insertCritica(jContrato, jVencimento, dtCritica, "EP não pode existir no contrato.");
                                    }
                                    break;
                                default:
                                    //System.out.println("Igualdade: " + it[0].toString());
                                    break;
                            }
                        }
                    }                
                }
            }
        }
        Object[] ct = ChecaTermino(jContrato, jVencimento);
        if (ct != null) {
            //System.out.println("Reajuste: " + ct[0] + " <= " + ct[1]);
            insertCritica(jContrato, jVencimento, dtCritica, "Reajuste: " + ct[0] + " <= " + ct[1]);
        }
    }
    
    private Object[][] Anterior(String jContrato, String jVencimento) {
        String sql = "SELECT contrato, rgprp, rgimv, campo, dtvencimento FROM extrato WHERE contrato = '&1.' AND (rc_aut <> 0) AND (Month(dtvencimento) = '&2.' AND Year(dtvencimento) = '&3.') ORDER BY dtvencimento;";
        Date tVencimento = Dates.DateAdd(Dates.MES, -1, Dates.StringtoDate(jVencimento, "yyyy-MM-dd"));
        String tMes = Dates.DateFormata("MM", tVencimento);
        String tAno = Dates.DateFormata("yyyy", tVencimento);
               sql = FuncoesGlobais.Subst(sql, new String[] {jContrato, tMes, tAno});
        ResultSet hrs = conn.AbrirTabela(sql, ResultSet.CONCUR_READ_ONLY);
        
        Object[][] aa = {};
        
        try {
            while (hrs.next()) {
                String tmpCampo = hrs.getString("campo");
                String[][] rCampos = FuncoesGlobais.treeArray(tmpCampo, true);

                for (int j = 0; j<rCampos.length; j++) {
                    String tpCampo = rCampos[j][rCampos[j].length - 1].toString().trim().toUpperCase();
                    try {
                        if ("AL".equals(rCampos[j][4])) {
                            if (LerValor.isNumeric(rCampos[j][0])) {
                                float verify_fAL = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][2],2));
                                aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"AL", verify_fAL});
                                
                                float verify_fCM = 0;
                                int nPos = FuncoesGlobais.IndexOf(rCampos[j], "CM");
                                if (nPos > -1) {
                                    // "CM"
                                    verify_fCM = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][nPos].substring(2),2));
                                    aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"CM", verify_fCM});
                                }

                                float verify_fAD = 0;
                                nPos = FuncoesGlobais.IndexOf(rCampos[j], "AD");
                                if (nPos > -1) {
                                    // AD
                                    verify_fAD = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][nPos].substring(2),2));
                                    aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"AD", verify_fAD});
                                }
                                
                                float verify_fMU = 0;
                                nPos = FuncoesGlobais.IndexOf(rCampos[j], "MU");
                                if (nPos > -1) {
                                    // "MU"
                                    verify_fMU = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][nPos].substring(2),2));
                                    aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"MU", verify_fMU});
                                }

                                float verify_fJU = 0;
                                nPos = FuncoesGlobais.IndexOf(rCampos[j], "JU");
                                if (nPos > -1) {
                                    // "JU"
                                    verify_fJU = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][nPos].substring(2),2));
                                    aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"JU", verify_fJU});
                                }

                                float verify_fCO = 0;
                                nPos = FuncoesGlobais.IndexOf(rCampos[j], "CO");
                                if (nPos > -1) {
                                    // "CO"
                                    verify_fCO = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][nPos].substring(2),2));
                                    aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"CO", verify_fCO});
                                }

                                float verify_fEP = 0;
                                nPos = FuncoesGlobais.IndexOf(rCampos[j], "EP");
                                if (nPos > -1) {
                                    // "EP"
                                    verify_fEP = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][nPos].substring(2),2));
                                    aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"EP", verify_fEP});
                                }                                
                            }
                        } else if ("DC".equals(rCampos[j][4])) {
                            float verify_fDC = 0;
                            verify_fDC = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][2],2));
                            aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"AL".equals(rCampos[j][5]) ? "DCA" : "DC", verify_fDC});
                        } else if ("DF".equals(rCampos[j][4])) {
                            float verify_fDF = 0;
                            verify_fDF = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][2],2));
                            aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"AL".equals(rCampos[j][5]) ? "DFA" : "DF", verify_fDF});
                        } else if ("SG".equals(rCampos[j][4])) {
                            float verify_fSG = 0;
                            verify_fSG = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][2],2));
                            aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"SG", verify_fSG});
                        } else {
                            float verify_fTX = 0;
                            verify_fTX = LerValor.StringToFloat(LerValor.FormatNumber(rCampos[j][2],2));
                            aa = FuncoesGlobais.ObjectsAdd(aa, new Object[] {"TX:" + rCampos[j][0], verify_fTX});
                        }
                    } catch (Exception e) {}
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        DbMain.FecharTabela(hrs);

        Arrays.sort (aa, new Comparator() {
            public int compare(Object o1, Object o2) {
                String p1 = ((Object[])o1)[0].toString();
                String p2 = ((Object[])o2)[0].toString();
                return p1.compareTo(p2);
            }
        });

        return aa;
    }

    private Object[] ChecaTermino(String contrato, String jVencimento) {
        Date dVencimento = Dates.StringtoDate(jVencimento, "yyyy-MM-dd");
        Object[] msg = null;        
        String[][] campos = null;
        try {
            campos = conn.LerCamposTabela(new String[] {"dtinicio","dttermino","dtadito"},"CARTEIRA", 
                    FuncoesGlobais.Subst("contrato = '&1.'", new String[] {contrato}));
        } catch (Exception e) {}
        if (campos != null) {
            String tdtini = campos[0][3];
            Date _inic = null;
            int _inic_dia = -1;
            int _inic_mes = -1;
            int _inic_ano = -1;
            if (!tdtini.isEmpty()) {
                _inic = Dates.StringtoDate(tdtini,"dd/MM/yyyy");
                _inic_dia = Integer.valueOf(Dates.DateFormata("dd", _inic));
                _inic_mes = Integer.valueOf(Dates.DateFormata("MM", _inic));
                _inic_ano = Integer.valueOf(Dates.DateFormata("yyyy", _inic));
            } else {
                msg = new Object[] {-1,"Cadastro incompleto.[DTINICIO]"};
                return msg;
            }
            
            String tdtter = campos[1][3];
            Date _term = null;
            int _term_dia = -1;
            int _term_mes = -1;
            int _term_ano = -1;
            if (!tdtter.isEmpty()) {
                _term = Dates.StringtoDate(tdtter,"dd/MM/yyyy");
                _term_dia = Integer.valueOf(Dates.DateFormata("dd", _term));
                _term_mes = Integer.valueOf(Dates.DateFormata("MM", _term));
                _term_ano = Integer.valueOf(Dates.DateFormata("yyyy", _term));
            } else {
                msg = new Object[] {-2,"Cadastro incompleto.[DTTERMINO]"};
                return msg;
            }
            
            String tdtadt = campos[2][3];
            Date _adit = null;
            int _adit_dia = -1;
            int _adit_mes = -1;
            int _adit_ano = -1;
            if (!tdtadt.isEmpty()) {
                _adit = Dates.StringtoDate(tdtadt,"dd/MM/yyyy");
                _adit_dia = Integer.valueOf(Dates.DateFormata("dd", _adit));
                _adit_mes = Integer.valueOf(Dates.DateFormata("MM", _adit));
                _adit_ano = Integer.valueOf(Dates.DateFormata("yyyy", _adit));
            }
            
            Date _comp = dVencimento;
            int _comp_dia = Integer.valueOf(Dates.DateFormata("dd", _comp));
            int _comp_mes = Integer.valueOf(Dates.DateFormata("MM", _comp));
            int _comp_ano = Integer.valueOf(Dates.DateFormata("yyyy", _comp));
            
            if (_inic_mes == _comp_mes && _inic_ano == _comp_ano) {
                // Este é o primeiro vencimento/Primeiro recibo
                msg = null;
                return msg;
            } else if (_comp_mes >= _inic_mes && _comp_ano >= _inic_ano ) {
                // Checa termino de contrato
                if (_comp_mes >= _term_mes && _comp_ano >= _term_ano) {
                    // Checa se esta em Termo aditivo
                    if (!tdtadt.isEmpty()) {
                        if (_comp_mes >= _adit_mes && _comp_ano >= _adit_ano) {
                            msg = new Object[] {-4,"Contrato vencido."};
                            return msg;
                        } else {
                            if (_comp_mes <= _adit_mes) {
                                if (_comp_mes == _adit_mes) {
                                    msg = new Object[] {-5,"Este é o mês do reajuste."};
                                    return msg;
                                } else {
                                    msg = new Object[] {-6,"Faltam [" + (_adit_mes - _comp_mes) + "] para mês do reajuste."};
                                    return msg;
                                }
                            }                            
                        }
                    } else {
                        msg = new Object[] {-3, "Contrato vencido. Sem adito [DTADITO]"};
                        return msg;
                    }                    
                } else {
                    if (_comp_mes <= _term_mes) {
                        if (_comp_mes == _term_mes) {
                            msg = new Object[] {-5,"Este é o mês do reajuste."};
                            return msg;
                        } else {
                            msg = new Object[] {-6,"Faltam [" + (_term_mes - _comp_mes) + "] para mês do reajuste."};
                            return msg;
                        }
                    }                                                    
                }
            }
        } else {
            msg = new Object[] {-2,"Cadastro incompleto. Verificar carteira."};
            return msg;
        }
        return msg;
    }

    private void insertCritica(String jContrato, String jVencimento, Date dtCritica, String jMsg) {
        String insertSQL = "INSERT INTO `critica` (contrato, vencimento, dtcritica, msg) VALUES (?, ?, ?, ?);";
        Object[][] param = {
            {"string", jContrato},
            {"date", Dates.toSqlDate(Dates.StringtoDate(jVencimento, "yyyy-MM-dd"))},
            {"date", Dates.toSqlDate(dtCritica)},
            {"string", jMsg}
        };
        conn.ExecutarComando(insertSQL, param);
    }
    
    private boolean ChecaCritica(String jContrato, String jVencimento) {
        boolean retorno = false;
        String selectSQL = "SELECT contrato, vencimento FROM critica WHERE contrato = '&1.' AND vencimento = '&2.';";
               selectSQL = FuncoesGlobais.Subst(selectSQL, new String[] {jContrato, jVencimento});
        ResultSet hrs = conn.AbrirTabela(selectSQL, ResultSet.CONCUR_READ_ONLY);        
        try {
            retorno = hrs.next();
        } catch (SQLException e) { retorno = false; }
        DbMain.FecharTabela(hrs);
        return retorno;
    }

    public void DelCritica(String jContrato, String jVencimento) {
        String deleteSQL = "DELETE FROM critica WHERE contrato = '&1.' AND vencimento = '&2.';";
               deleteSQL = FuncoesGlobais.Subst(deleteSQL, new String[] {jContrato, jVencimento});
        conn.ExecutarComando(deleteSQL);
    }
    
    public void ExistCritica() {
        if (conn == null) {
            new SettingPwd();
            String crySenha  = System.getProperty("Key", "7kf51b");
            if (crySenha != "7kf51b") {
                VariaveisGlobais.KeyPwd = CriptografiaUtil.decrypt(crySenha, CriptografiaUtil.ALGORITMO_AES, CriptografiaUtil.ALGORITMO_AES);
            } else VariaveisGlobais.KeyPwd = crySenha;                    
            conn = new DbMain(VariaveisGlobais.unidade,"root",(VariaveisGlobais.dbsenha ? VariaveisGlobais.KeyPwd : ""),VariaveisGlobais.dbnome);
            VariaveisGlobais.conexao = conn;
        }
        
        String createSQL = "CREATE TABLE IF NOT EXISTS `" + VariaveisGlobais.dbnome + "`.`critica` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT," +
                            "`contrato` VARCHAR(6) NOT NULL," +
                            "`vencimento` DATE NOT NULL," +
                            "`dtcritica` DATETIME NOT NULL," +
                            "`msg` VARCHAR(100) NOT NULL," +
                            "`justifica` varchar(100) COLLATE utf8_bin DEFAULT NULL, " +
                            "PRIMARY KEY (`id`)) " +
                            "ENGINE = MyISAM " +
                            "DEFAULT CHARACTER SET = utf8 " +
                            "COLLATE = utf8_bin;";
        conn.ExecutarComando(createSQL);
        
        createSQL = "CREATE TABLE IF NOT EXISTS `test`.`critica` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT," +
                            "`contrato` VARCHAR(6) NOT NULL," +
                            "`vencimento` DATE NOT NULL," +
                            "`dtcritica` DATETIME NOT NULL," +
                            "`msg` VARCHAR(100) NOT NULL," +
                            "`justifica` varchar(100) COLLATE utf8_bin DEFAULT NULL, " +
                            "PRIMARY KEY (`id`)) " +
                            "ENGINE = MyISAM " +
                            "DEFAULT CHARACTER SET = utf8 " +
                            "COLLATE = utf8_bin;";
        conn.ExecutarComando(createSQL);
    }
    
}
