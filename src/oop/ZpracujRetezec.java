package oop;
import java.util.*;
import java.text.*;
import java.awt.event.KeyEvent; // kvůli klávesovým zkratkám VK_F1..VK_F12

// POZN. Možná by bylo lepši udělat tuto třídu a všechny její metody statické
/**
 * Poskytuje metody pro výběr "zajímavých" řetězců z řetězce a pro práci s časem.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class ZpracujRetezec {
  /**
   * Vytvoří objekt třídy ZpracujRetezec.
   */
  public ZpracujRetezec() {
  }

  /**
   * Vrátí první dva řetězce oddělené mezerou.
   * @param s - vstupní řetězec
   */
  public String vratPrijmeniJmeno(String s) {
    String prvni="";
    String druhe="";

    StringTokenizer st=new StringTokenizer(s);
    if (st.hasMoreTokens()) { // ošetření prázdné řádky na konci souboru
      druhe = new String(st.nextToken());
      prvni = new String(st.nextToken());
    }
    return ("" + prvni + " " + druhe);
  }
  /**
   * Vrátí číslo oddělené od ostatního textu oddělovačem oddel.
   * @param s - vstupní řetězec
   * @param oddel - 0.index .. levý oddělovač , 1.index .. pravý oddělovač
   */
  public int vratKlavesZkratku(String s, String oddel) {
    String klZkratka;
    int cisloZkratky=0;

    if (s.indexOf(oddel.charAt(0)) != -1) { // nalezen znak uvozující zkratku
      klZkratka=s.substring(s.indexOf(oddel.charAt(0))+1, s.indexOf(oddel.charAt(1)));
      cisloZkratky=Integer.parseInt(klZkratka.substring(1,klZkratka.length()));
      cisloZkratky=konverzeNaFZkratku(cisloZkratky);
      return(cisloZkratky);
             //prevadim to, co je za F-kem na cislo
    }
    else
      return(-1); //zkratka uvozená oddělovačem se v řetězci s nevyskytuje
  }

  /**
   * Převede číslo klávesové zkratky na konstantu rozpoznatelnou GUI SWING.
   * @param zkratka - číslo zkratky
   */
  public int konverzeNaFZkratku(int zkratka) {
    switch (zkratka) {
        case 1: return(KeyEvent.VK_F1);
        case 2: return(KeyEvent.VK_F2);
        case 3: return(KeyEvent.VK_F3);
        case 4: return(KeyEvent.VK_F4);
        case 5: return(KeyEvent.VK_F5);
        case 6: return(KeyEvent.VK_F6);
        case 7: return(KeyEvent.VK_F7);
        case 8: return(KeyEvent.VK_F8);
        case 9: return(KeyEvent.VK_F9);
        case 10: return(KeyEvent.VK_F10);
        case 11: return(KeyEvent.VK_F11);
        case 12: return(KeyEvent.VK_F12);
        default: return(-1);

      }
  }

  /**
   * Převod řetězce čísel oddělených mezerou na ArrayList Integerů.
   * @param s - řetězec čísel oddělených mezerou
   * @param c - uvozující znak čísel v řetězci
   */
  public ArrayList vratSeznamPodProblemu(String s, char c) {
    String retPodProblemy;
    StringTokenizer st;
    ArrayList podProblemy=new ArrayList();

    if (s.indexOf(""+c) != -1) {
      // v s je znak uvozující seznam čísel --> následuje alespoň jedno číslo
      retPodProblemy=s.substring(s.indexOf(""+c)+1, s.length()); // +1 .. mezera
      st=new StringTokenizer(retPodProblemy);
      while (st.hasMoreTokens()) {
        podProblemy.add(new Integer(Integer.parseInt(st.nextToken())));
      }
      return(podProblemy);
    }
    else // nenalezen uvozující znak --> nenásleduje ani jedno číslo
      return(null);

  }

  /**
   * Spočte čas mezi intervaly retPocatek, retKonec. Předpokládá se formát
   * časů typu: HHMMSS.
   * @param retPocatek - počátek časového úseku
   * @param retKonec - konec časového úseku
   * @param <b>Vrací: </b> délku časového úseku v sekundách
   */
  public long spoctiCas(String retPocatek, String retKonec) {
    long casPocatek=0;
    long casKonec=0;

    // převod hodin HH (0,2) na sec
    casPocatek=casPocatek+3600*Integer.parseInt(retPocatek.substring(0,2));
    // převod minut MM (2,4) na sec
    casPocatek=casPocatek+60*Integer.parseInt(retPocatek.substring(2,4));
    // připočtení sec (4,6) SS
    casPocatek=casPocatek+Integer.parseInt(retPocatek.substring(4,6));

    casKonec=casKonec+3600*Integer.parseInt(retKonec.substring(0,2));
    casKonec=casKonec+60*Integer.parseInt(retKonec.substring(2,4));
    casKonec=casKonec+Integer.parseInt(retKonec.substring(4,6));
     return(casKonec-casPocatek);

  }

  /**
   * Převede long tvar časového úseku v sekundách na řetězec tvaru: HHMMSS.
   * @param casSec - časový úsek v sekundách
   * @param zobrazNulyCasu - přepínač navýznamových nul, Př: true --> 2:2:12, NE 02:02:12
   * @param <b>Vrací: </b> řetězec tvaru: HHMMSS
   */
  public String vratCasFormat(long casSec, boolean zobrazNulyCasu) {
    /* původní záměr využít toho co je v Javě, ale to by neumělo např: 25 hodin
    Calendar cal=new GregorianCalendar(2000,1,1,0,0,0);
    SimpleDateFormat ds=new SimpleDateFormat("HH:mm:ss");
    cal.roll(Calendar.HOUR, casSec/3600);
    cal.roll(Calendar.MINUTE, casSec/60);
    cal.roll(Calendar.SECOND, casSec%60);
    System.out.println("hehee" +ds.format(cal.getTime()));
    */
    String hodina;
    String minuta;
    String sec;

    if ( (casSec/3600) < 10 ) { // test pro zápis 0 před jednociferné číslo
      if (zobrazNulyCasu == true) // test pro zápis nevýznamových nul
        hodina = "0"+Long.toString(casSec/3600);
      else
        hodina = Long.toString(casSec/3600);
    }
    else
      hodina = Long.toString(casSec/3600);

    casSec=casSec-(casSec/3600)*3600; // odečet hodin
    if ( (casSec/60) < 10 ) { // test pro zápis 0 před jednociferné číslo
      if (zobrazNulyCasu == true) // test pro zápis nevýznamových nul
        minuta = "0"+Long.toString(casSec/60);
      else
        minuta = Long.toString(casSec/60);

    }
    else
      minuta = Long.toString(casSec/60);

    casSec=casSec-(casSec/60)*60;  // odečet minut
    if ( (casSec%60) < 10 )  { // test pro zápis 0 před jednociferné číslo
      if (zobrazNulyCasu == true) // test pro zápis nevýznamových nul
        sec = "0"+Long.toString(casSec);
      else
        sec = Long.toString(casSec);

    }
    else
      sec = Long.toString(casSec);

    if (zobrazNulyCasu == true )
      return(""+hodina+":"+minuta+":"+sec);
    else { // nemají se zobrazovat nevýznamové nuly
      if (hodina.compareTo("0") == 0) { // nedosažena ani jedna celá hodina
        if (minuta.compareTo("0") == 0) { // nedosažena ani jedna celá minuta
          if (sec.compareTo("0") == 0) // nedosažena ani sekunda
            return ("0");
          else
            return (""+sec);
        }
        else
          return ("" +minuta+":"+sec);

      }
      else
        return(""+hodina+":"+minuta+":"+sec);
    }
  }


  public static void main(String[] args) {
  }

}