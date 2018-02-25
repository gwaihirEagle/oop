package oop;
import java.util.*;
import java.io.*;

/**
 * Hlavní třída aplikace Odevzdávání.
 * Načte vstupní soubory a vytvoří objekt třídy Vyucujici.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */

public class Hlavni {
  private static final String TXT_PROBLEMY= "problemy.txt";
  private static final String TXT_STUDENTI= "studenti.txt";
  private static final String TXT_NAVSTEVY= "navstevy.txt";
  // pro ladící účely
  //private static final String TXT_ERR= "out\\erorlog.txt";

  public static void main(String[] args) {
    KontejnerStudentu studenti = new KontejnerStudentu();
    KontejnerProblemu problemy=new KontejnerProblemu();
    NacteniDat nacteniDat = new NacteniDat(TXT_PROBLEMY, TXT_STUDENTI,
                                           TXT_NAVSTEVY, studenti, problemy);
    nacteniDat.nactiStudenty();
    nacteniDat.nactiSeznamProblemu();
    studenti.seradPrijmJm();
    nacteniDat.nactiSeznamNavstev();
    /* pro ladění, přesměrování chybového výstupu
     try {
      FileOutputStream fo = new FileOutputStream(TXT_ERR);
      PrintStream perr = new PrintStream(fo);


      System.setOut(perr);
     }
     catch( IOException e) {
     System.out.println("Chyba cteni");
     }
     */
    new Vyucujici(studenti, problemy, TXT_NAVSTEVY);
    // po ukončení aplikace provede zápis proběhlých návštěv do souboru, který je
    // dán cestou v proměnné TXT_NAVSTEVY

  }   

}
