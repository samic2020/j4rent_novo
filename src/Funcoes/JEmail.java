/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Funcoes;


import Movimento.jViewDoctos;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;

/**
 *
 * @author supervisor
 */
public class JEmail {
    DbMain conn = VariaveisGlobais.conexao;

    public String SendEmail(String contrato, String anexo, Object SubJect, Object MSG) throws MalformedURLException, SQLException {
        String[][] EmailLocaDados = null;
        String EmailLoca = null;
        String LocaNome = null;
        if (LerValor.isNumeric(contrato)) {
            EmailLocaDados = conn.LerCamposTabela(new String[] {"nomerazao","email"}, "locatarios", "contrato = '" + contrato + "'");
            EmailLoca = EmailLocaDados[1][3]; 
            LocaNome = EmailLocaDados[0][3];
        } else {
            EmailLoca = contrato;
            LocaNome = contrato;
        }
            
        //String[][] EmailLocaDados = conn.LerCamposTabela(new String[] {"nomerazao","email"}, "locatarios", "contrato = '" + contrato + "'");
        //String EmailLoca = EmailLocaDados[1][3]; String LocaNome = EmailLocaDados[0][3];
        String retorno = "";            
        return retorno;
    }
}
