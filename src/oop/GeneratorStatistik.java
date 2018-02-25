package oop;
import java.text.*;
import java.io.*;
import java.util.*;

/**
 * Ukládá sledované parametry včetně HTML tagů do souboru "statistiky.html".
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class GeneratorStatistik {
  private KontejnerStudentu studenti;
  private KontejnerProblemu problemy;
  private String fStatistik;
  private ZpracujRetezec zpracuj;
  private boolean zobrazNulyCasu = true; // přepínač zobrazení nevýznamových nul v čase, viz 2 a 02..
  private int kolikZobrazitNejcProb = 2; // počet vytisknutých nejčastějších problémů

  /**
   * Vytvoří objekt třídy GeneratorStatistik. Potřebuje data z kontejnerů: studenti
   * a problemy, dále je mupředána informace o generování nevýznamových nul a
   * počtu zobrazených nejčastěji se vyskytujícíh problémů.
   */
  public GeneratorStatistik(KontejnerStudentu studenti, KontejnerProblemu problemy,
                            String fStatistik, boolean zobrazNulyCasu, int kolikZobrazitNejcProb) {
  this.studenti=studenti;
  this.problemy=problemy;
  this.fStatistik=fStatistik;
  this.zpracuj= new ZpracujRetezec();
  this.zobrazNulyCasu = zobrazNulyCasu;
  this.kolikZobrazitNejcProb = kolikZobrazitNejcProb;
  }  

  /**
   * Vrátí stav atributu zobrazNulyCasu, jehož stav rozhoduje o generování
   * nevýznamových nul.
   */
  public boolean getZobrazNulyCasu() {
    return(zobrazNulyCasu);
  }
  /**
   * Vrátí počet zobrazovaných nejčastěji se vyskytujícíh problémů.
   */
  public int getKolikZobrazitNejcProb() {
    return(kolikZobrazitNejcProb);
  }

  /**
   * Zapíše sledované nasbírané hodnoty atributů odevzdávání semestrálních prací
   * do souboru "statistiky.html"
   */
  public void zapisHodnoty() {
    Calendar cal;         /// Výpis aktuálního času a datumu//////////////
    SimpleDateFormat ds;
    DateFormat df;
    Date d=new Date();    ////////////////////////////////////////////////

    String datum;   // aktuální datum
    String cas;     // aktuální čas
    Student student;
    ArrayList seznamNejc;   // seznam nejčastějších problémů uspořádaný vzestupně

    cal=Calendar.getInstance();
    ds=new SimpleDateFormat("HH:mm:ss");
    df=DateFormat.getDateInstance(DateFormat.DEFAULT, new Locale("cs","CZ"));
    try {
      File fw = new File(fStatistik);
      FileOutputStream fo = new FileOutputStream(fw);
      OutputStreamWriter o = new OutputStreamWriter(fo, "iso-8859-2");
      BufferedWriter souborStatistik = new BufferedWriter(o);
      PrintWriter fp = new PrintWriter(souborStatistik);
      fp.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
      fp.print("<HTML><HEAD><TITLE>HTML Statistiky kontroly semestrálních prací");
      fp.print("</TITLE> <META http-equiv=Content-Type content=\"text/html;");
      fp.print("charset=iso-8859-2\"> <META content=\"MSHTML 5.50.4134.100\" ");
      fp.print("name=GENERATOR></HEAD>"+"\n");
      fp.print("<CENTER>Tato statistika byla vytvořena ");
      datum=" "+df.format(d);
      cas=" "+ds.format(cal.getTime());
      fp.print(" v "+cas+" dne  "+datum);
      fp.print("</CENTER><BR>");
      fp.print("<CENTER> <TABLE border=1> <TBODY>");
      fp.print("<TR> <TH>Charakteristika</TH> <TH> Hodnota</TH></TR> <TR>");
      fp.print("<TD>Průměrná doba odevzdávání</TD><TD>");
      if (studenti.jednouOdevzdali() != 0)
        fp.print(zpracuj.vratCasFormat(Vyucujici.getCelkDoba()/studenti.jednouOdevzdali(), getZobrazNulyCasu() ));
      else // žádný student ještě neodevzdal
        fp.print("0");

      fp.print("</TD></TR>  <TR> <TD>Průměrný počet průchodů</TD> <TD>");
      if (studenti.jednouOdevzdali() != 0)
        fp.print(""+ (float) (Vyucujici.getCelkPruchodu()/(float) studenti.jednouOdevzdali()));
      else // žádný student ještě neodevzdal
        fp.print("0");
      fp.print("</TD></TR>  <TR>  <TD>Celková doba odevzdávání</TD> <TD>");
      if (studenti.jednouOdevzdali() != 0)
        fp.print(zpracuj.vratCasFormat(Vyucujici.getCelkDoba(), getZobrazNulyCasu()));
      else // žádný student ještě neodevzdal
        fp.print("0");
      fp.print("</TD></TR><TR><TD vAlign=top rowSpan=");
      fp.print(" "+getKolikZobrazitNejcProb()+">");

      if (getKolikZobrazitNejcProb() == 1) // podmínky kvůli češtině - 1 problém
        fp.print(" "+getKolikZobrazitNejcProb()+" nejčastější problém</TD> ");
        else
          if ((getKolikZobrazitNejcProb() > 1 ) && (getKolikZobrazitNejcProb() < 5))
            fp.print(" "+getKolikZobrazitNejcProb()+" nejčastější problémy</TD> ");
          else
            fp.print(" "+getKolikZobrazitNejcProb()+" nejčastějších problémů</TD> ");
      // výpis nejčastějších problémů
      // nejčastější problém je na indexu 0
      seznamNejc=problemy.vratNejcastejsiProblemy(getKolikZobrazitNejcProb());
      if (studenti.jednouOdevzdali() != 0) {
        fp.print("<TD>" + (String) (seznamNejc.get(0)) + "</TD></TR>");
        for (int i = 1; i < getKolikZobrazitNejcProb(); i++) {
          fp.print("<TR> <TD>");
          fp.print("" + (String) (seznamNejc.get(i))); // další. nejčastější problémy
          fp.print("</TD></TR>");
        }
      }
      else // žádný student ještě neodevzdal
        fp.print("<TD>"+"-"+"</TD></TR>");


      fp.print("</TBODY></TABLE><BR><!-- Výstupní tabulka-->");
      fp.print("<TABLE cols=4 border=1><!-- Hlavička tabulky --> <TBODY> <TR>");
      fp.print("<TH>Jméno a Příjmení</TH>");
      fp.print("<TH>Počet průchodů</TH>");
      fp.print("<TH>Problémy</TH>");
      fp.print("<TH>Celková doba konzultací</TH>");
      fp.print("<TH>Odevzdáno</TH></TR><!-- Ostatní řádky -->");

      studenti.seradCelkDoba(); // kontejner studenti seřazen dle celkové doby u učitele

      for(int i=0; i<studenti.size(); i++) { // průchod seřazených studentů
        student=studenti.get(i);
        if ( (student.getCelkovyCasNavstev()) > 0 ) { // byla alespoň jedna návštěva
          fp.print("<TR>");
          fp.print("<TD>");
          fp.print(""+student.getPrijmeniAJmeno());
          fp.print("</TD> <TD>");
          fp.print(""+student.getPocetNavstev());
          fp.print("</TD> <TD>");
          if (student.getPocetNavstev()>0)
            fp.print(""+student.vratProblemyNavstev());
          fp.print("</TD> <TD>");
          fp.print(""+zpracuj.vratCasFormat(student.getCelkovyCasNavstev(), getZobrazNulyCasu()));
          fp.print("</TD> <TD>");
          if (student.getOdevzdano() == true )
            fp.print("ANO");
          else
            fp.print("NE");
          fp.print("</TD></TR>");
        }
      }
      fp.close();

      studenti.seradPrijmJm(); // seřadím kontejner studenti podle příjmení a jména

    }

    catch(IOException e) {
       System.err.println("oop: Chyba zapisu souboru: " + fStatistik);
   }
 }

   public static void main(String[] args) {
  }

}