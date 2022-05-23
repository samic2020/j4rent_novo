/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Movimento;

/**
 *
 * @author Samic
 */
public class classCritica {
    String id;
    String dtcritica;
    String contrato;
    String vencimento;
    String msg;
    String justifica;

    public classCritica() {}
    
    public classCritica(String id, String dtcritica, String contrato, String vencimento, String msg, String justifica) {
        this.id = id;
        this.dtcritica = dtcritica;
        this.contrato = contrato;
        this.vencimento = vencimento;
        this.msg = msg;
        this.justifica = justifica;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDtcritica() {
        return dtcritica;
    }

    public void setDtcritica(String dtcritica) {
        this.dtcritica = dtcritica;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getJustifica() {
        return justifica;
    }

    public void setJustifica(String justifica) {
        this.justifica = justifica;
    }    
}
