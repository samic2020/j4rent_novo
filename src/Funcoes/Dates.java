/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Funcoes;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import javafx.scene.control.DatePicker;

/**
 *
 * @author supervisor
 */
public class Dates {
    public static final String DIA = "D";
    public static final String MES = "M";
    public static final String ANO = "A";
    public static final String HOR = "H";

    public static Date DateAdd(String patern, int Valor, Date Data) {
        GregorianCalendar dt = new GregorianCalendar();
        dt.setTime(Data);

        if ("D".equals(patern.trim().toUpperCase())) {
            dt.add(GregorianCalendar.DATE, Valor);
        } else if ("M".equals(patern.trim().toUpperCase())) {
            dt.add(GregorianCalendar.MONTH, Valor);
        } else if ("A".equals(patern.trim().toUpperCase())) {
            dt.add(GregorianCalendar.YEAR, Valor);
        }
        return dt.getTime();
    }

    public static int DateDiff(String patern, Date Data1, Date Data2) {
        long a = Data2.getTime();
        long b = Data1.getTime();
//        long mx = Math.max(a,b);
//        long mi = Math.min(a, b);
        
        long i = (a - b);
        
        long r = 0;
        if ("D".equals(patern.trim().toUpperCase())) {
            r = (i / 1000 / 60 / 60 / 24);
        } else if ("M".equals(patern.trim().toUpperCase())) {
            r = (i / 1000 / 60 / 60 / 24 / 30);
        } else if ("A".equals(patern.trim().toUpperCase())) {
            r = (i / 1000 / 60 / 60 / 24 / 365);
        }

//        int d = 0;
//        if (a > b) {
//            d = (int)r;
//        } else {
//            d = -(int)r;
//        }
        return (int)r;
    }

    public static int DiffDate(Date Data1, Date Data2) {
        Date xData1, xData2; int mult = 1;
        if (Data2.getTime() > Data1.getTime()) { 
            xData1 = Data2;
            xData2 = Data1;
            mult = -1;
        } else {
            xData1 = Data1;
            xData2 = Data2;
            mult = 1;
        }
        
        boolean mLoop = true;
        int dias = 0; /////////////////////////////////////04/09/2011 - JOAO
        while (mLoop) {
            int idia = iDay(xData2);
            int imes = iMonth(xData2);
            int iano = iYear(xData2);
            
            int fdia = iDay(xData1);
            int fmes = iMonth(xData1);
            int fano = iYear(xData1);
            
            if (idia == fdia && imes == fmes && iano == fano) {
                mLoop = false;
            } else {
                dias = dias + 1;
                xData2 = Dates.DateAdd(Dates.DIA, 1, xData2);                
            }
        }
        
        return (dias * mult);
    }
    
    public static String DateFormata(String patern, Date Data) {
        SimpleDateFormat formatter = new SimpleDateFormat(patern);

        return formatter.format(Data);
    }

    public static String DatetoString(Date Data) { return DateFormata("dd/MM/yyyy", Data); }

    public static Date StringtoDate(String Data, String patern) {
        GregorianCalendar ret = null;
        Date ter = null;
        if (!Data.trim().equalsIgnoreCase("")) {
            int posDia = patern.indexOf("dd");
            int posMes = patern.indexOf("MM");
            int posAno = patern.indexOf("yyyy");

            int vDia = Integer.valueOf(Data.substring(posDia, posDia + 2));
            int vMes = Integer.valueOf(Data.substring(posMes, posMes + 2)) - 1;
            int vAno = Integer.valueOf(Data.substring(posAno, posAno + 4));

            GregorianCalendar dt = new GregorianCalendar();
            dt.set(vAno, vMes, vDia);
            ret = dt;
        }
        
        if (ret != null) { ter = ret.getTime(); }
        return ter;
    }
    
    public static String StringtoString(String Data, String patern, String outpatern) {
        int posDia = patern.indexOf("dd");
        int posMes = patern.indexOf("MM");
        int posAno = patern.indexOf("yyyy");
        
        String vDia = Data.substring(posDia, posDia + 2);
        String vMes = Data.substring(posMes, posMes + 2);
        String vAno = Data.substring(posAno, posAno + 4);

        String newDateFormat = outpatern;
        newDateFormat = newDateFormat.replace("dd", vDia);
        newDateFormat = newDateFormat.replace("MM", vMes);
        newDateFormat = newDateFormat.replace("yyyy", vAno);

        return newDateFormat;
    }    

