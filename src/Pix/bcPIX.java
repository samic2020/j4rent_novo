/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pix;

import BancosDigital.PEMImporter;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Samic
 */
public class bcPIX {
    private String PIX_DEVELOPER_APPLICATION_KEY = "d27b27790cffab60136ee17d00050456b911a5bc";
    private String PIX_CLIENT_ID = "eyJpZCI6IjMzMzdlMTIiLCJjb2RpZ29QdWJsaWNhZG9yIjowLCJjb2RpZ29Tb2Z0d2FyZSI6MzY1ODMsInNlcXVlbmNpYWxJbnN0YWxhY2FvIjoxfQ";
    private String PIX_CLIENT_SECRET = "eyJpZCI6IjM2ZmQ1NTItMGEyOSIsImNvZGlnb1B1YmxpY2Fkb3IiOjAsImNvZGlnb1NvZnR3YXJlIjozNjU4Mywic2VxdWVuY2lhbEluc3RhbGFjYW8iOjEsInNlcXVlbmNpYWxDcmVkZW5jaWFsIjoxLCJhbWJpZW50ZSI6ImhvbW9sb2dhY2FvIiwiaWF0IjoxNjUzOTg4MTk4Njk4fQ";
    private String PIX_INC_LOTE = "https://pix.example.com/oauth/token/cobv/{@txid}";
    private String PIX_CON_LOTE = "https://pix.example.com/oauth/token/cobv";
    private String codErro;
    private String msgErro;

    public String getCodErro() { return codErro; }
    public String getMsgErro() { return msgErro; }

    public static void main(String[] args) throws Exception {
        //bcPIX c = new bcPIX();
      
        //Object[] msg = c.pdfBoleta("https://apis.bancointer.com.br/openbanking/v1/certificado/boletos/00705612432/pdf", "c:\\cert\\Inter_API_Certificado.crt", "c:\\cert\\Inter_API_Chave.key");
        //System.out.println(msg[0] + "\n" + msg[1]);        
    }

public Object[] insertLotePix(String url_ws, String path_crt, String path_key, String json_message) throws Exception{
        if (!new File(path_crt).exists()) return new Object[] {-1, new String[] {"Não achei o Certificado."}};
        if (!new File(path_key).exists()) return new Object[] {-1, new String[] {"Não achei a Chave Privada."}};
        
        File crtFile = new File(path_crt);
        File keyFile = new File(path_key);
        
        KeyStore keyStore = PEMImporter.createKeyStore(keyFile, crtFile, "samicsistemas");
        SSLContext sslContext = PEMImporter.createSSLFactory(keyFile, crtFile, "samicsistemas");
        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();        
        
        URL url = new URL(url_ws);
        HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
        uc.setSSLSocketFactory(sslContext.getSocketFactory());
        // define que vai enviar dados da requisição
        uc.setDoOutput(true);
        uc.setRequestMethod("POST");
        uc.setRequestProperty("Content-Type", "application/json");
        uc.setRequestProperty("Accept", "application/json");
        uc.setRequestProperty("developer_application_key", PIX_DEVELOPER_APPLICATION_KEY);
        uc.setRequestProperty("client_id", PIX_CLIENT_ID);
        uc.setRequestProperty("client_secret", PIX_CLIENT_SECRET);

        OutputStream wr = uc.getOutputStream();
        wr.write(json_message.getBytes());
        wr.flush();
        wr.close();

        int statusCode = uc.getResponseCode();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (statusCode == 200) ? uc.getInputStream() : uc.getErrorStream()
        ));
        String message = br.readLine();
        
//        String out;
//        while ((out = br.readLine()) != null) {
//           System.out.println(out);
//       }

        String[] infoMessage = null;
        if (statusCode != 201) {
            if (message == null) {
                infoMessage = new String[] {"Erro desconhecido."};
            } else {
                if (statusCode == 401) {
                    infoMessage = new String[] {"Erro de Autenticação."};
                } else if (statusCode == 500) {
                    infoMessage = new String[] {"Erro Interno no servidor."};
                } else if (statusCode == 503) {
                    infoMessage = new String[] {"Serviço em Manutenção."};                    
                } else if (statusCode == 400) {                    
                    JSONArray arrJson = null;
                    try {
                        JSONObject jsonOb = new JSONObject(message);      
                        arrJson = jsonOb.getJSONArray("message");
                    } catch (JSONException jex) {} finally {
                        if (arrJson != null) infoMessage = new String[] {arrJson.getString(0)};                    
                    }
                } else {
                    infoMessage = new String[] {"Erro desconhecido ou Não encontrado."};
                }
            }
            codErro = String.valueOf(statusCode); msgErro = infoMessage != null ? infoMessage[0].toString() : null;
        } else {
            JSONObject jsonOb = new JSONObject(message);
            String nossoNumero = myfunction(jsonOb,"nossoNumero").toString();
            String codigoBarras = myfunction(jsonOb,"codigoBarras").toString();
            String linhaDigitavel = myfunction(jsonOb,"linhaDigitavel").toString();
            infoMessage = new String[] {nossoNumero, codigoBarras, linhaDigitavel};
            
            codErro = null; msgErro = null;
        }       
       
       uc.disconnect();
       
       return new Object[] {statusCode, infoMessage};
    }

    public Object myfunction(JSONObject x,String y) throws JSONException {
        Object finalresult = null;    
        JSONArray keys =  x.names();
        for(int i=0;i<keys.length();i++) {
            if(finalresult!=null) {
                return finalresult;                     //To kill the recursion
            }

            String current_key = keys.get(i).toString();
            if(current_key.equals(y)) {
                finalresult = x.get(current_key);
                return finalresult;
            }

            if(x.get(current_key).getClass().getName().equals("org.json.JSONObject")) {
                myfunction((JSONObject) x.get(current_key),y);
            } else if(x.get(current_key).getClass().getName().equals("org.json.JSONArray")) {
                for(int j=0;j<((JSONArray) x.get(current_key)).length();j++) {
                    if(((JSONArray) x.get(current_key)).get(j).getClass().getName().equals("org.json.JSONObject")) {
                        myfunction((JSONObject)((JSONArray) x.get(current_key)).get(j),y);
                    }
                }
            }
        }
        return null;
    }    

}
