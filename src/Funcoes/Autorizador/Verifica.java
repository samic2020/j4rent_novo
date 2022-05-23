/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Funcoes.Autorizador;

//import com.mysql.jdbc.CommunicationsException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author supervisor
 */
public class Verifica {
    public Connection conn = null;
    private String hostName = "127.0.0.1";
    private String userName = "root";
    private String password = "7kf51b"; 
    private String url = null;
    private String jdbcDriver = null;
    private String jdbcDriver8 = null;
    private String dataBaseName = null;
    private String dataBasePrefix = null;
    private String dabaBasePort = null;

    private String mdbConect = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
    private String mdbODBC = "sun.jdbc.odbc.JdbcOdbcDriver";
    private String mdbODBC8 = "com.mysql.cj.jdbc.Driver";
    
    public Verifica(String host, String user, String passwd, String databasename) {        
        jdbcDriver = "com.mysql.jdbc.Driver";
        jdbcDriver8 = "com.mysql.cj.jdbc.Driver";
        hostName = host;
        userName = user;
        password = passwd;
        
        dataBaseName = databasename;
        dataBasePrefix = "jdbc:mysql://";
        dabaBasePort = "3306";

        if ("".equals(host.trim()) && "".equals(user.trim()) && "".equals(passwd.trim()) && !"".equals(databasename.trim())) {
            jdbcDriver8 = mdbODBC8;
            url = mdbConect + databasename.trim();
            userName = "";
            password = "";
        } else {
            url = dataBasePrefix + hostName + ":"+dabaBasePort+"/" + dataBaseName +
                  "?useUnicode=true&characterEncoding=utf8";
        }

        AbrirConexao();
    }
    
    /* Abrir Banco de Dados
     * wellspinto@gmail.com
     * 12/01/2011
     */
    public Connection AbrirConexao() {
        try {
            if (conn == null) {
                try {Class.forName(jdbcDriver8);} catch (Exception e) {}
                conn = DriverManager.getConnection(url, userName, password);
            } else if (conn.isClosed()) {
                conn = null;
                return AbrirConexao();
            }
        } catch (SQLException e3) {
            JOptionPane.showMessageDialog(null, "Unidade OffLine!!!\nTente novamente...", "Atenção!!!", JOptionPane.INFORMATION_MESSAGE);
            e3.printStackTrace();
            return null;
        }
        return conn;
    }

    /**
    * Fecha a conexão com BD.
    *
    */
    public void FecharConexao() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
}