    public static String Month(Date Data) {
        GregorianCalendar dt = new GregorianCalendar();
        dt.setTime(Data);
        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        return meses[dt.getTime().getMonth()];
    }

    public static String ShortMonth(Date Data) {
        GregorianCalendar dt = new GregorianCalendar();
        dt.setTime(Data);
        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        return meses[dt.getTime().getMonth()].substring(0,3);
    }

    public static int iYear(Date Data) {
        GregorianCalendar dt = new GregorianCalendar();
        dt.setTime(Data);
        return dt.get(GregorianCalendar.YEAR);
    }

    public static int iMonth(Date Data) {
        GregorianCalendar dt = new GregorianCalendar();
        dt.setTime(Data);
        return dt.get(GregorianCalendar.MONTH) + 1;
    }

    public static int iDay(Date Data) {
        GregorianCalendar dt = new GregorianCalendar();
        dt.setTime(Data);
        return dt.get(GregorianCalendar.DATE);
    }

    public static int isSabadoOuDomingo(Date data) {  
        Calendar gc = GregorianCalendar.getInstance();
        gc.setTime(data);
        int diaSemana = gc.get(GregorianCalendar.DAY_OF_WEEK);  
        
        int retorno = 0;
        if (diaSemana == GregorianCalendar.SATURDAY) {
            retorno = 2;
        } else if (diaSemana == GregorianCalendar.SUNDAY) {
            retorno = 1;
        } else retorno = 0;

        return retorno;
    }      
    
    public static String ultDiaMes(Date data) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime( data );

        int dia = cal.getActualMaximum( Calendar.DAY_OF_MONTH );
        //int mes = (cal.get(Calendar.MONDAY)+1);
        //int ano = cal.get(Calendar.YEAR);        
        return FuncoesGlobais.StrZero(String.valueOf(dia), 2);
    }

    public static Date primeiraDataMes(Date data) {
        String dia = "01";
        String mes = Dates.DateFormata("MM", data);
        String ano = Dates.DateFormata("yyyy", data);
        return Dates.StringtoDate(dia + "-" + mes + "-" + ano,"dd-MM-yyyy");
    }
    
    public static Date ultimaDataMes(Date data) {
        String dia = Dates.ultDiaMes(data);
        String mes = Dates.DateFormata("MM", data);
        String ano = Dates.DateFormata("yyyy", data);
        return Dates.StringtoDate(dia + "-" + mes + "-" + ano, "dd-MM-yyyy");
    }
    
    public static Date ultimoDataMes(Date data) {
        String dia = Dates.ultDiaMes(data);
        String mes = Dates.DateFormata("MM", data);
        String ano = Dates.DateFormata("yyyy", data);
        String tData = dia + "-" + mes + "-" + ano;
        return Dates.StringtoDate(tData, "dd-MM-yyyy");
    }

    public static boolean isDateValid(String date, String pattern) {
        try {
            DateFormat df = new SimpleDateFormat(pattern);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }    
    
    public static java.sql.Date toSqlDate(DatePicker data) {
        java.sql.Date sqlDate = null;
        try {
            Date date = Date.from(data.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            sqlDate = new java.sql.Date(date.getTime());
        } catch (Exception e) {}       
        return sqlDate;
    }    
    
    public static java.sql.Date toSqlDate(Date data) {
        java.sql.Date sqlDate = null;
        try {
            sqlDate = new java.sql.Date(data.getTime());
        } catch (Exception e) {}
        return sqlDate;
    }
    
    public static LocalDate toLocalDate(Date d) {
        Instant instant = Instant.ofEpochMilli(d.getTime());
        LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }    
    
    public static long datesBetween(String pattern, Date data1, Date data2) {
        LocalDate datade =  toLocalDate(data1);
        LocalDate datapara = toLocalDate(data2);

        //Period age = Period.between(datade, datapara);
        //System.out.printf("You are now %d years, %d months and %d days old.%n",
        //        age.getYears(), age.getMonths(), age.getDays());

        // Using ChronoUnit to calculate difference in years, months and days
        // between two dates.
        long years = ChronoUnit.YEARS.between(datade, datapara);
        long months = ChronoUnit.MONTHS.between(datade, datapara);
        long days = ChronoUnit.DAYS.between(datade, datapara);

        long retorno = 0;
        if (pattern == ANO) {
            retorno = years;
        } else if (pattern == MES) {
            retorno = months;
        } else if (pattern == DIA) {
            retorno = days;    
        }
        
        return retorno;
    }
    
}
